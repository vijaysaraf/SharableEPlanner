package com.twosri.dev.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twosri.dev.bean.CustomException;
import com.twosri.dev.bean.RestResponse;
import com.twosri.dev.bean.Phase;
import com.twosri.dev.service.IPhaseService;
import com.twosri.dev.util.Converter;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.PhaseEnum;
import com.twosri.dev.util.ResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PhaseController {

	@Autowired
	IPhaseService phaseService;

	@Autowired
	Converter converter;

	@Autowired
	ResponseBuilder responseBuilder;

	@GetMapping(value = "/admin/phases/load", produces = "application/json")
	public List<Phase> load() {
		List<Phase> phaseList = phaseService.findAll();
		log.info("Phase records found of size {}", phaseList.size());
		return phaseList;
	}

	@GetMapping(value = "/admin/phases/names", produces = "application/json")
	public String[] loadNames() {
		return phaseService.getPhaseNames();
	}

	@PostMapping(value = "/admin/phases/save", produces = "application/json")
	public RestResponse save(Phase Phase) {
		try {
			Phase savedPhase = phaseService.save(Phase);
			String msg = "";
			if (Phase.getId() != null)
				msg = CustomMessage.getMessage(CustomMessage.PHASE_EDITED_SUCCESS,
						new String[] { Phase.getName().toUpperCase() });
			else
				msg = CustomMessage.getMessage(CustomMessage.PHASE_ADDED_SUCCESS,
						new String[] { Phase.getName().toUpperCase() });
			return responseBuilder.createSuccessResponse(savedPhase, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}

	@PostMapping(value = "/admin/phases/delete", produces = "application/json")
	public RestResponse delete(Phase Phase) {
		try {
			phaseService.delete(Phase);
			String msg = CustomMessage.getMessage(CustomMessage.PHASE_DELETED_SUCCESS,
					new String[] { Phase.getName().toUpperCase() });
			return responseBuilder.createSuccessResponse(null, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}
}
