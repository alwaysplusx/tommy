package com.harmony.tommy.bval;

import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.harmony.tommy.bval.Bar;
import com.harmony.tommy.bval.Foo;

public class BarTest {

	private Validator validator;

	@Before
	public void setUp() throws Exception {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Test
	public void testBar() {
		Bar bar = new Bar();
		bar.setFoo(new Foo());
		Set<ConstraintViolation<Bar>> cvs = validator.validate(bar);
		assertEquals("foo name violation?", 1, cvs.size());
	}

	@After
	public void tearDown() throws Exception {
	}


}
