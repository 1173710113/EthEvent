package com.ices.ethereumevent.util;

import java.util.Iterator;
import java.util.Map;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class ReSendUtil {

	@Autowired
	RabbitMQUtil rabbitMQUtil;
	
	@Autowired
	Redisson redisson;
	
	public static final Integer MAX_TRY_COUNT = 8;
	
	
	@Scheduled(cron = "*/30 * * * * ?")
	public void reSend() {
		log.info("Start Retry");
		RSet<String> set = redisson.getSet("message:delivering");
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			Map<String, Object> map = redisson.getMap("message:body:" + key);
			int tryCount = (int)map.get("tryCount");
			log.info("Retry:" + key + " times:" + tryCount);
			if(tryCount > MAX_TRY_COUNT) {
				set.remove(key);
				RSet<String> errorSet = redisson.getSet("message:error");
				errorSet.add(key);
			} else {
				rabbitMQUtil.send((String)map.get("exchange"), (String)map.get("routingKey"), (String)map.get("object"), key);
			}
		}
	}
	
	@Scheduled(cron = "0 */1 * * * ?")
	public void recordError() {
		log.info("Record Error");
		RSet<String> set = redisson.getSet("message:error");
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			set.remove(key);
			RMap<Object, Object> map = redisson.getMap("message:body:" + key);
			map.delete();
		}
	}
}
