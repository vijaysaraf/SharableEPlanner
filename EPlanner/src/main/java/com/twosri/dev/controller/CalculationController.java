package com.twosri.dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twosri.dev.bean.CustomException;
import com.twosri.dev.bean.RestResponse;
import com.twosri.dev.bean.Calculation;
import com.twosri.dev.service.ICalculationService;
import com.twosri.dev.util.Converter;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CalculationController {

	@Autowired
	ICalculationService calculationService;

	@Autowired
	Converter converter;

	@Autowired
	ResponseBuilder responseBuilder;

	@GetMapping(value = "/admin/calculations/load", produces = "application/json")
	public List<Calculation> load() {
		List<Calculation> calculationList = calculationService.findAll();
		log.info("calculation records found of size {}", calculationList.size());
		return calculationList;
	}

	@PostMapping(value = "/admin/calculations/save", produces = "application/json")
	public RestResponse save(Calculation calculation) {
		try {
			log.info("{}", calculation);
			Calculation savedCalculation = calculationService.save(calculation);
			String msg = "";
			if (calculation.getId() != null)
				msg = CustomMessage.getMessage(CustomMessage.CALCULATION_ENTRY_EDITED_SUCCESS, new String[] {
						calculation.getPhaseName().toUpperCase(), calculation.getProductName().toUpperCase() });
			else
				msg = CustomMessage.getMessage(CustomMessage.CALCULATION_ENTRY_ADDED_SUCCESS, new String[] {
						calculation.getPhaseName().toUpperCase(), calculation.getProductName().toUpperCase() });
			return responseBuilder.createSuccessResponse(savedCalculation, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}

	@PostMapping(value = "/admin/calculations/delete", produces = "application/json")
	public RestResponse delete(Calculation calculation) {
		try {
			log.info("{}", calculation);
			calculationService.delete(calculation);
			String msg = CustomMessage.getMessage(CustomMessage.CALCULATION_ENTRY_DELETED_SUCCESS, null);
			return responseBuilder.createSuccessResponse(null, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}
}
