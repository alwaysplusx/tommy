package com.harmony.tommy.bval;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.harmony.tommy.bval.Baz;

public class BazTest {

	private Validator validator;

	@Before
	public void setUp() throws Exception {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Test
	public void testBar() {
		Baz baz = new Baz();
		baz.setText("this is automata");
		Set<ConstraintViolation<Baz>> cvs = validator.validate(baz);
		for(ConstraintViolation<?> cv : cvs){
			System.out.println(cv.getMessage());
		}
	}

	@After
	public void tearDown() throws Exception {
	}

}
