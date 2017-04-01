package com.harmony.tommy.mail;

import static org.junit.Assert.*;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.harmony.tommy.mail.HtmlTempletMail;
import com.harmony.tommy.mail.MailHandler;
import com.harmony.tommy.mail.SimpleMail;

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
	@Ignore
	public void testSendTextMail(){
		SimpleMail mail = new SimpleMail("tomeejee@126.com", new String[]{"870757543@qq.com"},"Come On","I Support You");
		mail.setAttachFileNames(new String[]{"D:/a.txt"});
		assertEquals(true, mailHandler.sendTextMain(mail));
	}
	
	@Test
	public void testSendHtmlMail(){
		HtmlTempletMail mail = new HtmlTempletMail("tomeejee@126.com",new String[]{"870757543@qq.com"},"Html Mail","src/main/resources/mailMessageTemplet.xml",new Object[]{"wuxin"});
		assertEquals(true, mailHandler.sendHtmlTempletMail(mail));
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		container.close();
	}
}
