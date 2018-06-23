package com.twosri.dev.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JSEvent {
	private String id;

	private String start_date;

	private String end_date;

	private String text;

	private String details;

	public JSEvent(String id, String start_date, String end_date, String text, String details) {
		super();
		this.id = id;
		this.start_date = start_date;
		this.end_date = end_date;
		this.text = text;
		this.details = details;
	}
	
}
