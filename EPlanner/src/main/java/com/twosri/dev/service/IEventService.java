package com.twosri.dev.service;

import java.util.Date;
import java.util.List;

import com.twosri.dev.bean.Event;
import com.twosri.dev.bean.JSEvent;

public interface IEventService {

	void delete(Event deleted);

	List<Event> findAll();

	Event findOne(String id);

	Event save(Event saved);

	Event findAnyGreaterThanGivenDate(String phaseId, Date minimumDate);

	void deleteAll();

	void pushEvents(String orderId);

	List<JSEvent> loadEvents(String orderId);
	
	void deleteByOrderId(String orderId);
}
