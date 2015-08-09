package com.coupondunia.mailer.concurrent;

/**
 * AbstractServiceRunner implements Runnable and provide base to
 * all the thread type classes.subclass only need to override some
 * methods to implement its own behaviour.
 * @author neeraj 
 * neeraj.nayal.2008@gmail.com
 */
public abstract class AbstractServiceRunner implements Runnable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		if (Thread.currentThread().isInterrupted()) {
			// The thread has been interrupted
			return;
		}

		try {
			serviceInternal();
		} catch (Exception ex) {
			System.out.println("[Error] While executing thread "
					+ Thread.currentThread().getName());
		}
	}

	/**
	 * ServiceInternal method perform all the task of thread
	 * 
	 * @throws Exception
	 */
	private void serviceInternal() throws Exception {
		preServiceHook();

		try {
			init();
			acquireResources();
			while (continueService()) {
				task();
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			releaseResource();
		}

		postServiceHook();
	}

	/**
	 * Initialize the Thread (other than acquiring resources).
	 * 
	 * @throws Exception
	 */
	protected void init() throws Exception {
		// default behaviour is empty/nothing.
	}

	/**
	 * continueService() method determine whether task need to continue the
	 * service or stop.
	 * 
	 * @return if task need to continue, it return true. otherwise false.
	 * @throws Exception
	 */
	protected abstract boolean continueService() throws Exception;

	/**
	 * acquireResources() method acquire all the needed resource before starting
	 * the actual task
	 * 
	 * @throws Exception
	 */
	protected abstract void acquireResources() throws Exception;

	/**
	 * releaseResource() method release all the resource used by the thread.
	 * 
	 * @throws Exception
	 */
	protected abstract void releaseResource() throws Exception;

	/**
	 * task() method is the actual task done by the thread. Subclass need to
	 * override task() method. behaviour of task() depend on functionality of
	 * subclass
	 * 
	 * @throws Exception
	 */
	protected abstract void task() throws Exception;

	/**
	 * pre hook for Service()
	 */
	protected void preServiceHook() {
		// default behaviour is empty/nothing.
	}

	/**
	 * post hook for Service()
	 */
	protected void postServiceHook() {
		// default behaviour is empty/nothing.
	}

}
