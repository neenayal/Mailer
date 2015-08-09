package com.coupondunia.mailer.base;

/**
 * ConsoleApp class is base class for all console Application. In
 * current version, Mail Console Application use it as base class.
 * @author neeraj 
 * neeraj.nayal.2008@gmail.com
 */
public abstract class ConsoleApp {

	/**
	 * Starting point of Console Application.
	 * @throws Exception
	 */
	public void launch() throws Exception {
		try {
			init();
			preLaunchHook();
			execute();
			postLaunchHook();
		} catch (Exception e) {
			System.out.println("[Error] While launching Mail console Application");
			throw e;
		}

	}

	/** 
	 * init() : Initialize the Console Application
	 * @throws Exception
	 */
	protected void init() throws Exception {
		// default behaviour is empty.
	}

	/**
	 * preLaunchHook() : pre Hook for execute() function
	 * @throws Exception
	 */
	protected void preLaunchHook() throws Exception {
		// default behaviour is empty.
	}

	/**
	 * postLaunchHook() : post Hook for execute() function
	 * @throws Exception
	 */
	protected void postLaunchHook() throws Exception {
		// default behaviour is empty.
	}

	/**
	 * execute function should be override by subclass
	 * and Implementation depend on subclass's behavior.
	 * @throws Exception
	 */
	abstract protected void execute() throws Exception;
}
