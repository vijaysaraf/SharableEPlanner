package com.twosri.dev.bean;

import lombok.Data;

@Data
public class Calculation {

	private String id;
	private String productType;
	private String phaseId;
	private String productName; //for display
	private String phaseName; // for display
	private int givenManPower;
	private double calculatedManHours;
	
}
