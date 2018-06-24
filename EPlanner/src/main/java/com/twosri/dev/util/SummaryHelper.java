package com.twosri.dev.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twosri.dev.bean.Event;
import com.twosri.dev.bean.Order;
import com.twosri.dev.bean.Phase;
import com.twosri.dev.database.model.SummaryEntity;
import com.twosri.dev.service.impl.SummaryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SummaryHelper {

	@Autowired
	SummaryService summaryService;

	public void pushSummary(Order order, Phase phase, List<Event> events) {
		SummaryEntity entity = new SummaryEntity();
		entity.setOrderId(order.getId());
		entity.setPhaseId(phase.getId());
		entity.setStartDate(events.get(0).getStartDate());
		entity.setEndDate(events.get(events.size() - 1).getEndDate());
		entity.setHrsRequired(getTimeNeeded(events));
		log.info("Summary Created \norderId [" + entity.getOrderId() + "] " + "\nPhaseId [" + entity.getPhaseId() + "] "
				+ "\nStartDate [" + entity.getStartDate() + "] " + "\nEndDate [" + entity.getEndDate()
				+ "] " + "\nHrsRequired [" + entity.getHrsRequired() + "]\n");
		summaryService.save(entity);
	}

	private double getTimeNeeded(List<Event> events) {
		double timeinMinutes = 0;
		for (int i = 0; i < events.size(); i++) {
			long millis = events.get(i).getEndDate().getTime() - events.get(i).getStartDate().getTime();
			timeinMinutes = timeinMinutes + (millis / 60000);
		}
		return timeinMinutes / 60;
	}
}
