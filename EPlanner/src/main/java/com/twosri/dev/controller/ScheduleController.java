package com.twosri.dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twosri.dev.bean.CustomException;
import com.twosri.dev.bean.JSEvent;
import com.twosri.dev.bean.RestResponse;
import com.twosri.dev.service.IEventService;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ScheduleController {

	@Autowired
	IEventService eventService;

	@Autowired
	ResponseBuilder responseBuilder;

	@PostMapping(value = "/admin/orders/events", produces = "application/json")
	public RestResponse proposal(String orderId) {
		try {
			List<JSEvent> eventMap = eventService.loadEvents(orderId);
			String msg = CustomMessage.getMessage("SUCCESS", null);
			return responseBuilder.createSuccessResponse(eventMap, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}

}
