package com.twosri.dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twosri.dev.bean.CustomException;
import com.twosri.dev.bean.JSEvent;
import com.twosri.dev.bean.RestResponse;
import com.twosri.dev.bean.Order;
import com.twosri.dev.service.IEventService;
import com.twosri.dev.service.IOrderService;
import com.twosri.dev.service.impl.EventAdviser;
import com.twosri.dev.util.Converter;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderController {

	@Autowired
	IOrderService orderService;

	@Autowired
	Converter converter;

	@Autowired
	ResponseBuilder responseBuilder;
	
	@Autowired
	IEventService eventService;

	@GetMapping(value = "/admin/orders/load", produces = "application/json")
	public List<Order> load() {
		List<Order> orderList = orderService.findAll();
		log.info("order records found of size {}", orderList.size());
		return orderList;
	}

	@PostMapping(value = "/admin/orders/save", produces = "application/json")
	public RestResponse save(Order order) {
		try {
			Order savedOrder = orderService.save(order);
			String msg = "";
			if (order.getId() != null)
				msg = CustomMessage.getMessage(CustomMessage.ORDER_ENTRY_ADDED_SUCCESS,
						new String[] { order.getOrderNumber().toUpperCase() });
			else {
				eventService.pushEvents(savedOrder.getId());
				msg = CustomMessage.getMessage(CustomMessage.ORDER_ENTRY_ADDED_SUCCESS,
						new String[] { order.getOrderNumber().toUpperCase() });
			}return responseBuilder.createSuccessResponse(savedOrder, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}

	@PostMapping(value = "/admin/orders/delete", produces = "application/json")
	public RestResponse delete(Order order) {
		try {
			orderService.delete(order);
			String msg = CustomMessage.getMessage(CustomMessage.ORDER_ENTRY_DELETED_SUCCESS,
					new String[] { order.getOrderNumber().toUpperCase() });
			return responseBuilder.createSuccessResponse(null, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}
	
}
