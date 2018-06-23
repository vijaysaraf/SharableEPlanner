package com.twosri.dev.database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "USER")
public class UserEntity {
	
	@Id
	private String id;
	
	@Indexed(unique = true)
	private String userId;
	
	private String passcode;
	
	private boolean active;
}
