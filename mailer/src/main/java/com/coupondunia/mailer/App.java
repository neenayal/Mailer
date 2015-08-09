package com.coupondunia.mailer;

import com.coupondunia.mailer.base.MailerConsoleApp;

/**
 * App class is driver class to run Mailer Console Application.
 * @author neeraj 
 * neeraj.nayal.2008@gmail.com
 */
public class App {

	/**
	 * driver class to run Mailer Console Application.
	 * 
	 * @param args
	 *            - Parameter list for Mail Console application. Current
	 *            Version/Implementation do not take parameter from console.
	 */
	public static void main(String[] args) {
		long startDate = System.currentTimeMillis();
		try {

			System.out.println("\n-----------------------------------------");
			System.out.println("  Running Mail Console Application");
			System.out.println("-----------------------------------------");

			System.out.println("Starting Mail Console Application ...");

			if (args.length > 0) {
				// if user give parameter from console
				System.out
						.println("[Error] Current Version do not take parameter from console");
			} else {
				// if no parameter from console, start the Mail Console App
				MailerConsoleApp app = new MailerConsoleApp();
				System.out.println("Launching Mail Console Application...");
				app.launch();
			}

			System.out.println("Stopping Mail Console Application.");
		} catch (Exception e) {
			System.out
					.println("[Error] While Running Mail Console Application.");
		} finally {

			long endDate = System.currentTimeMillis();
			System.out.println("Time take to complete is "
					+ (endDate - startDate) + " milliseconds.");
			System.out.println("-----------------------------------------\n");
		}

	}

}
