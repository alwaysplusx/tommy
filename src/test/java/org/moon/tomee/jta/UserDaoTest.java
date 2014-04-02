package org.moon.tomee.jta;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.transaction.UserTransaction;

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
	public void testSaveUser() {
		userCMPDao.saveUser(new User("AAA"));
		userBMPDao.saveUser(new User("BBB"));
		assertEquals("Save user with CMP & BMP", 2l, userBMPDao.count());
	}
	
	@Test
	@Ignore
	public void testCMPSaveUser() throws Exception{
		//ux.begin();
		userCMPDao.saveUser(new User("AAA"));
		//ux.commit();
		assertEquals("Save user with CMP", 1l, userBMPDao.count());
	}
	
	@Test
	@Ignore
	public void testCMPCount() throws Exception{
		ux.begin();
		userCMPDao.saveUser(new User("AAA"));;
		ux.commit();
	}
	
	@Test
	@Ignore
	public void testBMPSaveUser() throws Exception{
		ux.begin();
		userBMPDao.saveUser(new User("AAA"));
		ux.commit();
		assertEquals("Save user with BMP", 1l, userBMPDao.count());
	}
	
	@Test
	public void testSaveWithCMPDao(){
		userBMPDao.saveWithCMPDao(new User("AAA"), new User("BBB"));
		assertEquals("save with CMP Dao", 2l, userBMPDao.count());
	}
	
	public void testSaveWithBMPDao(){
		userCMPDao.saveWithBMPDao(new User("AAA"), new User("BBB"));
	}
	
	@After
	public void tearDown() throws Exception {
		container.close();
	}

}
