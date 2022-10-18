package com.ices.ethereumevent.consumer;

import org.springframework.messaging.Message;

public interface IEthEventConsumer {

	public void consume(Message<String> message);
}
