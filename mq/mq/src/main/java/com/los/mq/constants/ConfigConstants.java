package com.los.mq.constants;

import java.io.File;

public class ConfigConstants {

	private ConfigConstants() {
		throw new IllegalStateException(ConfigConstants.class.getName());
	}

	public static final String BASE_PACKAGE = "com.los.mq";

	public static final String MQ_QUEUE_MANAGER = "ibm.mq.queueManager";

	public static final String MQ_CONN_NAME = "ibm.mq.connName";

	public static final String MQ_CHANNEL = "ibm.mq.channel";
	
	public static final String MQ_PORT = "ibm.mq.port";

	public static final String MQ_USERNAME = "ibm.mq.user";

	public static final String MQ_PASSWORD = "ibm.mq.password";
	
	public static final String MQ_RECEIVE_TIMEOUT = "ibm.mq.receiveTimeout";
	
	public static final String MQ_CTOS = "queue.ctos";
	
	public static final String MQ_CRIS = "queue.cris";
	
	public static final String PATH_CATALINA_HOME = "";

	public static final String PATH_CATALINA_BASE = "";

	public static final String PATH_PROJ_CONFIG = "los.config.path";

	public static final String PROPERTY_FILENAME = "los-mq";
	
	public static final String FILE_PFX = "file:";
	
	public static final String PROPERTIES_EXT = ".properties";

	public static final String FILE_SYS_RESOURCE = File.separator + PROPERTY_FILENAME + ConfigConstants.PROPERTIES_EXT;
}
