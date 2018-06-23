package com.twosri.dev.database.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "EVENT")
public class EventEntity {

	@Id
	private String id;

	@Indexed
	private Date startDate;

	@Indexed
	private Date endDate;

	private String text;

	private String details;

	private String orderId;

	private String phaseId;

	private String phaseName;
}
