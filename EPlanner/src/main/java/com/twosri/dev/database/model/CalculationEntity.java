package com.twosri.dev.database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Calculation")
public class CalculationEntity {

	@Id
	private String id;

	@Indexed
	private String productType;
	@Indexed
	private String phaseId;
	
	private int givenManPower;
	private double calculatedManHours;
}
