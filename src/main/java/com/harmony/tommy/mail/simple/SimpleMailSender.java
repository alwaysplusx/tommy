package com.harmony.tommy.mail.simple;

import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SimpleMailSender {

    public static void main(String[] args) {
        Authenticator auth = new Authenticator("tomeejee@126.com", "abc123");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.126.com");
        props.put("mail.smtp.port", "25");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, auth);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("tomeejee@126.com"));
            message.setRecipient(RecipientType.TO, new InternetAddress("870757543@qq.com"));
            message.setSubject("Hi");
            message.setText("Hello World!");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
