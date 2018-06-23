package com.twosri.dev.database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "REFERENCE")
public class ReferenceEntity {
	@Id
	private String id;
	@Indexed
	private String domain;
	private String name;
	private String value;
}
