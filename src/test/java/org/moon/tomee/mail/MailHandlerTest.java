package org.moon.tomee.mail;

import static org.junit.Assert.*;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MailHandlerTest {

	private static EJBContainer container;
	@EJB
	private MailHandler mailHandler;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Properties props = new Properties();
		props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
		container = EJBContainer.createEJBContainer(props);
	}
	
	@Before
	public void setUp(){
		try {
			container.getContext().bind("inject", this);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSendMain() {
		assertEquals(true, mailHandler.sendMain(new Mail("tomeejee@126.com","870757543@qq.com","Hello World!","Hi")));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		container.close();
	}
}
