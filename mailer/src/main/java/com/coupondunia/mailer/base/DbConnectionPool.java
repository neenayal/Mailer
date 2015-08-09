package com.coupondunia.mailer.base;

import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

import com.coupondunia.mailer.constants.Constants;
import com.coupondunia.mailer.utils.ResourceUtils;

/**
 * DbConnectionPool class is singleton implementation and create
 * new connection. if data source is null, it will be initialize first
 * DbConnectionPool can't be inherited.
 * @author neeraj 
 * neeraj.nayal.2008@gmail.com
 */
public final class DbConnectionPool {

	/**
	 * Data source for connection pool
	 */
	private volatile BasicDataSource ds = null;

	/**
	 * the only object of DbConnectionPool class.
	 */
	private static DbConnectionPool pool = new DbConnectionPool();

	/**
	 * default private constructor - No outer class can create the object of
	 * DbConnectionPool object.
	 */
	private DbConnectionPool() {
	}

	/**
	 * static method to get the only object of DbConnectionPool class.
	 * 
	 * @return return the object of DbConnectionPool
	 */
	public static DbConnectionPool getInstance() {
		return pool;
	}

	/**
	 * Initialize the data source. Database setup values are fetched from
	 * mailer-jdbc.properties file.
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		Properties props = ResourceUtils.loadProperties(Application
				.getInstance().getJdbcProperties());
		ds = new BasicDataSource();
		ds.setDriverClassName(props.getProperty(Constants.DRIVER_CLASS));
		ds.setUrl(props.getProperty(Constants.DB_URL));
		ds.setUsername(props.getProperty(Constants.DB_USER));
		ds.setPassword(props.getProperty(Constants.DB_PWD));
	}

	/**
	 * getConnection create a new connection. if data source is null, it will be
	 * initialize first. only one thread at a time can initialize the
	 * data source.
	 * 
	 * @return new connection available from the pool
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {
		return getConnection(true);
	}

	public Connection getConnection(boolean autoCommit) throws Exception {
		Connection con = null;
		try {
			if (ds == null) {
				synchronized (DbConnectionPool.class) {
					if (ds == null) {
						init();
					}
				}
			}
			con = ds.getConnection();
			con.setAutoCommit(autoCommit);
		} catch (Exception e) {
			System.out.println("[Error] Enable to get Database connection.");
			throw e;
		}
		return con;
	}

}
