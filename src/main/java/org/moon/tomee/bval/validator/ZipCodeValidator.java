package org.moon.tomee.bval.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.moon.tomee.bval.constraints.ZipCode;

public class ZipCodeValidator implements ConstraintValidator<ZipCode, String> {

	private int length;
	private boolean nullable;
	
	@Override
	public void initialize(ZipCode constraint) {
		length = constraint.length();
		nullable = constraint.nullable();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (nullable && value == null)
			return true;
		return value.length() == length;
	}


}
