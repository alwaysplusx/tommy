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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.moon.tomee.bval.validator.IDCardValidator;

@NotNull(message = "idcard can't be null")
@Size(min = 15, max = 18, message = "idcard length may not be right")
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { IDCardValidator.class })
public @interface IDCard {

	String message() default "idcard is may not be right";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
