package com.coupondunia.mailer.concurrent;

import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import com.coupondunia.mailer.base.Application;
import com.coupondunia.mailer.beans.EmailQueue;
import com.coupondunia.mailer.constants.Constants;
import com.coupondunia.mailer.utils.MailUtils;
import com.coupondunia.mailer.utils.QueryUtils;

/**
 * MailRunner is subclass of AbstractServiceRunner which implements Runnable
 * MailRunner class fetch some record from db with the help of QueryUtils and
 * send email and update the record in database.
 * 
 * @author neeraj
 * neeraj.nayal.2008@gmail.com
 */
public class MailRunner extends AbstractServiceRunner {

	/**
	 * records contain the list of EmailQueue will be used to sent email.
	 */
	private ArrayList<EmailQueue> records;

	/**
	 * one single Session is used for single individual thread. Session is for
	 * sending mail.
	 */
	Session session = null;

	/**
	 * transport object to be get from SMTP session
	 */
	Transport transport = null;

	/**
	 * default Constructor
	 */
	public MailRunner() {
		// default behaviour is empty/nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coupondunia.mailer.concurrent.AbstractServiceRunner#init()
	 */
	@Override
	protected void init() {
		// default behaviour is empty/nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coupondunia.mailer.concurrent.AbstractServiceRunner#acquireResources
	 * ()
	 */
	@Override
	protected void acquireResources() throws Exception {

		Properties smtpProps = Application.getInstance().getSMPTproperty();
		Authenticator auth = Application.getInstance().getMailAuthenticator();
		session = Session.getInstance(smtpProps, auth);

		// connect to the the server only once.
		try {
			transport = session.getTransport("smtp");
			transport.connect();
		} catch (Exception e) {
			System.out.println("[Error] Unable to connect SMTP server " + e);
			throw e;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coupondunia.mailer.concurrent.AbstractServiceRunner#continueService()
	 */
	protected boolean continueService() throws Exception {

		// get Record list from database using QueryUtils.
		records = new ArrayList<EmailQueue>();
		records = QueryUtils.getEmailRecords();

		if (records.size() > 0) {
			// if record found, continue the service by executing task() method
			validate();
			return true;
		} else { 
			// if records is empty, no need to continue the service
			return false;
		}
	}
	
	/**
	 * validate the records fetch from the database.
	 * if any records contains invalid email id, remove it
	 * from the record set.
	 */
	private void validate(){
		// default behaviour is empty/nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coupondunia.mailer.concurrent.AbstractServiceRunner#task()
	 */
	public void task() throws Exception {

		// send mail to all email records.
		for (int inner = 0; inner < records.size(); inner++) {
			EmailQueue eq = records.get(inner);
			Message msg = MailUtils.composeMail(session, eq);
			// System.out.println(Thread.currentThread() +"Mail Id : "
			// +eq.getEmailPk()+ "sent succesfully.");
			msg.saveChanges();
			try {
				transport.sendMessage(msg, msg.getAllRecipients());

				// if no exception occur, mail sending completed successfully.
				eq.setStatus(Constants.STATUS_SUCCESS);
				System.out.println("[Success] " + Thread.currentThread()
						+ "Email Record with Mail Id : [" + eq.getEmailId()
						+ "] sent succesfully.");
			} catch (Exception e) {
				// mail sending process fail.
				eq.setStatus(Constants.STATUS_FAIL);
				System.out.println("[Fail] " + Thread.currentThread()
						+ "Email Record with Mail Id : [" + eq.getEmailId()
						+ "] sent fail.");
			} finally {
				eq.setAttempt(eq.getAttempt() + 1);
				eq.setSendTime(new Timestamp(System.currentTimeMillis()));
			}
		}
		QueryUtils.updateStatusOfRecord(records);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coupondunia.mailer.concurrent.AbstractServiceRunner#releaseResource()
	 */
	@Override
	protected void releaseResource() throws Exception {
		records.clear();
		try {
			transport.close();
		} catch (MessagingException e) {
			System.out
					.println("[Warning] Unable to close SMTP Session Transport Object");
		}
	}

}
