package com.github.rhdesouza.ifood.cadstro.infra;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDTOValidator implements ConstraintValidator<ValidDTO, DTO> {

	@Override
	public void initialize(ValidDTO constratintAnnotation) {
	}
	
	
	@Override
	public boolean isValid(DTO dto, ConstraintValidatorContext context) {
		return dto.isValid(context);
	}
	
	

}
