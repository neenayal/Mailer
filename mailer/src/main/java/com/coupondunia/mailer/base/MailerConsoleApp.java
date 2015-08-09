package com.coupondunia.mailer.base;

import com.coupondunia.mailer.concurrent.MailService;

/**
 * MailerConsoleApp class - Initialize the Console Mail
 * Application - start the mail service to send email extends ConsoleApp
 * @author neeraj 
 * neeraj.nayal.2008@gmail.com
 */
public class MailerConsoleApp extends ConsoleApp {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coupondunia.mailer.base.ConsoleApp#init() Initialize the Mail
	 * Console Application
	 */
	public void init() throws Exception {
		try {
			// initialize Application set up
			Application.getInstance().init();
			System.out.println("Using Thread pool Size ["
					+ Application.getInstance().getAppConfig()
							.getThreadPoolSize() + "]");
			System.out.println("Using Database Commit Size ["
					+ Application.getInstance().getAppConfig()
							.getCommitSize() + "]");
			System.out.println("Using Databse Slot Size ["
					+ Application.getInstance().getAppConfig()
							.getDbSlotSize() + "]");
			System.out.println("Using Max Retry for Fail Record ["
					+ Application.getInstance().getAppConfig()
							.getMaxRetry() + "]");
			
		} catch (Exception e) {
			System.out
					.println("[Error] While Initializing Application configuration");
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coupondunia.mailer.base.ConsoleApp#execute()
	 */
	public void execute() throws Exception {
		try {
			// start the mail service
			MailService mailService = new MailService();
			mailService.service();

		} catch (Exception e) {
			System.out.println("[Error] While Executing mail service.");
			throw e;
		}
	}

}
