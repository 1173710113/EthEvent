package com.ices.ethereumevent.util;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class RabbitMQUtil {

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	Redisson redisson;
	
	
	public void send(String exchange, String routingKey, String object, String corralateDataId) {
		RSet<String> set = redisson.getSet("message:delivering");
		RMap<Object, Object> map = redisson.getMap("message:body:" + corralateDataId);
		map.put("exchange", exchange);
		map.put("routingKey", routingKey);
		map.put("object", object);
		int tryCount = (Integer)map.getOrDefault("tryCount", 0) + 1;
		map.put("tryCount", tryCount);
		set.add(corralateDataId);
		log.info(object);
		rabbitTemplate.convertAndSend(exchange, routingKey, object, new CorrelationData(corralateDataId));
	}
}
