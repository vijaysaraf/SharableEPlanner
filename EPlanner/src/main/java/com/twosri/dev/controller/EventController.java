package com.twosri.dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twosri.dev.bean.CustomException;
import com.twosri.dev.bean.Event;
import com.twosri.dev.bean.JSEvent;
import com.twosri.dev.bean.Order;
import com.twosri.dev.bean.RestResponse;
import com.twosri.dev.service.IEventService;
import com.twosri.dev.service.impl.EventAdviser;
import com.twosri.dev.util.Converter;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EventController {

	@Autowired
	IEventService eventService;

	@Autowired
	Converter converter;

	@Autowired
	ResponseBuilder responseBuilder;
	
	@Autowired
	EventAdviser adviser;

	@GetMapping(value = "/admin/events/load", produces = "application/json")
	public List<Event> load() {
		List<Event> eventList = eventService.findAll();
		log.info("event records found of size {}", eventList.size());
		return eventList;
	}

	@PostMapping(value = "/admin/events/save", produces = "application/json")
	public RestResponse save(Event event) {
		try {
			Event savedEvent = eventService.save(event);
			String msg = "";
			if (event.getId() != null)
				msg = CustomMessage.getMessage(CustomMessage.EVENT_EDITED_SUCCESS,
						new String[] { savedEvent.getId().toUpperCase() });
			else
				msg = CustomMessage.getMessage(CustomMessage.EVENT_ADDED_SUCCESS,
						new String[] { savedEvent.getId().toUpperCase() });
			return responseBuilder.createSuccessResponse(savedEvent, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}

	@PostMapping(value = "/admin/events/delete", produces = "application/json")
	public RestResponse delete(Event event) {
		try {
			eventService.delete(event);
			String msg = CustomMessage.getMessage(CustomMessage.EVENT_DELETED_SUCCESS,
					new String[] { event.getId().toUpperCase() });
			return responseBuilder.createSuccessResponse(null, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}
	@PostMapping(value = "/admin/events/proposal", produces = "application/json")
	public RestResponse proposal(Order dummyOrder) {
		try {
			List<JSEvent> jsEvents = adviser.generateEvents(dummyOrder);
			String msg = CustomMessage.getMessage("SUCCESS",null);
			return responseBuilder.createSuccessResponse(jsEvents, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}
}
