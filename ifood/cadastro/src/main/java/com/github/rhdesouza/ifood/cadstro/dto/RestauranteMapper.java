package com.github.rhdesouza.ifood.cadstro.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.github.rhdesouza.ifood.cadstro.Restaurante;


@Mapper(componentModel = "cdi")
public interface RestauranteMapper {
	
	@Mapping(target="id", ignore = true)
	@Mapping(target="dataCriacao", ignore = true)
	@Mapping(target="dataAtualizacao", ignore = true)
	@Mapping(target="localizacao.id", ignore = true)
	public Restaurante toRestaurante(AdicionarRestauranteDTO dto);
	
	@Mapping(target="cnpj", ignore = true)
	public Restaurante toRestaurante(AtualizarRestauranteDTO dto, @MappingTarget Restaurante restaurante);
}
