package com.los.mq.controller;

import java.util.HashMap;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.los.mq.config.CorrelationIdPostProcessor;
import com.los.mq.constants.Constants;
import com.los.mq.constants.MessageConstants;
import com.los.mq.constants.UriConstants;
import com.los.mq.dto.MqConfigurationDto;
import com.los.mq.dto.MqGenericReqDto;
import com.los.mq.dto.MqGenericRespDto;
import com.los.mq.utilities.AppUtility;
import com.los.mq.utilities.Utility;

@RestController
@RequestMapping(UriConstants.BASE_URI)
public class MQSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(MQSender.class);

	@Autowired
	private JmsTemplate jms;

	@Autowired
	@Qualifier("mqConfiguration")
	private MqConfigurationDto mqConfiguration;

	@Autowired
	private MqGenericRespDto response;

	@Autowired
	@Qualifier("queueConfig")
	private HashMap<String, String> queueConfig;

	@PostMapping(value = UriConstants.SEND)
	public @ResponseBody MqGenericRespDto sendToQueue(@RequestBody MqGenericReqDto msg) {
		String correlationId = UUID.randomUUID().toString();
		response.setTimestamp(AppUtility.getCurrentTimeStamp());
		response.setCorrelationId(correlationId);
		LOGGER.info("Incoming Message: " + msg.getMessage());
		try {
			String receiveQueue = queueConfig.get(msg.getSendQueue());
			if (!Utility.isObjNull(receiveQueue)) {
				jms.setReceiveTimeout(mqConfiguration.getMqReceiveTimeout());
				jms.convertAndSend(msg.getSendQueue(), msg.getMessage(), new CorrelationIdPostProcessor(correlationId));
				String respMsg = (String) jms.receiveSelectedAndConvert(receiveQueue,
						"JMSCorrelationID='" + correlationId + "'");
				response.setResponse(respMsg);
				response.setStatus(!Utility.isObjNull(respMsg) ? Constants.RESP_SUCCESS : Constants.RESP_FAILED);
			} else {
				response.setResponse(MessageConstants.QUEUE_NOT_FOUND);
				response.setStatus(Constants.RESP_FAILED);
			}

		} catch (Exception e) {
			response.setResponse(e.getMessage());
			response.setStatus(Constants.RESP_FAILED);
			e.printStackTrace();
		}

		return response;
	}
}
