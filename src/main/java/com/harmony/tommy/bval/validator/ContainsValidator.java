package com.harmony.tommy.bval.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.harmony.tommy.bval.constraints.Contains;

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
