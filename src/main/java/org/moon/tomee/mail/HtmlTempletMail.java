package org.moon.tomee.mail;

public class HtmlTempletMail extends Mail {

	public HtmlTempletMail() {
		super();
	}

	public HtmlTempletMail(String fromAddress, String[] toAddresses, String subject, String path, Object[] arguments) {
		super(fromAddress, toAddresses, subject, null);
		this.setMailTemplet(new MailTemplet(path, arguments));
	}

	public HtmlTempletMail(String fromAddress, String[] toAddresses, String subject, MailTemplet templet) {
		super(fromAddress, toAddresses, subject, null);
		this.setMailTemplet(templet);
	}

}
