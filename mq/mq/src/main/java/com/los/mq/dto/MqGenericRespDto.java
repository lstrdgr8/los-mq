package com.los.mq.dto;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
public class MqGenericRespDto {

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp timestamp;
	
	private String status;
	
	private String correlationId;
	
	private Object response;
}
