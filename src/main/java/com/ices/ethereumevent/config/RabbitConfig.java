package com.ices.ethereumevent.config;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Configuration
public class RabbitConfig {
	
	@Autowired
	Redisson redisson;

	@Bean
	public Queue singleTransferQueue() {
		return new Queue("Votes.SingleTransfer", true);
	}
	
	@Bean
	public Queue crowdfundCreatedQueue() {
		return new Queue("Crowdfund.CrowdfundCreated", true);
	}
	
	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange("directExchange", true, false);
	}
	
	@Bean
	public Binding bind() {
		return BindingBuilder.bind(singleTransferQueue()).to(directExchange()).with("singleTransfer");
	}
	
	@Bean
	public Binding bindCrowdfundCreated() {
		return BindingBuilder.bind(crowdfundCreatedQueue()).to(directExchange()).with("crowdfundCreatedEvent");
	}
	
	@Bean
	public RabbitTemplate createRAbbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
			
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if(ack) {
					RSet<String> set = redisson.getSet("message:delivering");
					set.remove(correlationData.getId());
					RMap<Object, Object> map = redisson.getMap("message:body:" + correlationData.getId());
					map.delete();
					log.info("Success deliver:" + correlationData.getId());
				} else {
					log.error(correlationData.getId() + ":Fail\n" + cause);
				}				
			}
		});
		
		return rabbitTemplate;
	}
}
