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
	@EJB(beanName = "UserCMTDaoImpl")
	private UserDao userCMTDao;
	@EJB(beanName = "UserBMTDaoImpl")
	private UserDao userBMTDao;
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
		userCMTDao.saveUser(new User("AAA"));
		userBMTDao.saveUser(new User("BBB"));
		assertEquals("Save user with CMT & BMT", 2l, userBMTDao.count());
	}
	
	@Test
	@Ignore
	public void testCMTSaveUser() throws Exception{
		//ux.begin();
		userCMTDao.saveUser(new User("AAA"));
		//ux.commit();
		assertEquals("Save user with CMT", 1l, userBMTDao.count());
	}
	
	@Test
	@Ignore
	public void testCMTCount() throws Exception{
		ux.begin();
		userCMTDao.saveUser(new User("AAA"));;
		ux.commit();
	}
	
	@Test
	@Ignore
	public void testBMTSaveUser() throws Exception{
		ux.begin();
		userBMTDao.saveUser(new User("AAA"));
		ux.commit();
		assertEquals("Save user with BMT", 1l, userBMTDao.count());
	}
	
	@Test
	public void testSaveWithCMTDao(){
		userBMTDao.saveWithCMTDao(new User("AAA"), new User("BBB"));
		assertEquals("save with CMT Dao", 2l, userBMTDao.count());
	}
	
	public void testSaveWithBMTDao(){
		userCMTDao.saveWithBMTDao(new User("AAA"), new User("BBB"));
	}
	
	@After
	public void tearDown() throws Exception {
		container.close();
	}

}
