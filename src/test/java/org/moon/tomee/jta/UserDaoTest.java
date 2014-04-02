package org.moon.tomee.jta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.moon.tomee.jpa.persistence.User;

public class UserDaoTest {

	private EJBContainer container;
	@EJB(beanName = "UserCMPDaoImpl")
	private UserDao userCMPDao;
	@EJB(beanName = "UserBMPDaoImpl")
	private UserDao userBMPDao;

	@Before
	public void setUp() throws Exception {
		Properties props = new Properties();
		props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
		container = EJBContainer.createEJBContainer(props);
		container.getContext().bind("inject", this);
	}

	@Test
	public void testSaveUser() {
		userCMPDao.saveUser(new User("AAA"));
		userBMPDao.saveUser(new User("BBB"));
		assertEquals("Save user with CMP & BMP", 2l, userBMPDao.count());
	}

	@Test
	@Ignore
	public void testDeleteUser() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testCMPSaveWithOtherDao(){
		userCMPDao.saveWithOtherDao(new User("AAA"), new User("BBB"));
		assertEquals("Container manager Persistence", 2l, userCMPDao.count());
	}

	@Test
	public void testBMPSaveWithOtherDao(){
		userBMPDao.saveWithOtherDao(new User("AAA"), new User("BBB"));
		assertEquals("Bean manager Persistence Bean", 2l, userCMPDao.count());
	}
	
	@After
	public void tearDown() throws Exception {
		container.close();
	}

}
