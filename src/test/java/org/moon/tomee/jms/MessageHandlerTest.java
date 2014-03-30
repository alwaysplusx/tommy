package org.moon.tomee.jms;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.jms.JMSException;
import javax.naming.NamingException;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageHandlerTest {

	private static EJBContainer container;

	@EJB
	private MessageHandler messageHandler;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Properties props = new Properties();
		props.put("openejb.conf.file", "src/main/resources/conf/openejb.xml");
		container = EJBContainer.createEJBContainer(props);
	}

	@Before
	public void setUp() {
		try {
			container.getContext().bind("inject", this);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSendMessage() {
		try {
			messageHandler.sendMessage("hello world");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReceiveMessage(){
		try {
			assertEquals("hello world", messageHandler.receiveMessage());
		} catch (JMSException e) {
			e.printStackTrace();
		}		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		container.close();
	}

}
