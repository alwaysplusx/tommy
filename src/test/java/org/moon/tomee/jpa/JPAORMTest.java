package org.moon.tomee.jpa;

import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class JPAORMTest {

	private EJBContainer container;
	@PersistenceContext(unitName = "hibernate-moon")
	private EntityManager em;
	@Resource
	private UserTransaction ux;
	
	@Before
	public void setUp() throws Exception {
		Properties props = new Properties();
		props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
		container = EJBContainer.createEJBContainer(props);
		container.getContext().bind("inject", this);
	}
	
	@Test
	@Ignore
	public void testOneToOne() throws Exception {
		
	}

	@Test
	public void testOneToMany() throws Exception {

	}
	
	@Test
	public void testManyToMany() throws Exception {
		
	}
	
	@After
	public void tearDown() throws Exception {
		container.close();
	}


}
