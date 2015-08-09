package com.coupondunia.mailer.utils;

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.coupondunia.mailer.beans.EmailQueue;

/**
 * MailUtils provide Apis for Mail related task.
 * 
 * @author neeraj
 * neeraj.nayal.2008@gmail.com
 */
public class MailUtils {

	/**
	 * private default constructor, can't create onject of MailUtils
	 */
	private MailUtils() {

	}

	/**
	 * Compose mail create a MimeMessage object for specific session
	 * 
	 * @param session
	 *            - session of SMTP
	 * @param email
	 *            - email detail to create Mimemessage
	 * @return - Mimemessage containing email info
	 */
	public static Message composeMail(Session session, EmailQueue email) {

		Message message = null;
		String mailTo = email.getToEmail();
		String mailFrom = email.getFromEmail();
		String subject = email.getSubject();
		String body = email.getBody();

		try {
			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailFrom, mailFrom));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(mailTo));
			message.setSubject(subject);
			message.setText(body);

		} catch (MessagingException e) {
			System.out.println("[Error] Unable to compose email message.");
		} catch (UnsupportedEncodingException use) {
			System.out.println("[Error] Unable to compose email message.");
		}
		return message;
	}

}
