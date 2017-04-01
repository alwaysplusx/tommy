package com.harmony.tommy.bval;

import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.harmony.tommy.bval.MyGroup;
import com.harmony.tommy.bval.Username;

public class UsernameTest {

private Validator validator; 
	
	@Before
	public void setUp() throws Exception {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Test
	public void testUsername() {
		Username username = new Username();
		Set<ConstraintViolation<Username>> cvs = validator.validate(username, MyGroup.class);
		for (ConstraintViolation<?> cv : cvs) {
			System.out.println(cv.getMessage());
		}
		assertEquals("violation size is 1", 1, cvs.size());
	}

	@After
	public void tearDown() throws Exception {
	}

}
