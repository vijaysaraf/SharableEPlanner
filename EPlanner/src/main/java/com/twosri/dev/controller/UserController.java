package com.twosri.dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twosri.dev.bean.CustomException;
import com.twosri.dev.bean.RestResponse;
import com.twosri.dev.bean.User;
import com.twosri.dev.service.IUserService;
import com.twosri.dev.util.Converter;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

	@Autowired
	IUserService userService;

	@Autowired
	Converter converter;

	@Autowired
	ResponseBuilder responseBuilder;

	@GetMapping(value = "/admin/users/load", produces = "application/json")
	public List<User> load() {
		List<User> userList = userService.findAll();
		log.info("user records found of size {}", userList.size());
		return userList;
	}

	@PostMapping(value = "/admin/users/save", produces = "application/json")
	public RestResponse save(User user) {
		try {
			User savedUser = userService.save(user);
			String msg = "";
			if (user.getId() != null)
				msg = CustomMessage.getMessage(CustomMessage.USER_EDITED_SUCCESS,
						new String[] { user.getUserId().toUpperCase() });
			else
				msg = CustomMessage.getMessage(CustomMessage.USER_ADDED_SUCCESS,
						new String[] { user.getUserId().toUpperCase() });
			return responseBuilder.createSuccessResponse(savedUser, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}

	@PostMapping(value = "/admin/users/delete", produces = "application/json")
	public RestResponse delete(User user) {
		try {
			userService.delete(user);
			String msg = CustomMessage.getMessage(CustomMessage.USER_DELETED_SUCCESS,
					new String[] { user.getUserId().toUpperCase() });
			return responseBuilder.createSuccessResponse(null, msg);
		} catch (CustomException e) {
			log.error(e.toString());
			return responseBuilder.createErrorResponse(null, e.getDisplayMessage());
		}
	}
}
