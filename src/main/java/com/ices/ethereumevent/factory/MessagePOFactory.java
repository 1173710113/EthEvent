package com.ices.ethereumevent.factory;

import java.util.Date;

import com.ices.ethereumevent.domain.MessagePO;

public class MessagePOFactory {

	public static MessagePO successMessage(String correlationId, String payload) {
		MessagePO messagePO = MessagePO.builder().correlationId(correlationId).payload(payload)
				.completeTime(new Date()).state(MessagePO.STATE_COMPLETE).build();
		return messagePO;
	}
}
