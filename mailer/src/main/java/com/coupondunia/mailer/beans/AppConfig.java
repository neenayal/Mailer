package com.coupondunia.mailer.beans;

import com.coupondunia.mailer.constants.Constants;

/**
 * AppConfig class is container for all configuration setting used by mail
 * application
 * 
 * @author neeraj
 * neeraj.nayal.2008@gmail.com
 * */
public class AppConfig {

	/**
	 * thread pool size used by the application
	 */
	private int threadPoolSize;
	/**
	 * db slot size to be used while SQL select operation
	 */
	private int dbSlotSize;
	/**
	 * waiting time before shut down the mail service
	 */
	private int awaitTerminationSeconds;
	/**
	 * max number of retry to send email record if fail.
	 */
	private int maxRetry;
	/**
	 * user id to connect SMTP server
	 */
	private String smtpUserId = Constants.BLANK_STRING;
	/**
	 * User's password to connect SMTP server
	 */
	private String smtpPasswd = Constants.BLANK_STRING;

	/**
	 * database commit size
	 */
	private int commitSize;

	/**
	 * default constructor to set member variables
	 */
	public AppConfig() {
		// set the default values
		threadPoolSize = Constants.DEFAULT_POOL_SIZE;
		dbSlotSize = Constants.DEFAULT_SLOT_SIZE;
		awaitTerminationSeconds = Constants.DEFAULT_AWAIT_TIME;
		maxRetry = Constants.DEFAULT_DB_RETRY;
		commitSize = Constants.DEFAULT_COMMIT_SIZE;
	}

	/**
	 * @return commit size
	 */
	public int getCommitSize() {
		return commitSize;
	}

	/**
	 * @param commitSize
	 *            - to set commit size
	 */
	public void setCommitSize(int commitSize) {
		this.commitSize = commitSize;
	}

	/**
	 * @return - thread Pool Size
	 */
	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	/**
	 * @param threadPoolSize
	 *            - to set threadPoolSize
	 */
	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	/**
	 * @return - database Slot Size
	 */
	public int getDbSlotSize() {
		return dbSlotSize;
	}

	/**
	 * @param dbSlotSize
	 *            - to set dbSlotSize
	 */
	public void setDbSlotSize(int dbSlotSize) {
		this.dbSlotSize = dbSlotSize;
	}

	/**
	 * @return - max Retry
	 */
	public int getMaxRetry() {
		return maxRetry;
	}

	/**
	 * @param maxRetry
	 *            - to set maxRetry
	 */
	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	/**
	 * @return - SMTP User Id
	 */
	public String getSmtpUserId() {
		return smtpUserId;
	}

	/**
	 * @param smtpUserId
	 *            - to set smtpUserId
	 */
	public void setSmtpUserId(String smtpUserId) {
		this.smtpUserId = smtpUserId;
	}

	/**
	 * @return - SMTP Passwd
	 */
	public String getSmtpPasswd() {
		return smtpPasswd;
	}

	/**
	 * @param smtpPasswd
	 *            - to set smtpPasswd
	 */
	public void setSmtpPasswd(String smtpPasswd) {
		this.smtpPasswd = smtpPasswd;
	}

	/**
	 * @return - await time for Termination in Seconds
	 */
	public int getAwaitTerminationSeconds() {
		return awaitTerminationSeconds;
	}

	/**
	 * @param awaitTerminationSeconds
	 *            - to set awaitTerminationSeconds
	 */
	public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
		this.awaitTerminationSeconds = awaitTerminationSeconds;
	}
}
