package com.ices.ethereumevent;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class EthereumEventConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EthereumEventConsumerApplication.class, args);
	}

}
