package com.ices.ethereumevent.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ices.ethereumevent.domain.MessagePO;

@Mapper
public interface MessageMapper {

	public void insert(MessagePO messagePO);
	
	public MessagePO selectByCorrelationId(String correlationId);
	
	public void updateByCorrelationId(String correlationId);
}
