package org.moon.tomee.mail;

public class Mail {
	
	private String fromAddress;
	private String toAddress;
	private Object content;
	private String subject;
	private String[] attachFileNames;

	public Mail() {
	}

	public Mail(String fromAddress, String toAddress, Object content, String subject) {
		this.fromAddress = fromAddress;
		this.toAddress = toAddress;
		this.content = content;
		this.subject = subject;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(String[] attachFileNames) {
		this.attachFileNames = attachFileNames;
	}
}
