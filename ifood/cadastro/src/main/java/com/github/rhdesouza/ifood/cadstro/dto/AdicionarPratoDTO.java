package com.github.rhdesouza.ifood.cadstro.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Size;

public class AdicionarPratoDTO {

	public String nome;
	
	@Size(min = 3, max = 30)
	public String descricao;
	public BigDecimal preço;

}
