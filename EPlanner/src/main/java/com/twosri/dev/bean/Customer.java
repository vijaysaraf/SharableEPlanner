package com.twosri.dev.bean;

import lombok.Data;

@Data
public class Customer {
	private String id;
	private String name;
	private String address;
	private String contact;

	public Customer(String id, String name, String address, String contact) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.contact = contact;
	}

	public Customer() {
		super();
	}

}
