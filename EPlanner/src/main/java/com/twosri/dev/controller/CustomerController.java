package com.twosri.dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twosri.dev.bean.CustomException;
import com.twosri.dev.bean.RestResponse;
import com.twosri.dev.bean.Customer;
import com.twosri.dev.service.ICustomerService;
import com.twosri.dev.util.Converter;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CustomerController {

	@Autowired
	ICustomerService customerService;

	@Autowired
	Converter converter;

	@Autowired
	ResponseBuilder responseBuilder;

	@GetMapping(value = "/admin/customers/load", produces = "application/json")
	public List<Customer> load() {
		List<Customer> CustomerList = customerService.findAll();
		log.info("Customer records found of size {}", CustomerList.size());
		return CustomerList;
	}

	@PostMapping(value = "/admin/customers/save", produces = "application/json")
	public RestResponse save(Customer Customer) {
		try {
			Customer savedCustomer = customerService.save(Customer);
			String msg = "";
			if (Customer.getId() != null)
				msg = CustomMessage.getMessage(CustomMessage.CUSTOMER_EDITED_SUCCESS,
						new String[] { Customer.getName().toUpperCase() });
			else
				msg = CustomMessage.getMessage(CustomMessage.CUSTOMER_ADDED_SUCCESS,
						new String[] { Customer.getName().toUpperCase() });
			return responseBuilder.createSuccessResponse(savedCustomer, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}

	@PostMapping(value = "/admin/customers/delete", produces = "application/json")
	public RestResponse delete(Customer Customer) {
		try {
			customerService.delete(Customer);
			String msg = CustomMessage.getMessage(CustomMessage.CUSTOMER_DELETED_SUCCESS,
					new String[] { Customer.getName().toUpperCase() });
			return responseBuilder.createSuccessResponse(null, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}
}
