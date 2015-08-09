package com.coupondunia.mailer.utils;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

/**
 * ResourceUtils provide Apis for resource loading related task.
 * @author neeraj 
 * neeraj.nayal.2008@gmail.com
 */
public class ResourceUtils {

	/**
	 * default private constructor - no outer class can create object of
	 * ResourceUtils.
	 */
	private ResourceUtils() {
	}

	/**
	 * load the given properties file
	 * 
	 * @param file
	 *            - filename to load
	 * @return properties file, if file is blank or null will return null
	 * @throws Exception 
	 */
	public static Properties loadProperties(String file) throws Exception {

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		Properties props = null;

		try {
			if (StringUtils.isNotBlank(file)) {
				props = new Properties();
				InputStream FileInputStream = classLoader.getResourceAsStream(file);
				props.load(FileInputStream);
			}
		} catch (Exception e) {
			System.out.println("[Error] while loading "+file+" file.");
			throw e;
		}
		return props;
	}

}
