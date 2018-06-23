package com.twosri.dev.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Reference {
	private String id;
	private String domain;
	private String name;
	private String value;

}
