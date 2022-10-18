package com.ices.ethereumevent.util;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ices.ethereumevent.domain.MessagePO;
import com.ices.ethereumevent.mapper.MessageMapper;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class EventUtil {

	@Autowired
	Redisson redisson;
	
	@Autowired
	MessageMapper messageMapper;
	
	private static final String MESSAGE_CONSUME_PREFIX = "message:consumed:";
	
	private static final String MESSAGE_CONSUME_LOCK_PREFIX = "message:consume:lock:";
	
	private static final String MESSAGE_CHECK_CONSUME_DB_LOCK_PREFIX = "message:consume:check:db:lock:";
	
	
	public boolean checkConsumedInCache(String correlationId) {
		RBucket<String> messageInRedis = redisson.getBucket(MESSAGE_CONSUME_PREFIX + correlationId);
		if (messageInRedis.get() != null) {
			log.error(correlationId + " already consumed");
			return true;
		}
		return false;
	}
	
	public boolean checkConsumedInDB(String correlationId) {
		if(checkConsumedInCache(correlationId)) return true;
		RLock lock = redisson.getLock(MESSAGE_CHECK_CONSUME_DB_LOCK_PREFIX + correlationId);
		lock.lock();
		try {
			if(checkConsumedInCache(correlationId)) return true;
			MessagePO messagePO = messageMapper.selectByCorrelationId(correlationId);
			if(messagePO == null) return false;
			setConsumedInCache(correlationId, messagePO.getPayload());
			
		} finally {
			lock.unlock();
		}
		return true;
	}
	
	public void setConsumedInCache(String correlationId, String payload) {
		RBucket<String> messageInRedis = redisson.getBucket(MESSAGE_CONSUME_PREFIX + correlationId);
		messageInRedis.set(payload);
	
		messageInRedis.expire(TimeUtil.randomDurationNormal());
		
	}

	
	public RLock getConsumeLock(String correlationId) {
		return redisson.getLock(MESSAGE_CONSUME_LOCK_PREFIX + correlationId);
	}
	
	
}
