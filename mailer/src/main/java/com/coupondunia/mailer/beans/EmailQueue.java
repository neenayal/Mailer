package com.coupondunia.mailer.beans;

import java.sql.Timestamp;

/**
 * EmailQueue class is container of EmailQueue table of database. it contain all
 * its column to store/retrieve/update the database record.
 * 
 * @author neeraj
 * neeraj.nayal.2008@gmail.com
 */
public class EmailQueue {

	/**
	 * email id column of EmailQueue table
	 */
	private long email_Id;
	/**
	 * from_email column of EmailQueue table
	 */
	private String from_email;
	/**
	 * to_email column of EmailQueue table
	 */
	private String to_email;
	/**
	 * subject column of EmailQueue table
	 */
	private String subject;
	/**
	 * message column of EmailQueue table
	 */
	private String body;
	/**
	 * status column of EmailQueue table
	 */
	private String status;
	/**
	 * attempt column of EmailQueue table
	 */
	private int attempt;

	/**
	 * 
	 */
	private Timestamp sendTime;

	/**
	 * @return the sending time
	 */
	public Timestamp getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime - to set sending time
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return - email id of Particular record of EmailQueue table.
	 */
	public long getEmailId() {
		return email_Id;
	}

	/**
	 * @param email_pk
	 *            - to set email id of Particular record of EmailQueue table.
	 */
	public void setEmailId(long email_Id) {
		this.email_Id = email_Id;
	}

	/**
	 * @return from_email of Particular record of EmailQueue table.
	 */
	public String getFromEmail() {
		return from_email;
	}

	/**
	 * @param from_email
	 *            - to set from_email of Particular record of EmailQueue table.
	 */
	public void setFromEmail(String from_email) {
		this.from_email = from_email;
	}

	/**
	 * @return to_email of Particular record of EmailQueue table.
	 */
	public String getToEmail() {
		return to_email;
	}

	/**
	 * @param to_email
	 *            - to set to_email of Particular record of EmailQueue table.
	 */
	public void setToEmail(String to_email) {
		this.to_email = to_email;
	}

	/**
	 * @return - subject of Particular record of EmailQueue table.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            - to set subject of Particular record of EmailQueue table.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return - body of Particular record of EmailQueue table.
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param message
	 *            - to set body of Particular record of EmailQueue table.
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return staus of Particular record of EmailQueue table.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            - to set status of Particular record of EmailQueue table.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return attempt made by Particular record of EmailQueue table.
	 */
	public int getAttempt() {
		return attempt;
	}

	/**
	 * @param attempt
	 *            - set attempt of Particular record of EmailQueue table.
	 */
	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}

}
