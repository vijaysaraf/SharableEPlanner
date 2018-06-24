package com.twosri.dev.database.model;

import java.util.Date;

import lombok.Data;

@Data
public class SummaryEntity {
	private String id;
	private String orderId;
	private String phaseId;
	private Date startDate;
	private Date endDate;
	private double hrsRequired;
}
