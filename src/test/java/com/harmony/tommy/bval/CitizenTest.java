package com.harmony.tommy.bval;

import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.harmony.tommy.bval.Citizen;

public class CitizenTest {

	private Validator validator;

	@Before
	public void setUp() throws Exception {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Test
	public void testCitizen() {
		Citizen citizen = new Citizen();
		//citizen.setIdCard("111111111111111");
		Set<ConstraintViolation<Citizen>> cvs = validator.validate(citizen);
		for(ConstraintViolation<?> cv : cvs){
			System.out.println(cv.getMessage());
		}
		assertEquals("violation is not empty", false, cvs.isEmpty());
	}

	@After
	public void tearDown() throws Exception {
	}
}
