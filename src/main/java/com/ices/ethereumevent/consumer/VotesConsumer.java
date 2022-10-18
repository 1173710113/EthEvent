package com.ices.ethereumevent.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.ices.ethereumevent.domain.BaseEventResponseBody;
import com.ices.ethereumevent.domain.TransferSingleEventReturnValues;
import com.ices.ethereumevent.service.impl.DACService;

@Component
public class VotesConsumer implements IEthEventConsumer {

	@Autowired
	DACService dacService;

	@Override
	@Transactional
	@RabbitListener(queues = { "Votes.SingleTransfer" })
	public void consume(Message<String> message) {
		BaseEventResponseBody<TransferSingleEventReturnValues> response = JSON.parseObject(message.getPayload(),
				new TypeReference<BaseEventResponseBody<TransferSingleEventReturnValues>>(
						TransferSingleEventReturnValues.class) {
				});
		dacService.DAC(response.getAddress());

	}

}
