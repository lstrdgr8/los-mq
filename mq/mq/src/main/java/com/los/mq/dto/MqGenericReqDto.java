package com.los.mq.dto;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
public class MqGenericReqDto {

	private Object message;
	
	private String sendQueue;
}
