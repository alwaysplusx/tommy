package org.moon.tomee.jpa.dao;

import static org.junit.Assert.*;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.moon.tomee.jpa.persistence.User;

public class UserRepositoryTest {

	private static EJBContainer container;
	@EJB
	private UserRepository userRepository;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Properties props = new Properties();
		props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
		container = EJBContainer.createEJBContainer(props);
	}

	@Before
	public void setUp() throws Exception {
		container.getContext().bind("inject", this);
	}

	@Test
	public void test() {
		User user = new User("AAA");
		userRepository.saveUser(user);
		assertEquals("after save user", 1l, userRepository.count());
		user.setUsername("BBB");
		userRepository.updateUser(user);
		assertEquals("username equals BBB size", 1, userRepository.findUserByUsername("BBB").size());
		assertEquals("after update user", "BBB", userRepository.findUserById(user.getId()).getUsername());
		userRepository.deleteUser(userRepository.findUserById(user.getId()));
		assertEquals("after delete user", 0l, userRepository.count());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		container.close();
	}
}
