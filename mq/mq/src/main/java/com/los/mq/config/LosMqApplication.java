package com.los.mq.config;

import java.util.HashMap;
import java.util.Locale;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.ibm.mq.headers.CMQC;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.spring.boot.MQConnectionFactoryCustomizer;
import com.ibm.msg.client.wmq.WMQConstants;
import com.los.mq.constants.ConfigConstants;
import com.los.mq.constants.Constants;
import com.los.mq.dto.MqConfigurationDto;

@SpringBootApplication
@ComponentScan(basePackages = { ConfigConstants.BASE_PACKAGE + ".*" })
@EnableJms
public class LosMqApplication extends SpringBootServletInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(LosMqApplication.class);

	@Autowired
	MessageSource messageSource;

	public static void main(String[] args) {
		SpringApplication.run(LosMqApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		LOGGER.info("LOS MQ Spring Boot started...");
		application.bannerMode(Banner.Mode.OFF);
		return application.sources(LosMqApplication.class);
	}

	/*
	 * @Bean public MQConnectionFactoryCustomizer customConnectionFactory() {
	 * MQConnectionFactoryCustomizer customConnection = new
	 * MQConnectionFactoryCustomizer() {
	 * 
	 * @Override public void customize(MQConnectionFactory factory) { try { String
	 * queueManager = messageSource.getMessage(ConfigConstants.MQ_QUEUE_MANAGER,
	 * null, Locale.getDefault()); String channel =
	 * messageSource.getMessage(ConfigConstants.MQ_CHANNEL, null,
	 * Locale.getDefault()); String hostName =
	 * messageSource.getMessage(ConfigConstants.MQ_CONN_NAME, null,
	 * Locale.getDefault()); String port =
	 * messageSource.getMessage(ConfigConstants.MQ_PORT, null, Locale.getDefault());
	 * String user = messageSource.getMessage(ConfigConstants.MQ_USERNAME, null,
	 * Locale.getDefault()); String pass =
	 * messageSource.getMessage(ConfigConstants.MQ_PASSWORD, null,
	 * Locale.getDefault()); StringBuilder sb = new StringBuilder();
	 * sb.append(Constants.NEW_LINE + Constants.NEW_LINE);
	 * sb.append(Constants.LOG_SEPARATOR + Constants.NEW_LINE);
	 * sb.append("MQ Properties"); sb.append(Constants.NEW_LINE +
	 * Constants.LOG_SEPARATOR + Constants.NEW_LINE); sb.append("Queue Manager: " +
	 * queueManager + Constants.NEW_LINE); sb.append("Channel Name:  " + channel +
	 * Constants.NEW_LINE); sb.append("Host Name:  " + hostName +
	 * Constants.NEW_LINE); sb.append("Port Number:  " + port + Constants.NEW_LINE);
	 * sb.append(Constants.NEW_LINE + Constants.LOG_SEPARATOR + Constants.NEW_LINE);
	 * LOGGER.info("{}", sb); factory.setQueueManager(queueManager);
	 * factory.setChannel(channel); factory.setHostName(hostName);
	 * factory.setPort(Integer.parseInt(port));
	 * factory.setTransportType(WMQConstants.WMQ_CM_CLIENT); } catch (JMSException
	 * e) { } } }; return customConnection; }
	 */

	@Bean(name = "queueConnectionFactory")
	public MQQueueConnectionFactory mqQueueConnectionFactory() {
		MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();

		try {
			String queueManager = messageSource.getMessage(ConfigConstants.MQ_QUEUE_MANAGER, null, Locale.getDefault());
			String channel = messageSource.getMessage(ConfigConstants.MQ_CHANNEL, null, Locale.getDefault());
			String hostName = messageSource.getMessage(ConfigConstants.MQ_CONN_NAME, null, Locale.getDefault());
			String port = messageSource.getMessage(ConfigConstants.MQ_PORT, null, Locale.getDefault());
			String user = messageSource.getMessage(ConfigConstants.MQ_USERNAME, null, Locale.getDefault());
			String pass = messageSource.getMessage(ConfigConstants.MQ_PASSWORD, null, Locale.getDefault());
			StringBuilder sb = new StringBuilder();
			sb.append(Constants.NEW_LINE + Constants.NEW_LINE);
			sb.append(Constants.LOG_SEPARATOR + Constants.NEW_LINE);
			sb.append("MQ Properties");
			sb.append(Constants.NEW_LINE + Constants.LOG_SEPARATOR + Constants.NEW_LINE);
			sb.append("Queue Manager: " + queueManager + Constants.NEW_LINE);
			sb.append("Channel Name:  " + channel + Constants.NEW_LINE);
			sb.append("Host Name:  " + hostName + Constants.NEW_LINE);
			sb.append("Port Number:  " + port + Constants.NEW_LINE);
			sb.append(Constants.NEW_LINE + Constants.LOG_SEPARATOR + Constants.NEW_LINE);
			LOGGER.info("{}", sb);
			mqQueueConnectionFactory.setHostName(hostName);
			mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
			mqQueueConnectionFactory.setChannel(channel);
			mqQueueConnectionFactory.setPort(Integer.parseInt(port));
			mqQueueConnectionFactory.setQueueManager(queueManager);
			mqQueueConnectionFactory.setCCSID(819);
			mqQueueConnectionFactory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
			mqQueueConnectionFactory.setStringProperty(WMQConstants.USERID, user);
			mqQueueConnectionFactory.setStringProperty(WMQConstants.PASSWORD, pass);
		} catch (JMSException e) {
			logger.error("Error occured: " + e);
		}

		return mqQueueConnectionFactory;
	}

	@Primary
	@Bean(name = "userCredentialsConnectionFactoryAdapter")
	@DependsOn(value = { "queueConnectionFactory" })
	UserCredentialsConnectionFactoryAdapter getUserCredentialsConnectionFactoryAdapter(
			MQQueueConnectionFactory mqQueueConnectionFactory) {
		UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
		String user = messageSource.getMessage(ConfigConstants.MQ_USERNAME, null, Locale.getDefault());
		String pass = messageSource.getMessage(ConfigConstants.MQ_PASSWORD, null, Locale.getDefault());
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.NEW_LINE + Constants.NEW_LINE);
		sb.append(Constants.LOG_SEPARATOR + Constants.NEW_LINE);
		sb.append("userCredentialsConnectionFactoryAdapter");
		sb.append(Constants.NEW_LINE + Constants.LOG_SEPARATOR + Constants.NEW_LINE);
		sb.append("user: " + user + Constants.NEW_LINE);
		sb.append("pass:  " + pass + Constants.NEW_LINE);
		sb.append(Constants.NEW_LINE + Constants.LOG_SEPARATOR + Constants.NEW_LINE);
		LOGGER.info("{}", sb);
		userCredentialsConnectionFactoryAdapter.setUsername(user);
		userCredentialsConnectionFactoryAdapter.setPassword(pass);
		userCredentialsConnectionFactoryAdapter.setTargetConnectionFactory(mqQueueConnectionFactory);
		return userCredentialsConnectionFactoryAdapter;
	}

	@Bean
	public JmsOperations jmsOperations(ConnectionFactory userCredentialsConnectionFactoryAdapter) {
		JmsTemplate jmsTemplate = new JmsTemplate(userCredentialsConnectionFactoryAdapter);
		String receiveTimeout = messageSource.getMessage(ConfigConstants.MQ_RECEIVE_TIMEOUT, null, Locale.getDefault());
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.NEW_LINE + Constants.NEW_LINE);
		sb.append(Constants.LOG_SEPARATOR + Constants.NEW_LINE);
		sb.append("jms operation");
		sb.append(Constants.NEW_LINE + Constants.LOG_SEPARATOR + Constants.NEW_LINE);
		sb.append("receiveTimeout: " + receiveTimeout + Constants.NEW_LINE);
		sb.append(Constants.NEW_LINE + Constants.LOG_SEPARATOR + Constants.NEW_LINE);
		LOGGER.info("{}", sb);
		jmsTemplate.setReceiveTimeout(Integer.parseInt(receiveTimeout));
		return jmsTemplate;
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate template = new JmsTemplate();
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.NEW_LINE + Constants.NEW_LINE);
		sb.append(Constants.LOG_SEPARATOR + Constants.NEW_LINE);
		sb.append("jms template");
		template.setConnectionFactory(mqQueueConnectionFactory());
		template.setPubSubDomain(false);
		return template;
	}
	
	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
	    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.NEW_LINE + Constants.NEW_LINE);
		sb.append(Constants.LOG_SEPARATOR + Constants.NEW_LINE);
		sb.append("jacksonJmsMessageConverter");
	    converter.setTargetType(MessageType.TEXT);
	    converter.setTypeIdPropertyName("_type");
	    return converter;
	}

	@Bean
	@Qualifier("mqConfiguration")
	public MqConfigurationDto setMQConfig() {
		MqConfigurationDto dto = new MqConfigurationDto();
		String receiveTimeout = messageSource.getMessage(ConfigConstants.MQ_RECEIVE_TIMEOUT, null, Locale.getDefault());
		dto.setMqReceiveTimeout(Integer.parseInt(receiveTimeout));
		return dto;
	}

	// TODO Ugly implementation. Need to implement dynamic generation of queue based
	// on property file without too much code changes
	@Bean
	@Qualifier("queueConfig")
	public HashMap<String, String> setQueueConfig() {
		HashMap<String, String> queue = new HashMap<String, String>();
		String ctosQueue = messageSource.getMessage(ConfigConstants.MQ_CTOS, null, Locale.getDefault());
		String crisQueue = messageSource.getMessage(ConfigConstants.MQ_CRIS, null, Locale.getDefault());
		queue.put(ctosQueue.split(":")[0], ctosQueue.split(":")[1]);
		queue.put(crisQueue.split(":")[0], crisQueue.split(":")[1]);
		return queue;
	}

}
