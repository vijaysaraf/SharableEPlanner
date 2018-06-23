package com.twosri.dev.bean;

import java.util.Date;

import com.twosri.dev.util.Status;

import lombok.Data;

@Data
public class Order {
	private String id;
	private String customerId;
	private String customerName;
	private String orderNumber;
	private String jobNumber;
	private String designId;
	private boolean isNew;
	private int width;
	private int depth;
	private int height;
	private int quantity;
	private String productType;
	private String productName;
	private double price;
	private Status status;
	private String displayStartDate;
	private Date startDate;

}
