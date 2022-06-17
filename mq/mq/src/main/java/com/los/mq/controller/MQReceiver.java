package com.los.mq.controller;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MQReceiver {
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@JmsListener(destination = "CTOS_SND")
	public void receiveCtosMsg(Message message) throws JMSException {
		System.out.println("Received CTOS <" + message + ">");
		jmsTemplate.convertAndSend("CTOS_RCV", message);
	}

	@JmsListener(destination = "CRIS_SND")
	public void receiveCrisMsg(Message message) {
		System.out.println("Received CCRIS <" + message + ">");
		jmsTemplate.convertAndSend("CRIS_RCV", message);
	}
}
