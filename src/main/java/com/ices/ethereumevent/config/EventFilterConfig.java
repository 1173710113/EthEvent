package com.ices.ethereumevent.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ices.ethereumevent.event.EventFilter;

@Configuration
@ConfigurationProperties(prefix = "event-config")
public class EventFilterConfig {

	private List<EventFilter> eventFilters;

	public List<EventFilter> getEventFilters() {
		return eventFilters;
	}

	public void setEventFilters(List<EventFilter> eventFilters) {
		this.eventFilters = eventFilters;
	}

}
