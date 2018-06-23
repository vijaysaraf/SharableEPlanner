package com.twosri.dev.util;

import org.springframework.stereotype.Component;

import com.twosri.dev.bean.RestResponse;

@Component
public class ResponseBuilder {
	
	public RestResponse createSuccessResponse(Object returnObject,String message) {
		RestResponse response = new RestResponse();
		response.setSuccessMessage(message);
		response.setObject(returnObject);
		return response;
	}
	public RestResponse createErrorResponse(Object returnObject,String message) {
		RestResponse response = new RestResponse();
		response.setErrorMessage(message);
		response.setObject(returnObject);
		return response;
	}

}
