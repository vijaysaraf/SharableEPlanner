package com.twosri.dev.database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "CUSTOMER")
public class CustomerEntity {

	@Id
	private String id;

	@Indexed(unique = true)
	private String name;

	private String address;

	private String contact;

}
