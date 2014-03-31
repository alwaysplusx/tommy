package org.moon.tomee.mail.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.moon.tomee.mail.Mail;
import org.moon.tomee.mail.MailHandler;

@Stateless
public class MailHandlerImpl implements MailHandler {

	@Resource
	private Session session;

	@Override
	public boolean sendMain(Mail mail) {
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(mail.getFromAddress()));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getToAddress()));
			message.setSubject(mail.getSubject());
			message.setText((String) mail.getContent());
			message.setSender(new InternetAddress(mail.getToAddress()));
			message.setSentDate(new Date());
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

}
