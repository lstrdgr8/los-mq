package com.los.mq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.los.mq.constants.ConfigConstants;

@Configuration
public class ServiceConfig implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfig.class);

	@Value("${" + ConfigConstants.MQ_QUEUE_MANAGER + "}")
	private String mqQueueManager;

	@Value("${" + ConfigConstants.MQ_CHANNEL + "}")
	private String mqChannel;

	@Value("${" + ConfigConstants.MQ_CONN_NAME + "}")
	private String mqConnName;

	@Value("${" + ConfigConstants.MQ_PORT + "}")
	private String mqPort;

	@Value("${" + ConfigConstants.MQ_USERNAME + "}")
	private String mqUserName;

	@Value("${" + ConfigConstants.MQ_PASSWORD + "}")
	private String mqPassword;

	@Value("${" + ConfigConstants.MQ_RECEIVE_TIMEOUT + "}")
	private String mqReceiveTimeout;

	@Value("${" + ConfigConstants.MQ_CTOS + "}")
	private String ctosQueue;

	@Value("${" + ConfigConstants.MQ_CRIS + "}")
	private String crisQueue;

	@Override
	public void afterPropertiesSet() throws Exception {
		LOGGER.info("Loading of Service Configuration Start ...");
	}
}
