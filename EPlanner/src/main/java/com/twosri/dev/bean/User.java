package com.twosri.dev.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {
	private String id;
	private String userId;
	private String passcode;
	private boolean active;
}
