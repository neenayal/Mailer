package com.coupondunia.mailer.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.coupondunia.mailer.base.Application;

/**
 * MailService Class send mails by managing the MailRunner thread pool.
 * 
 * @author neeraj
 * neeraj.nayal.2008@gmail.com
 */
public class MailService {

	/**
	 * Executor Service - to manage the thread pool
	 */
	private ExecutorService executorService;

	/**
	 * threadPoolSize - size of thread pool
	 */
	private int threadPoolSize;

	/**
	 * List of future object return from thread's execution
	 */
	private final List<Future<?>> futureList = new ArrayList<Future<?>>();

	/**
	 * Initialize the thread pool Thread pool size is store in Application class
	 * to change the pool size make change in mailer-app.properties property
	 * file
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		threadPoolSize = Application.getInstance().getAppConfig()
				.getThreadPoolSize();
		System.out.println("Initializing Mail Service Using " + threadPoolSize
				+ " thread...");
		executorService = Executors.newFixedThreadPool(threadPoolSize,
				Executors.defaultThreadFactory());
	}

	/**
	 * Initialize the mail service create and start mail runner Thread and
	 * shutdown the service
	 * 
	 * @throws Exception
	 */
	public void service() throws Exception {

		init();
		System.out.println("[Wait] Starting Mail Service ......");

		// Spawn all threads
		for (int idx = 0; idx < threadPoolSize; idx++) {
			MailRunner mailRunner = new MailRunner();
			futureList.add(executorService.submit(mailRunner));
		}

		// block until all threads'stask has finished correctly.
		for (int idx = 0; idx < futureList.size(); idx++) {
			try {
				futureList.get(idx).get();
			} catch (InterruptedException e) {
				System.out
						.println("[Error] thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted.");
			} catch (ExecutionException e) {
				System.out
						.println("[Error] attempting to retrieve the result of a task that aborted by throwing an exception");
			}
		}
		shutDownService();
	}

	/**
	 * Once all the thread complete execution shutdown the service. first stop
	 * taking new thread request, wait for 60 sec. than stop the service.
	 * 
	 * @throws Exception
	 */
	public void shutDownService() throws Exception {
		try {
			System.out.println("Mail Service shutdown initiated.");
			int awaitTime = Application.getInstance().getAppConfig()
					.getAwaitTerminationSeconds();
			executorService.shutdown();
			if (!executorService.awaitTermination(awaitTime, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
				if (!executorService.awaitTermination(awaitTime,
						TimeUnit.SECONDS)) {
					System.out.println("Executor Service did't terminate.");
				}
			}
			System.out.println("Executor Service shutdown complete.");
		} catch (InterruptedException ie) {
			executorService.shutdownNow();
		}
	}
}
