package com.twosri.dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twosri.dev.bean.CustomException;
import com.twosri.dev.bean.RestResponse;
import com.twosri.dev.bean.Summary;
import com.twosri.dev.service.ISummaryService;
import com.twosri.dev.util.Converter;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SummaryController {

	@Autowired
	ISummaryService summaryService;

	@Autowired
	Converter converter;

	@Autowired
	ResponseBuilder responseBuilder;

	@GetMapping(value = "/admin/summary/load", produces = "application/json")
	public List<Summary> load() {
		List<Summary> summaryList = summaryService.findAll();
		log.info("summary records found of size {}", summaryList.size());
		return summaryList;
	}

	@PostMapping(value = "/admin/summary/save", produces = "application/json")
	public RestResponse save(Summary summary) {
		try {
			Summary savedSummary = summaryService.save(summary);
			String msg = "";
			if (summary.getId() != null)
				msg = CustomMessage.getMessage(CustomMessage.USER_EDITED_SUCCESS,
						new String[] { summary.getOrderNumber().toUpperCase() });
			else
				msg = CustomMessage.getMessage(CustomMessage.USER_ADDED_SUCCESS,
						new String[] { summary.getOrderNumber().toUpperCase() });
			return responseBuilder.createSuccessResponse(savedSummary, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}

	@PostMapping(value = "/admin/summary/delete", produces = "application/json")
	public RestResponse delete(Summary summary) {
		try {
			summaryService.delete(summary);
			String msg = CustomMessage.getMessage(CustomMessage.USER_DELETED_SUCCESS,
					new String[] { summary.getOrderNumber().toUpperCase() });
			return responseBuilder.createSuccessResponse(null, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}
}
