package org.moon.tomee.mail;

public interface MailHandler {
	/**
	 * 发送文本格式邮件
	 * @param mail
	 * @return
	 */
	public boolean sendTextMain(Mail mail);
	/**
	 * 发送模版邮件
	 * @param mail
	 * @return
	 */
	public boolean sendHtmlTempletMail(Mail mail);
	/**
	 * 发送普通HTML邮件
	 * @param mail
	 * @return
	 */
	public boolean sendHtmlMail(Mail mail);
}
