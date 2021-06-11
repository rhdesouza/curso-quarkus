package com.github.rhdesouza.ifood.cadstro.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.github.rhdesouza.ifood.cadstro.Prato;

@Mapper(componentModel = "cdi")
public interface PratoMapper {

	public Prato toPrato(AdicionarPratoDTO dto);

	public Prato toPrato(AtualizarPratoDTO dto, @MappingTarget Prato prato);

}
