package org.moon.tomee.bval.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.moon.tomee.bval.constraints.Contains;

public class ContainsValidator implements ConstraintValidator<Contains, String> {

	private String content;

	public void initialize(Contains constraint) {
		content = constraint.content();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return false;
		return value.contains(content);
	}

}
