package org.moon.tomee.cdi.base;

import static org.junit.Assert.*;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourseTest {

	@EJB 
	private Course course;
	
	@Before
	public void setUp() throws Exception {
		Properties props = new Properties();
		props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
		EJBContainer.createEJBContainer(props).getContext().bind("inject", this);;
	}

	@Test
	public void test() {
		assertNotEquals("course is can't be null", null, course);
		assertNotEquals("faculty can't be null", null, course.getFaculty());
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
