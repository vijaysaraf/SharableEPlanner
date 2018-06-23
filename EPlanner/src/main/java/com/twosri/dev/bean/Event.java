package com.twosri.dev.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Event {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private String id;

	private Date startDate;

	private String displayStartDate;

	private Date endDate;

	private String displayEndDate;

	private String text;

	private String details;

	private String orderId;

	private String orderNumber;

	private String phaseId;

	private String phaseName;

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		if (startDate != null)
			setDisplayStartDate(dateFormat.format(startDate));
		else
			setDisplayStartDate("");
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		if (endDate != null)
			setDisplayEndDate(dateFormat.format(endDate));
		else
			setDisplayEndDate("");
	}

}