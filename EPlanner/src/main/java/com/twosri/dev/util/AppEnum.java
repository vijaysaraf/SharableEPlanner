package com.twosri.dev.util;

public enum AppEnum {
	

	_PATH_USER("admin/user"),
	_PATH_CUSTOMER("admin/customer"),
	_PATH_PHASE("admin/phase"),
	_PATH_REFERENCE("admin/reference"),
	_PATH_CALCULATION("admin/calculation"),
	_PATH_ORDER("admin/order"),
	_PATH_EVENT("admin/event"),
	_PATH_SCHEDULE("admin/schedule"),
	_PATH_PROPOSE_EVENT("admin/proposal"),
	_PATH_SUMMARY("admin/summary");
	private String value;

	AppEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
