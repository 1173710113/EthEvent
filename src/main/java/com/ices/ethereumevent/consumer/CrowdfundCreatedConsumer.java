package com.ices.ethereumevent.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.ices.ethereumevent.domain.BaseEventResponseBody;
import com.ices.ethereumevent.domain.CrowdfundCreatedEventReturnValues;

@Component
public class CrowdfundCreatedConsumer implements IEthEventConsumer {

	@Override
	@RabbitListener(queues = { "Crowdfund.CrowdfundCreated" })
	@Transactional
	public void consume(Message<String> message) {
		BaseEventResponseBody<CrowdfundCreatedEventReturnValues> response = JSON.parseObject(message.getPayload(),
				new TypeReference<BaseEventResponseBody<CrowdfundCreatedEventReturnValues>>(
						CrowdfundCreatedEventReturnValues.class) {
				});
		

	}
}
