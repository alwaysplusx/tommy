package org.moon.tomee.mail.impl;

import java.io.File;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.moon.tomee.mail.Mail;
import org.moon.tomee.mail.MailHandler;
import org.moon.tomee.mail.MailTemplet;

@Stateless
public class MailHandlerImpl implements MailHandler {

	@Resource
	private Session session;
	private Logger log = Logger.getLogger(MailHandlerImpl.class);
	private Map<String, String> mailMessageTempletMap = new HashMap<String, String>();
	private static String DEFAULT_CHARSET = "text/html;charset=utf-8";
	
	
	@Override
	public boolean sendTextMain(Mail mail) {
		try {
			MimeMessage message = buildMessage(mail);
			Multipart multipart = new MimeMultipart();
			addMailContent(multipart, (String) mail.getContent(), DEFAULT_CHARSET);
			if (mail.getAttachFileNames() != null && mail.getAttachFileNames().length > 0) {
				addAttachments(multipart, mail.getAttachFileNames());
			}
			message.setContent(multipart);
			Transport.send(message);
			return true;
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
	
	@Override
	public boolean sendHtmlMail(Mail mail) {
		return sendTextMain(mail);
	}
	
	@Override
	public boolean sendHtmlTempletMail(Mail mail) {
		try {
			MimeMessage message = buildMessage(mail);
			Multipart multipart = new MimeMultipart();
			MailTemplet templet = mail.getMailTemplet();
			addMailContent(multipart, templet.getPath(), templet.getArguments(), DEFAULT_CHARSET);
			if (mail.getAttachFileNames() != null && mail.getAttachFileNames().length > 0) {
				addAttachments(multipart, mail.getAttachFileNames());
			}
			message.setContent(multipart);
			Transport.send(message);
			return true;
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
	
	private void addMailContent(Multipart multipart, String htmlContent, String type) throws Exception{
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(htmlContent, type);
		multipart.addBodyPart(bodyPart);
	}
	
	private void addAttachments(Multipart multipart, String[] fileNames) throws Exception{
		MimeBodyPart attachmentsPart = new MimeBodyPart();
		for(String fileName : fileNames){
			FileDataSource fds = new FileDataSource(fileName);
			DataHandler dataHandler = new DataHandler(fds);
			attachmentsPart.setDataHandler(dataHandler);
			attachmentsPart.setFileName(fds.getName());
		}
		multipart.addBodyPart(attachmentsPart);
	}
	
	private void addMailContent(Multipart multipart, String path, Object[] arguments, String type) throws Exception{
		this.addMailContent(multipart, MessageFormat.format(readMailMessageTemplet(path), arguments), type);
	}
	
	/**
	 * 创建邮件基础信息.发送人/接收人/抄送人/密送人/主题/发送时间
	 * 邮件正文未填写
	 * @param mail
	 * @return
	 * @throws Exception
	 */
	private MimeMessage buildMessage(Mail mail) throws Exception{
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(mail.getFromAddress()));
		message.setRecipients(RecipientType.TO, parseInternetAddresses(mail.getToAddresses()));
		message.setSubject(mail.getSubject());
		if (mail.getCcAddresses() != null && mail.getCcAddresses().length > 0) {
			message.setRecipients(RecipientType.CC, parseInternetAddresses(mail.getCcAddresses()));
		}
		if (mail.getBccAddresses() != null && mail.getBccAddresses().length > 0) {
			message.setRecipients(RecipientType.CC, parseInternetAddresses(mail.getBccAddresses()));
		}
		message.setSentDate(new Date());
		return message;
	}
	
	private InternetAddress[] parseInternetAddresses(String[] addresses){
		if(addresses == null || addresses.length < 1) return null;
		InternetAddress[] internetaddresses = new InternetAddress[addresses.length];
		for (int i = 0; i < addresses.length; i++) {
			try {
				internetaddresses[i] = new InternetAddress(addresses[i]);
			} catch (AddressException e) {
				log.warn("address "+ addresses[i] +" can't parse to internet address");
			}
		}
		return internetaddresses;
	}
	
	private String readMailMessageTemplet(String path) throws Exception{
		if(!mailMessageTempletMap.containsKey(path)){
			mailMessageTempletMap.put(path, readMailMessageTempletFromPath(path));
		}
		return mailMessageTempletMap.get(path);
	}

	private String readMailMessageTempletFromPath(String path) throws Exception {
		SAXReader saxReader = new SAXReader();
		File file = new File(path);
		if (path != null && (path.endsWith(".xml") || path.endsWith(".html") || path.endsWith(".txt"))) {
			if(file.exists() && file.isFile()){
				Document document = saxReader.read(file);
				Element messageElemet = document.getRootElement().element("html");
				return messageElemet.asXML();
			}
		} 
		throw new Exception("无效邮件模版链接" + file.getAbsolutePath());
	}
	
	@PreDestroy
	private void destory(){
		mailMessageTempletMap.clear();
	}

}
