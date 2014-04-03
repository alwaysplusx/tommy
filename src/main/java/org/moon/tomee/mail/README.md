### JavaMail API

JavaMail API提供了一种与平台无关和协议独立的框架来构建邮件和消息应用程序。 JavaMail API是作为可选包在Java SE平台使用，也包含在Java EE平台。

#### SimpleMailSender.java

	public class SimpleMailSender {
	
		public static void main(String[] args) {
			Authenticator auth = new Authenticator("tomeejee@126.com","abc123");
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
