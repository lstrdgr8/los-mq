package com.los.mq.utilities;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.los.mq.constants.ConfigConstants;

public class ConfigUtility {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtility.class);

	private ConfigUtility() {

	}

	/**
	 * Gets the property path.
	 *
	 * @param pathProjConfig  the path proj config
	 * @param fileSysResource the file sys resource
	 * @param propFilename    the prop filename
	 * @return the property path
	 */
	public static String getPropertyPath(String pathProjConfig, String fileSysResource, String propFilename) {
		String propertyPath = null;
		String propertyHome = System.getProperty(pathProjConfig) != null ? System.getProperty(pathProjConfig)
				: System.getenv(pathProjConfig);
		File file = new File(propertyHome + fileSysResource);
		if (!file.exists()) {
			propertyHome = null;
		}

		// Get from TOMCAT server
		if (Utility.isObjNull(propertyHome)) {
			propertyHome = getPropertiesFromTomcat(fileSysResource);
		}

		if (!Utility.isObjNull(propertyHome)) {
			file = new File(propertyHome + fileSysResource);
			if (file.exists() && !file.isDirectory()) {
				propertyPath = propertyHome + File.separator + propFilename;
			} else {
				LOGGER.error("Missing properties file >> {}{}", propertyHome, fileSysResource);
			}
		}

		String propClasspath = "classpath:" + propFilename;

		// Get from Application CLASSPATH
		propertyPath = propertyPath != null ? propertyPath : propClasspath;

		LOGGER.info("Application Properties Path: {}", propertyPath);

		return propertyPath;

	}

	/**
	 * Gets the properties from tomcat.
	 *
	 * @param fileSysResource the file sys resource
	 * @return the properties from tomcat
	 */
	private static String getPropertiesFromTomcat(String fileSysResource) {
		String propertyHome = System.getProperty(ConfigConstants.PATH_CATALINA_HOME) != null
				? System.getProperty(ConfigConstants.PATH_CATALINA_HOME)
				: System.getProperty(ConfigConstants.PATH_CATALINA_BASE);
		if (!Utility.isObjNull(propertyHome)) {
			propertyHome = propertyHome + File.separator + "conf";
		}

		File file = new File(propertyHome + fileSysResource);
		if (!file.exists()) {
			propertyHome = null;
		}

		return propertyHome;
	}
}
