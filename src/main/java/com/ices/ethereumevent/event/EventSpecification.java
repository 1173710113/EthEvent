package com.ices.ethereumevent.event;

import java.util.List;

public class EventSpecification {

	private String eventName;

	private List<ParameterType> parameterDefinitions;

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public List<ParameterType> getParameterTypes() {
		return parameterDefinitions;
	}

	public void setParameterTypes(List<ParameterType> parameterTypes) {
		this.parameterDefinitions = parameterTypes;
	}

}
