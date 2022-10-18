package com.ices.ethereumevent.event;

public class EventFilter {

	private String id;

	private EventSpecification eventSpecification;

	private String exchange;

	private String bindingKey;
	
	private String bindingData;
	
	private String contractAddress;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EventSpecification getEventSpecification() {
		return eventSpecification;
	}

	public void setEventSpecification(EventSpecification eventSpecification) {
		this.eventSpecification = eventSpecification;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getBindingKey() {
		return bindingKey;
	}

	public void setBindingKey(String bindingKey) {
		this.bindingKey = bindingKey;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public String getBindingData() {
		return bindingData;
	}

	public void setBindingData(String bindingData) {
		this.bindingData = bindingData;
	}

	
	
	
	
	

}
