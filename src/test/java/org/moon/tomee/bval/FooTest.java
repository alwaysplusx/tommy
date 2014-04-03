package org.moon.tomee.bval;

import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FooTest {
	
	private Validator validator; 
	
	@Before
	public void setUp() throws Exception {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Test
	public void testFoo() {
		Foo foo = new Foo();
		foo.setName("foo1");
		foo.setZipCode("ABC");
		Set<ConstraintViolation<Foo>> cvs = validator.validate(foo);
		assertEquals("constraint violation size is 1?", 1, cvs.size());
		for (ConstraintViolation<?> cv : cvs) {
			assertEquals("violation message is?", "zip code may not be right", cv.getMessage());
		}
	}

	@After
	public void tearDown() throws Exception {
	}

}
