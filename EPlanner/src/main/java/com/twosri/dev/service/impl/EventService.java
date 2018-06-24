package com.twosri.dev.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twosri.dev.bean.Event;
import com.twosri.dev.bean.JSEvent;
import com.twosri.dev.database.model.EventEntity;
import com.twosri.dev.database.repository.EventRepository;
import com.twosri.dev.database.repository.UtilityRepository;
import com.twosri.dev.service.IEventService;
import com.twosri.dev.service.cache.CacheManager;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ErrorCode;
import com.twosri.dev.util.ExceptionFactory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventService implements IEventService {

	@Autowired
	EventRepository repository;

	@Autowired
	ModelMapper mMapper;

	@Autowired
	ExceptionFactory exceptionFactory;

	@Autowired
	EventAdviser eventAdviser;

	@Override
	public void delete(Event deleted) {
		try {
			// repository.delete(deleted.getId());
			deleteAll();
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	@Autowired
	CacheManager cacheManager;

	@Override
	public List<Event> findAll() {

		List<EventEntity> entityList = repository.findAll();
		return mapEntity(entityList);
	}

	@Override
	public Event findOne(String id) {
		EventEntity entity = repository.findOne(id);
		return entity != null ? mMapper.map(entity, Event.class) : null;
	}

	@Override
	public Event save(Event toBeSaved) {
		validateEvent(toBeSaved);
		try {
			log.info("to be saved event {}", toBeSaved);
			EventEntity entity = repository.save(mMapper.map(toBeSaved, EventEntity.class));
			log.info("saved event {}", entity);
			return mMapper.map(entity, Event.class);
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	@Autowired
	UtilityRepository utilityRepository;

	@Override
	public Event findAnyGreaterThanGivenDate(String phaseId, Date minimumDate) {
		EventEntity entity = utilityRepository.findAnyGreaterThanGivenDate(phaseId, minimumDate);
		return entity != null ? mMapper.map(entity, Event.class) : null;
	}

	@Override
	public void deleteAll() {
		repository.deleteAll();

	}

	@Override
	public void pushEvents(String orderId) {
		List<Event> events = eventAdviser.pushEvents(orderId);
		repository.save(mapEvent(events));
	}

	@Override
	public List<JSEvent> loadEvents(String orderId) {
		List<EventEntity> entityList = null;
		if (orderId.equals("ALL")) {
			Date startDate = getUtilDate(LocalDateTime.now().minusMonths(1L));
			Date endDate = getUtilDate(LocalDateTime.now().plusMonths(3L));
			entityList = repository.findByStartDateBetween(startDate, endDate);
		} else {
			entityList = repository.findByOrderIdOrderByStartDate(orderId);
		}

		List<Event> eventList = mapEntity(entityList);
		List<JSEvent> jsEvents = eventAdviser.getJavaScriptEvents(null, eventList);
		return jsEvents;
	}
	public Date getUtilDate(LocalDateTime localDateTime) {
		return java.sql.Timestamp.valueOf(localDateTime);
	}

	private List<Event> mapEntity(List<EventEntity> entityList) {
		return entityList.stream().map(entity -> {
			Event event = mMapper.map(entity, Event.class);
			event.setPhaseName(cacheManager.getPhase(entity.getPhaseId()).getName());
			return event;
		}).collect(Collectors.toList());
	}

	private List<EventEntity> mapEvent(List<Event> eventList) {
		return eventList.stream().map(event -> {
			EventEntity entity = mMapper.map(event, EventEntity.class);
			return entity;
		}).collect(Collectors.toList());
	}

	private void validateEvent(Event toBeSaved) {
		String msg = "";
		if (!msg.equals("")) {
			throw exceptionFactory.createException(ErrorCode.VALIDATION, msg, null);
		}

	}
	public void deleteByOrderId(String orderId) {
		repository.deleteByOrderId(orderId);
	}

}
