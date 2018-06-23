package com.twosri.dev.database.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "ORDER")
public class OrderEntity {

	@Id
	private String id;

	@Indexed
	private String customerId;
	@Indexed
	private String orderNumber;
	@Indexed
	private String jobNumber;
	private String designId;
	private boolean isNew;
	private int width;
	private int depth;
	private int height;
	private int quantity;
	@Indexed
	private String productType;
	private double price;
	private Date startDate;
}
