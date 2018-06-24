package com.twosri.dev.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twosri.dev.bean.Event;
import com.twosri.dev.bean.JSEvent;
import com.twosri.dev.bean.Order;
import com.twosri.dev.bean.Phase;
import com.twosri.dev.service.cache.CacheManager;
import com.twosri.dev.util.EventAdviserHelper;
import com.twosri.dev.util.PhaseEnum;
import com.twosri.dev.util.SummaryHelper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventAdviser {

	@Autowired
	PhaseService phaseService;

	@Autowired
	EventAdviserHelper adviserHelper;

	@Autowired
	OrderService orderService;

	@Autowired
	CacheManager cacheManager;
	
	@Autowired
	SummaryHelper summaryHelper;

	public List<Event> pushEvents(String orderId) {
		log.info("enter.pushEvents");
		Order order = orderService.findOne(orderId);
		String[] phaseNames = phaseService.getPhaseNames();
		List<Phase> phases = phaseService.findAll();
		final Date startDate = order.getStartDate();
		List<Event> allEvents = new ArrayList<>();
		for (int i = 0; i < phaseNames.length; i++) {
			Phase phase = cacheManager.getPhase(phaseNames[i]);
			if (phase == null || phase.getId() == null)
				phase = new Phase(phaseNames[i], phaseNames[i], PhaseEnum.valueOf(phaseNames[i]).ordinal(), 1);
			List<Event> events = adviserHelper.getEventChunks(order, phase, startDate);
			summaryHelper.pushSummary(order, phase, events);
			startDate.setTime(events.get(events.size() - 1).getEndDate().getTime());
			allEvents.addAll(events);
		}
		log.info("exit.pushEvents");
		return allEvents;
	}

	public List<JSEvent> generateEvents(Order order) {
		log.info("enter.generateEvents");
		List<Phase> phases = phaseService.findAll();

		final List<JSEvent> jsEvents = new ArrayList<>();
		final Date startDate = order.getStartDate();
		phases.forEach(phase -> {
			List<Event> events = adviserHelper.getEventChunks(order, phase, startDate);
			startDate.setTime(events.get(events.size() - 1).getEndDate().getTime());
			jsEvents.addAll(getJavaScriptEvents(phase, events));
		});
		log.info("exit.generateEvents");
		return jsEvents;
	}

	public List<JSEvent> getJavaScriptEvents(Phase phase, List<Event> events) {
		List<JSEvent> jsEvents = new ArrayList<>();
		events.forEach(event -> {
			String id = "";
			if (phase != null)
				id = (phase.getName().toUpperCase() + "_" + jsEvents.size());
			else {
				Phase tempPhase = cacheManager.getPhase(event.getPhaseId());
				if (tempPhase == null || tempPhase.getId() == null)
					tempPhase = new Phase(event.getPhaseId(), event.getPhaseId(),
							PhaseEnum.valueOf(event.getPhaseId()).ordinal(), 1);
				id = (tempPhase.getName().toUpperCase() + "_" + jsEvents.size());
			}

			jsEvents.add(new JSEvent(id, event.getDisplayStartDate(), event.getDisplayEndDate(), event.getText(),
					event.getDetails()));
		});
		return jsEvents;
	}
}
