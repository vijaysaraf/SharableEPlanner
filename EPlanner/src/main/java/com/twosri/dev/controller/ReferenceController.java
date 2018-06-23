package com.twosri.dev.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twosri.dev.bean.CustomException;
import com.twosri.dev.bean.Reference;
import com.twosri.dev.bean.RestResponse;
import com.twosri.dev.service.IReferenceService;
import com.twosri.dev.util.Converter;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ReferenceController {

	@Autowired
	IReferenceService referenceService;

	@Autowired
	Converter converter;

	@Autowired
	ResponseBuilder responseBuilder;

	private static String DOMAIN = "PRODUCT_TYPE";

	@GetMapping(value = "/admin/references/load", produces = "application/json")
	public List<Reference> load() {
		List<Reference> referenceList = referenceService.findAll();
		log.info("reference records found of size {}", referenceList.size());
		return referenceList;
	}

	@PostMapping(value = "/admin/references/save", produces = "application/json")
	public RestResponse save(Reference reference) {
		try {
			log.info("Reference to add/edit {}", reference);
			Reference savedReference = referenceService.save(reference);
			String msg = "";
			if (reference.getId() != null)
				msg = CustomMessage.getMessage(CustomMessage.REFERENCE_EDITED_SUCCESS,
						new String[] { reference.getName().toUpperCase() });
			else
				msg = CustomMessage.getMessage(CustomMessage.REFERENCE_ADDED_SUCCESS,
						new String[] { reference.getName().toUpperCase() });
			return responseBuilder.createSuccessResponse(savedReference, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}

	@PostMapping(value = "/admin/references/delete", produces = "application/json")
	public RestResponse delete(Reference reference) {
		try {
			referenceService.delete(reference);
			String msg = CustomMessage.getMessage(CustomMessage.REFERENCE_DELETED_SUCCESS,
					new String[] { reference.getName().toUpperCase() });
			return responseBuilder.createSuccessResponse(null, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}
}
