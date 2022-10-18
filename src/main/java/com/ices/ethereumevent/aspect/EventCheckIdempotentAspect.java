package com.ices.ethereumevent.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ices.ethereumevent.domain.MessagePO;
import com.ices.ethereumevent.factory.MessagePOFactory;
import com.ices.ethereumevent.mapper.MessageMapper;
import com.ices.ethereumevent.util.EventUtil;

@Aspect
@Component
public class EventCheckIdempotentAspect {

	@Autowired
	private EventUtil eventUtil;

	@Autowired
	private MessageMapper messageMapper;

	@Pointcut("execution(public void com.ices.ethereumevent.consumer.IEthEventConsumer.consume(org.springframework.messaging.Message<String>))")
	public void pointcut() {

	}

	@Transactional
	@Around("pointcut()")
	public void around(ProceedingJoinPoint joinPoint) throws Throwable {
		@SuppressWarnings("unchecked")
		Message<String> message = (Message<String>) joinPoint.getArgs()[0];
		String correlationId = (String) message.getHeaders().get("spring_returned_message_correlation");
		// check whether message has been consumed
		if (eventUtil.checkConsumedInCache(correlationId))
			return;

		// get lock
		RLock lock = eventUtil.getConsumeLock(correlationId);
		lock.lock();

		try {
			// double check lock
			if (!eventUtil.checkConsumedInDB(correlationId)) {
				joinPoint.proceed();
				MessagePO messagePO = MessagePOFactory.successMessage(correlationId, message.getPayload());
				messageMapper.insert(messagePO);
				eventUtil.setConsumedInCache(correlationId, message.getPayload());
			}
		} finally {
			lock.unlock();
		}
	}
}
