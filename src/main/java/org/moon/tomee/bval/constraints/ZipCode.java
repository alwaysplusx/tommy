package org.moon.tomee.bval.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.moon.tomee.bval.validator.ZipCodeValidator;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { ZipCodeValidator.class })
public @interface ZipCode {
	
	static final String MESSAGE = "zip code may not be right";
	
	int length() default 6;
	
	boolean nullable() default true;
	
    String message() default MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
