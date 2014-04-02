package org.moon.tomee.jpa;

import static org.junit.Assert.*;

import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.moon.tomee.jpa.persistence.Passport;
import org.moon.tomee.jpa.persistence.User;

public class JPAORMTest {

	private static EJBContainer container;
	@PersistenceContext(unitName = "hibernate-moon")
	private EntityManager em;
	@Resource
	private UserTransaction ux;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Properties props = new Properties();
		props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
		container = EJBContainer.createEJBContainer(props);
	}
	
	@Before
	public void setUp() throws Exception{
		container.getContext().bind("inject", this);
	}
	
	@Test
	public void testOneToOne() throws Exception {
		ux.begin();
		User user = new User("AAA");
		Passport passport = new Passport("China");
		passport.setUser(user);
		user.setPassport(passport);
		em.persist(user);
		ux.commit();
		User user2 = null;
		Passport passport2 = null;
		assertNotEquals(null, (user2 = (User)em.find(User.class, user.getUserId())));
		assertNotEquals(null, (passport2 = user2.getPassport()));
		assertEquals(user.getUsername(), user2.getUsername());
		assertEquals(passport.getCountry(), passport2.getCountry());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		container.close();
	}


}
