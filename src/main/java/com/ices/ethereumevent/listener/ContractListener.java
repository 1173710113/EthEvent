package com.ices.ethereumevent.listener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.tx.Contract;

import com.alibaba.fastjson2.JSON;
import com.ices.ethereumevent.config.EventFilterConfig;
import com.ices.ethereumevent.domain.BaseEventResponseBody;
import com.ices.ethereumevent.event.EventFactory;
import com.ices.ethereumevent.event.EventFilter;
import com.ices.ethereumevent.event.ParameterType;
import com.ices.ethereumevent.mapper.MessageMapper;
import com.ices.ethereumevent.util.RabbitMQUtil;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ContractListener {

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	Web3j web3j;

	@Autowired
	RabbitMQUtil rabbitMQUtil;
	
	@Autowired
	MessageMapper messageMapper;

	@Autowired
	EventFilterConfig eventFilterConfig;

	public static final Event TRANSFERSINGLE_EVENT = new Event("TransferSingle",
			Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
			}, new TypeReference<Address>(true) {
			}, new TypeReference<Address>(true) {
			}, new TypeReference<Uint256>() {
			}, new TypeReference<Uint256>() {
			}));;

	@PostConstruct
	public void listen() {
		eventFilterConfig.getEventFilters().stream().forEach(eventFilter -> {
			Event event = EventFactory.get(eventFilter.getEventSpecification().getEventName(),
					eventFilter.getEventSpecification().getParameterTypes());
			String hash = EventEncoder.encode(event);
			EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
					eventFilter.getContractAddress()).addSingleTopic(hash);
			subscribe(filter, eventFilter, event);
		});
	}

	private void subscribe(EthFilter filter, EventFilter eventFilter, Event event) {
		web3j.ethLogFlowable(filter).subscribe(EthLog -> {
			log.info("log:" + JSON.toJSONString(EthLog));
			EventValues values = Contract.staticExtractEventParameters(event, EthLog);
			Object obj = reflectObjec(eventFilter, values);
			log.info("obj:" + JSON.toJSON(obj));
			BaseEventResponseBody<Object> eventResponseBody = new BaseEventResponseBody<>(EthLog, obj);
			log.info("response:" + JSON.toJSONString(eventResponseBody));
			rabbitMQUtil.send(eventFilter.getExchange(), eventFilter.getBindingKey(),
					JSON.toJSONString(eventResponseBody),
					EthLog.getTransactionHash() + EthLog.getLogIndex().toString());
		});
	}

	private Object reflectObjec(EventFilter eventFilter, EventValues values)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		Class<?> bindingDataClass = Class.forName(eventFilter.getBindingData());
		Object obj = bindingDataClass.getDeclaredConstructor().newInstance();
		int indexedValueCount = 0;
		int unindexedValueCount = 0;
		for (ParameterType parameterType : eventFilter.getEventSpecification().getParameterTypes()) {
			Field field = bindingDataClass.getDeclaredField(parameterType.getName());
			field.setAccessible(true);
			if (parameterType.isIndexed()) {
				Object value = values.getIndexedValues().get(indexedValueCount++).getValue();
				field.set(obj, value);
			} else {
				Object value = values.getNonIndexedValues().get(unindexedValueCount++).getValue();
				field.set(obj, value);
			}
		}
		return obj;
	}
}
