package com.twosri.dev.bean;

import lombok.Data;

@Data
public class Summary {
	private String id;
	private String orderNumber;
	private String jobNumber;
	private String designId;
	private boolean isNew;
	private int width;
	private int depth;
	private int height;
	private int quantity;
	private String productName;
	private String phaseName;
	private String displayStartDate;
	private String displayEndDate;
	private double hrsRequired;
	
}
