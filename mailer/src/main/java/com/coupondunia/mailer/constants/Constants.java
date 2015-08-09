package com.coupondunia.mailer.constants;

/**
 * Constants class contain the constants used in Application.
 * 
 * @author neeraj
 * neeraj.nayal.2008@gmail.com
 */
public class Constants {

	/**
	 * default private constructor - no outer class can create object of
	 * Constants class
	 */
	private Constants() {
	}

	/*
	 * constants for Key of mailer-app.properties file
	 */
	// key for for smtp user credentials
	public static final String SMTP_USER = "coupondunia.mailer.mail.user";
	public static final String SMTP_USER_PASSWD = "coupondunia.mailer.mail.passwd";

	// key for max retry to send a record , if fail
	public static final String DB_RETRY = "coupondunia.mailer.mail.retry";
	public static final int DEFAULT_DB_RETRY = 1;

	// key for thread pool size to be used by the application
	public static final String THREAD_POOL_SIZE = "coupondunia.mailer.thread.pool.size";
	public static final int DEFAULT_POOL_SIZE = 5;

	// key for waiting time for shutting down the mail service.
	public static final String THREAD_AWAIT_TIME = "coupondunia.mailer.await.time";
	public static final int DEFAULT_AWAIT_TIME = 30;

	// key for size of slot - number of record to be fetch @ one select query
	public static final String DB_SLOT_SIZE = "coupondunia.mailer.db.slot.size";;
	public static final int DEFAULT_SLOT_SIZE = 100;

	// key for size of slot - number of record to be fetch @ one select query
	public static final String DB_COMMIT_SIZE = "coupondunia.mailer.db.commit.size";;
	public static final int DEFAULT_COMMIT_SIZE = 30;

	/*
	 * Global Constants
	 */

	public static final String BLANK_STRING = "";
	public static final int ZERO = 0;

	/*
	 * Email Constants/Columns
	 */

	public static final String TBL_EMAIL_QUEUE = "EmailQueue";

	public static final String EMAIL_ID = "id";
	public static final String EMAIL_FROM = "from_email_address";
	public static final String EMAIL_TO = "to_email_address";
	public static final String EMAIL_SUBJECT = "subject";
	public static final String EMAIL_BODY = "body";
	public static final String EMAIL_STATUS = "status";
	public static final String EMAIL_ATTEMPT = "attempt";
	public static final String EMAIL_PTIME = "process_time";

	/*
	 * Database Setup Constants
	 */
	public static final String DRIVER_CLASS = "MYSQL_DB_DRIVER_CLASS";
	public static final String DB_URL = "MYSQL_DB_URL";
	public static final String DB_USER = "MYSQL_DB_USERNAME";
	public static final String DB_PWD = "MYSQL_DB_PASSWORD";

	/*
	 * Database related Constants
	 */

	// for status = pending
	public static final String STATUS_PENDING = "P";

	// for status = success
	public static final String STATUS_SUCCESS = "S";

	// for status = fail
	public static final String STATUS_FAIL = "F";

	// for locking the record status = Lock
	public static final String STATUS_LOCK = "L";

	/*
	 * SMTP setup related Constants
	 */

	// SMTP Auth Setting
	public static final String SMTP_AUTH = "mail.smtp.auth";
	public static final String SMTP_AUTH_VAL = "true";

	// SMTP TLS enable Setting
	public static final String SMTP_STARTTLS = "mail.smtp.starttls.enable";
	public static final String SMTP_STARTTLS_VAL = "true";

	// SMTP host name setting
	public static final String SMTP_HOST = "mail.smtp.host";
	public static final String SMTP_HOST_VAL = "smtp.gmail.com";

	// SMTP port number setting
	public static final String SMTP_PORT = "mail.smtp.port";
	public static final String SMTP_PORT_VAL = "587";

}
