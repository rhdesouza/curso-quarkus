package com.github.rhdesouza.ifood.cadstro;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.SimplyTimed;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.github.rhdesouza.ifood.cadstro.dto.AdicionarPratoDTO;
import com.github.rhdesouza.ifood.cadstro.dto.AdicionarRestauranteDTO;
import com.github.rhdesouza.ifood.cadstro.dto.AtualizarPratoDTO;
import com.github.rhdesouza.ifood.cadstro.dto.AtualizarRestauranteDTO;
import com.github.rhdesouza.ifood.cadstro.dto.PratoMapper;
import com.github.rhdesouza.ifood.cadstro.dto.RestauranteMapper;


@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurante")
@RolesAllowed("proprietario")
@SecurityScheme(securitySchemeName = "ifood-oauth", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(password = @OAuthFlow(tokenUrl = "http://localhost:8180/auth/realms/ifood/protocol/openid-connect/token")))
@SecurityRequirement(name = "ifood-oauth", scopes = {})
public class RestauranteResource {

	@Inject
	RestauranteMapper restauranteMapper;
	
	@Inject
	PratoMapper pratoMapper;
	
	@Inject
	@Channel("restaurantes")
	Emitter<String> emitter;
	

	@GET
	@Counted(name="quantidade buscas Restaurante")
	@SimplyTimed(name="Tempo simples de busca")
	@Timed(name = "Tempo completo de busca")
	public List<Restaurante> hello() {
		return Restaurante.listAll();
	}

	@POST
	@Transactional
	public Response adicionar(@Valid AdicionarRestauranteDTO dto) {
		Restaurante restaurante = restauranteMapper.toRestaurante(dto);
		restaurante.persist();
		
		String json = JsonbBuilder.create().toJson(restaurante);
		
		emitter.send(json);
		
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public void atualizar(@PathParam("id") Long id, @Valid AtualizarRestauranteDTO dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
		if (restauranteOp.isEmpty())
			throw new NotFoundException();

		Restaurante restaurante = restauranteOp.get();
		restauranteMapper.toRestaurante(dto, restaurante);
		restaurante.persist();
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public void apagar(@PathParam("id") Long id) {
		Optional<Restaurante> restaurante = Restaurante.findByIdOptional(id);

		restaurante.ifPresentOrElse(Restaurante::delete, () -> {
			throw new NotFoundException();
		});

	}

	/**
	 * Pratos
	 */

	@GET
	@Path("{idRestaurante}/pratos")
	@Tag(name = "prato")
	public List<Prato> hello(@PathParam("idRestaurante") Long idRestaurante) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty())
			throw new NotFoundException("O Restaurante não existe");

		return Prato.list("restaurante", restauranteOp.get());
	}

	@POST
	@Path("{idRestaurante}/pratos")
	@Transactional
	@Tag(name = "prato")
	public Response adicionar(@PathParam("idRestaurante") Long idRestaurante, @Valid AdicionarPratoDTO dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty())
			throw new NotFoundException("O Restaurante não existe");

		Prato prato = pratoMapper.toPrato(dto);
		prato.restaurante = restauranteOp.get();
		prato.persist();

		return Response.status(Status.CREATED).build();
		
	}

	@PUT
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	@Tag(name = "prato")
	public void atualizar(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, @Valid AtualizarPratoDTO dto) {
		Optional<Prato> pratoOp = Prato.findByIdOptional(id);
		if (pratoOp.isEmpty())
			throw new NotFoundException();

		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty())
			throw new NotFoundException("O Restaurante não existe");

		Prato prato = pratoOp.get();
		pratoMapper.toPrato(dto, prato);
		prato.persist();
	}

	@DELETE
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	@Tag(name = "prato")
	public void apagarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
		Optional<Prato> prato = Prato.findByIdOptional(id);

		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty())
			throw new NotFoundException("O Restaurante não existe");

		prato.ifPresentOrElse(Prato::delete, () -> {
			throw new NotFoundException();
		});

	}
}