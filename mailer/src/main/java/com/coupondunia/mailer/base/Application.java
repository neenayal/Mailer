package com.coupondunia.mailer.base;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.coupondunia.mailer.beans.AppConfig;
import com.coupondunia.mailer.constants.Constants;
import com.coupondunia.mailer.utils.ResourceUtils;

/**
 * Application class is singleton implementation it contain all
 * the global objects and application conf. setup.
 * @author neeraj 
 * neeraj.nayal.2008@gmail.com
 */
public class Application {

	/**
	 * the only object of Application class
	 */
	private static Application instance = new Application();

	/**
	 * properties file to create JDBC connection mailer-jdbc.properties file
	 * contain sufficient info for JDBC connection
	 */
	private final static String jdbcConfig = "mailer-jdbc.properties";

	/**
	 * properties file to application configuration setup mailer-jdbc.properties
	 * file contain sufficient info SMTP setup and thread pool size, db slot
	 * size etc.
	 */
	private final static String customConfig = "mailer-app.properties";

	/**
	 * SMTP property file, will be shared by all the thread in application
	 */
	private Properties smtpProps = null;

	/**
	 * Mail Authenticator, will be shared by all the thread in application
	 */
	private Authenticator auth = null;

	/**
	 * 
	 */
	private static AppConfig appConfig = null;

	/**
	 * private default constructor. No outer class can create object of
	 * Application class.
	 */
	private Application() {
		appConfig = new AppConfig();
	}

	/**
	 * @return the only object of Application class
	 */
	public static Application getInstance() {
		return instance;
	}

	/**
	 * initialize the application
	 * @throws Exception
	 */
	public void init() throws Exception {
		initSMTPproperty();
		initAppConfig();
		initMailAuthenticator();
	}

	/**
	 * @return appConfig object. if null, try to initialize first
	 * @throws Exception
	 */
	public AppConfig getAppConfig() throws Exception {
		if (appConfig == null) {
			initAppConfig();
		}
		return appConfig;
	}

	/**
	 * Initialize the application configuration from mailer-app.properties
	 * 
	 * @throws Exception
	 */
	public void initAppConfig() throws Exception {
		appConfig = new AppConfig();

		if (appConfig != null) {
			Properties configProps = ResourceUtils.loadProperties(customConfig);

			String User = configProps.getProperty(Constants.SMTP_USER);
			String passwd = configProps.getProperty(Constants.SMTP_USER_PASSWD);

			appConfig.setSmtpUserId(User);
			appConfig.setSmtpPasswd(passwd);

			try {
				int awaitTime = Integer.parseInt(configProps
						.getProperty(Constants.THREAD_AWAIT_TIME));
				int dbSlotSize = Integer.parseInt(configProps
						.getProperty(Constants.DB_SLOT_SIZE));
				int maxRetry = Integer.parseInt(configProps
						.getProperty(Constants.DB_RETRY));
				int poolSize = Integer.parseInt(configProps
						.getProperty(Constants.THREAD_POOL_SIZE));
				int commitSize = Integer.parseInt(configProps
						.getProperty(Constants.DB_COMMIT_SIZE));

				appConfig.setAwaitTerminationSeconds(awaitTime);
				appConfig.setDbSlotSize(dbSlotSize);
				appConfig.setMaxRetry(maxRetry);
				appConfig.setThreadPoolSize(poolSize);
				appConfig.setCommitSize(commitSize);
			} catch (NumberFormatException nfe) {
				// if exception occurs default values will be set to appConfig
				System.out
						.println("[Warning] Unable to set config values from property file.");
			}

		}
	}

	/**
	 * @param config
	 *            to set appConfig object
	 * @throws Exception
	 */
	public void initAppConfig(AppConfig config) throws Exception {
		if (config != null)
			appConfig = config;
		else
			initAppConfig();
	}

	/**
	 * create the SMTP property file for entire application
	 */
	private void initSMTPproperty() {
		smtpProps = new Properties();

		smtpProps.put(Constants.SMTP_AUTH, Constants.SMTP_AUTH_VAL);
		smtpProps.put(Constants.SMTP_STARTTLS, Constants.SMTP_STARTTLS_VAL);
		smtpProps.put(Constants.SMTP_HOST, Constants.SMTP_HOST_VAL);
		smtpProps.put(Constants.SMTP_PORT_VAL, Constants.SMTP_PORT_VAL);
	}

	/**
	 * Initialize the Mail Authenticator for entire application
	 * 
	 * @throws Exception
	 */
	private void initMailAuthenticator() throws Exception {
		if(appConfig == null){
			initAppConfig();
		}
		final String userId = Application.getInstance().getAppConfig()
		.getSmtpUserId();
		final String passwd = Application.getInstance().getAppConfig()
		.getSmtpPasswd();

		auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userId, passwd);
			}
		};
	}

	/**
	 * @return the smtp properties file if null, try to initialize
	 */
	public Properties getSMPTproperty() {
		if (smtpProps == null) {
			initSMTPproperty();
		}
		return smtpProps;
	}

	/**
	 * @return the mail authenticator object, if null, try to initialize
	 * @throws Exception
	 */
	public Authenticator getMailAuthenticator() throws Exception {
		if (auth == null) {
			initMailAuthenticator();
		}
		return auth;
	}

	/**
	 * @return the jdbc config property file name.
	 */
	public String getJdbcProperties() {
		return jdbcConfig;
	}

}
