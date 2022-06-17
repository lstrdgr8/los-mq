package com.los.mq.config;

import java.util.HashMap;
import java.util.Locale;

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
import org.springframework.jms.annotation.EnableJms;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.spring.boot.MQConnectionFactoryCustomizer;
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

	@Bean
	public MQConnectionFactoryCustomizer customConnectionFactory() {
		MQConnectionFactoryCustomizer customConnection = new MQConnectionFactoryCustomizer() {
			@Override
			public void customize(MQConnectionFactory factory) {
				try {
					String queueManager = messageSource.getMessage(ConfigConstants.MQ_QUEUE_MANAGER, null,
							Locale.getDefault());
					String channel = messageSource.getMessage(ConfigConstants.MQ_CHANNEL, null, Locale.getDefault());
					String hostName = messageSource.getMessage(ConfigConstants.MQ_CONN_NAME, null, Locale.getDefault());
					String port = messageSource.getMessage(ConfigConstants.MQ_PORT, null, Locale.getDefault());
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
					factory.setQueueManager(queueManager);
					factory.setChannel(channel);
					factory.setHostName(hostName);
					factory.setPort(Integer.parseInt(port));
				} catch (JMSException e) {
				}
			}
		};
		return customConnection;
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
