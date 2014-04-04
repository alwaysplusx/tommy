package org.moon.tomee.bval.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.moon.tomee.bval.constraints.IDCard;

public class IDCardValidator implements ConstraintValidator<IDCard, String> {

	@Override
	public void initialize(IDCard constraint) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		//return value.matches("([0-9]{17}([0-9]|X))|([0-9]{15})");
		return true;
	}

}
