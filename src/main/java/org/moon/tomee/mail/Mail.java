package org.moon.tomee.mail;

public abstract class Mail {

	protected String fromAddress;
	protected String[] toAddresses;
	protected String[] ccAddresses;
	protected String[] bccAddresses;
	protected String subject;
	protected Object content;
	protected String[] attachFileNames;
	protected MailTemplet mailTemplet;

	public Mail() {
	}

	public Mail(String fromAddress, String[] toAddresses, String subject, Object content) {
		this.fromAddress = fromAddress;
		this.toAddresses = toAddresses;
		this.subject = subject;
		this.content = content;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String[] getToAddresses() {
		return toAddresses;
	}

	public void setToAddresses(String[] toAddresses) {
		this.toAddresses = toAddresses;
	}

	public String[] getCcAddresses() {
		return ccAddresses;
	}

	public void setCcAddresses(String[] ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public String[] getBccAddresses() {
		return bccAddresses;
	}

	public void setBccAddresses(String[] bccAddresses) {
		this.bccAddresses = bccAddresses;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public MailTemplet getMailTemplet() {
		return mailTemplet;
	}

	public void setMailTemplet(MailTemplet mailTemplet) {
		this.mailTemplet = mailTemplet;
	}

	public String[] getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(String[] attachFileNames) {
		this.attachFileNames = attachFileNames;
	}

}
