package com.ices.ethereumevent.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissionConfig {

	
	@Bean
	public Redisson redission() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://101.43.244.240:6379").setDatabase(0).setPassword("112358");
		return (Redisson)Redisson.create(config);
	}
}
