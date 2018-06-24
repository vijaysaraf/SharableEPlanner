package com.twosri.dev.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twosri.dev.bean.Calculation;
import com.twosri.dev.bean.Event;
import com.twosri.dev.bean.Order;
import com.twosri.dev.bean.Phase;
import com.twosri.dev.database.repository.interfaces.IUtilityRepository;
import com.twosri.dev.service.impl.CalculationService;
import com.twosri.dev.service.impl.EventService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventAdviserHelper {

	@Autowired
	EventService eventService;
	@Autowired
	CalculationService calService;

	public List<Event> getEventChunks(Order order, Phase phase, Date startDate) {
		log.info("enter.getChunks for {}\n", phase.getName());
		List<Event> events = new ArrayList<>();
		LocalDateTime minimumStartDate = getBaselineStartDate(phase, startDate);
		LocalDateTime tentativeEndDate = getTentativeEndDate(order, phase, minimumStartDate);
		log.info("minimumStartDate {} and tentativeEndDate {} and duration is {} ", minimumStartDate, tentativeEndDate,
				((Duration.between(minimumStartDate, tentativeEndDate).getSeconds()) / (60 * 60)) + "\n");
		while (true) {
			ProcessingAttributes attributes = generateChunk(order, phase, minimumStartDate, tentativeEndDate);
			if (attributes != null) {
				log.info("event created with StartDate {} and EndDate {}", attributes.getEvent().getStartDate(),
						attributes.getEvent().getEndDate());
				events.add(attributes.getEvent());
				minimumStartDate = getLocalDateTime(attributes.getEvent().getEndDate());
				tentativeEndDate = attributes.getAdjustedEndDate();
			} else
				break;
		}
		log.info("exit.getChunks for {}\n", phase.getName());
		return events;
	}

	@Getter
	class ProcessingAttributes {
		Event event;
		LocalDateTime adjustedEndDate;

		public ProcessingAttributes(Event event, LocalDateTime adjustedEndDate) {
			super();
			this.event = event;
			this.adjustedEndDate = adjustedEndDate;
		}

	}

	public ProcessingAttributes createOutSourceEvent(Order order, Phase phase, LocalDateTime minimumStartDate,
			LocalDateTime tentativeEndDate) {
		Event event = getEventWithBasicDetails(order, phase);
		event.setStartDate(getUtilDate(minimumStartDate));
		event.setEndDate(getUtilDate(tentativeEndDate));
		return new ProcessingAttributes(event, tentativeEndDate);
	}

	public ProcessingAttributes generateChunk(Order order, Phase phase, LocalDateTime minimumStartDate,
			LocalDateTime tentativeEndDate) {
		ProcessingAttributes attributes = null;

		if (minimumStartDate.isBefore(tentativeEndDate)) {
			if (phase.getName().equals(PhaseEnum.PC.name()))
				return createOutSourceEvent(order, phase, minimumStartDate, tentativeEndDate);
			Event event = getEventWithBasicDetails(order, phase);
			LocalDateTime adjustedStartDate = minimumStartDate;
			LocalDateTime adjustedEndDate = tentativeEndDate;
			Duration eventDuration = Duration.between(adjustedStartDate, adjustedEndDate);
			Duration preBreak = Duration.between(getDayStart(), getBreakStartTime());
			Duration postBreak = Duration.between(getBreakEndTime(), getDayEnd());

			LocalTime startTime = LocalTime.of(minimumStartDate.getHour(), minimumStartDate.getMinute());
			if (startTime.isBefore(getDayStart())) {
				log.info("inside before day start");
				int minuteAdjustment = (getDayStart().getHour() * 60) - (minimumStartDate.getHour() * 60);
				adjustedStartDate = minimumStartDate.plusMinutes(minuteAdjustment);
				adjustedEndDate = tentativeEndDate.plusMinutes(minuteAdjustment);
				if (eventDuration.toMinutes() < preBreak.toMinutes()
						|| eventDuration.toMinutes() == preBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(getUtilDate(adjustedEndDate));
				} else if (eventDuration.toMinutes() > preBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(
							getUtilDate(LocalDateTime.of(adjustedStartDate.toLocalDate(), getBreakStartTime())));
				}
			} else if (startTime.equals(getDayStart())) {
				log.info("inside on day start ");
				if (eventDuration.toMinutes() < preBreak.toMinutes()
						|| eventDuration.toMinutes() == preBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(getUtilDate(adjustedEndDate));
				} else if (eventDuration.toMinutes() > preBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(
							getUtilDate(LocalDateTime.of(adjustedStartDate.toLocalDate(), getBreakStartTime())));
				}
			} else if (startTime.isAfter(getDayStart()) && startTime.isBefore(getBreakStartTime())) {
				log.info("inside between day start and break start");
				Duration preBreakAvailableTime = Duration.between(startTime, getBreakStartTime());
				if (eventDuration.toMinutes() < preBreakAvailableTime.toMinutes()
						|| eventDuration.toMinutes() == preBreakAvailableTime.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(getUtilDate(adjustedEndDate));
				} else if (eventDuration.toMinutes() > preBreakAvailableTime.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(
							getUtilDate(LocalDateTime.of(adjustedStartDate.toLocalDate(), getBreakStartTime())));
				}
			} else if (startTime.equals(getBreakStartTime())) {
				log.info("inside on break start");
				adjustedStartDate = minimumStartDate.with(getBreakEndTime());
				adjustedEndDate = tentativeEndDate.plus(getBreakDuration().toMinutes(), ChronoUnit.MINUTES);
				if (eventDuration.toMinutes() < postBreak.toMinutes()
						|| eventDuration.toMinutes() == postBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(getUtilDate(adjustedEndDate));
				} else if (eventDuration.toMinutes() > postBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(getUtilDate(LocalDateTime.of(adjustedStartDate.toLocalDate(), getDayEnd())));
				}

			} else if (startTime.isAfter(getBreakStartTime()) && startTime.isBefore(getBreakEndTime())) {
				log.info("inside during break start");

				long minuteAdjustment = getBreakDuration().toMinutes() - startTime.getMinute();
				adjustedStartDate = minimumStartDate.plusMinutes(minuteAdjustment);
				adjustedEndDate = tentativeEndDate.plusMinutes(minuteAdjustment);

				if (eventDuration.toMinutes() < postBreak.toMinutes()
						|| eventDuration.toMinutes() == postBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(getUtilDate(adjustedEndDate));
				} else if (eventDuration.toMinutes() > postBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(getUtilDate(LocalDateTime.of(adjustedStartDate.toLocalDate(), getDayEnd())));
				}
			} else if ((startTime.isAfter(getBreakEndTime()) || startTime.equals(getBreakEndTime()))
					&& startTime.isBefore(getDayEnd())) {
				log.info("inside on or after break time");
				if (eventDuration.toMinutes() < postBreak.toMinutes()
						|| eventDuration.toMinutes() == postBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(getUtilDate(adjustedEndDate));
				} else if (eventDuration.toMinutes() > postBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(getUtilDate(LocalDateTime.of(adjustedStartDate.toLocalDate(), getDayEnd())));
				}
			} else if (startTime.isAfter(getDayEnd()) || startTime.equals(getDayEnd())) {
				log.info("inside after day end or on day end");
				long minuteAdjustment = Duration
						.between(minimumStartDate, minimumStartDate.plusDays(1).with(getDayStart())).toMinutes();
				adjustedStartDate = minimumStartDate.plusMinutes(minuteAdjustment);
				adjustedEndDate = tentativeEndDate.plusMinutes(minuteAdjustment);
				if (eventDuration.toMinutes() < preBreak.toMinutes()
						|| eventDuration.toMinutes() == preBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(getUtilDate(adjustedEndDate));
				} else if (eventDuration.toMinutes() > preBreak.toMinutes()) {
					event.setStartDate(getUtilDate(adjustedStartDate));
					event.setEndDate(
							getUtilDate(LocalDateTime.of(adjustedStartDate.toLocalDate(), getBreakStartTime())));
				}
			}
			attributes = new ProcessingAttributes(event, adjustedEndDate);
			return attributes;
		} else
			return null;
	}

	public Date getUtilDate(LocalDateTime localDateTime) {
		return java.sql.Timestamp.valueOf(localDateTime);
	}

	private LocalDateTime getBaselineStartDate(Phase phase, Date minimumStartDate) {
		Event existingEvent = eventService.findAnyGreaterThanGivenDate(phase.getId(), minimumStartDate);
		if (existingEvent == null) {
			if (minimumStartDate.getHours() == 0)
				return getLocalDateTime(minimumStartDate).with(getDayStart());
			else
				return getLocalDateTime(minimumStartDate);
		} else
			return getLocalDateTime(existingEvent.getEndDate());
		/*
		 * } else { existingEvents.sort(new Comparator<Event>() {
		 * 
		 * @Override public int compare(Event event1, Event event2) { return
		 * event1.getEndDate().compareTo(event2.getEndDate()); } }); return
		 * getLocalDateTime(existingEvents.get(0).getEndDate());
		 * 
		 * }
		 */
	}

	private Event getEventWithBasicDetails(Order order, Phase phase) {
		Event event = new Event();
		String eventDetails = order.getCustomerId() + "-" + order.getOrderNumber();
		event.setOrderId(order.getId());
		event.setText(order.getJobNumber().concat("-").concat(phase.getName()));
		event.setDetails(eventDetails);
		event.setPhaseId(phase.getId());
		return event;
	}

	public LocalDateTime getTentativeEndDate(Order order, Phase phase, LocalDateTime availableStartDate) {
		long calculatedRequiredTime = 0;
		if (phase.getName().equals(PhaseEnum.DESIGN.name())) {
			calculatedRequiredTime = getDesignTime(order);
		} else if (phase.getName().equals(PhaseEnum.PC.name())) {
			return availableStartDate.plusDays(1).with(getDayEnd());
		} else {
			calculatedRequiredTime = getTimeRequiredInMinutes(order, phase);
		}
		LocalDateTime calculatedEndDate = availableStartDate.plus(calculatedRequiredTime, ChronoUnit.MINUTES);
		return calculatedEndDate;
	}
	
	
	// TODO change from long to double
	public long getTimeRequiredInMinutes(Order order, Phase phase) {
		Calculation calculation = calService.findByProductTypeAndPhaseId(order.getProductType(), phase.getId());
		long manPowerAvailable = phase.getDefaultManPower();
		double manHoursNeeded = calculation != null ? calculation.getCalculatedManHours() : 1;
		long manMinuteNeeded = convertHoursToMinute(manHoursNeeded);
		long calculatedTimeNeeded = (manMinuteNeeded / manPowerAvailable) * order.getQuantity();
		return calculatedTimeNeeded;

	}

	private long convertHoursToMinute(double manHours) {
		return (new Double(manHours * 60)).longValue();
	}

	private LocalDateTime getLocalDateTime(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	private LocalDate getLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private LocalTime getDayStart() {
		LocalTime dayStart = LocalTime.parse("08:00");
		return dayStart;
	}

	private LocalTime getDayEnd() {
		LocalTime dayEnd = LocalTime.parse("18:30");
		return dayEnd;
	}

	private LocalDateTime getNextWorkingDay(LocalDateTime dateTime) {
		while (isHoliday(dateTime) || isWeeklyOff(dateTime)) {
			log.info("dateTime {} is holiday or off", dateTime);
			dateTime = dateTime.plusDays(1);
			log.info("new dateTime {} is ", dateTime);
		}
		return dateTime;
	}

	private boolean isHoliday(LocalDateTime dateTime) {
		return getHolidayList().contains(dateTime);
	}

	private boolean isWeeklyOff(LocalDateTime dateTime) {
		return dateTime.getDayOfWeek().getValue() == getWeeklyOff();
	}

	private List<LocalDateTime> getHolidayList() {
		List<LocalDateTime> holidays = new ArrayList<>();
		holidays.add(LocalDateTime.of(2018, 5, 28, 0, 0));
		holidays.add(LocalDateTime.of(2018, 6, 1, 0, 0));
		return holidays;
	}

	private int getWeeklyOff() {
		return 4;
	}

	private LocalTime getBreakStartTime() {
		LocalTime breakStart = LocalTime.parse("12:30");
		return breakStart;
	}

	private LocalTime getBreakEndTime() {
		LocalTime breakStart = LocalTime.parse("13:00");
		return breakStart;
	}

	private Duration getBreakDuration() {
		return Duration.between(getBreakStartTime(), getBreakEndTime());
	}

	public long getDesignTime(Order order) {
		long calculatedRequiredTime = -1;
		if (order.isNew())
			calculatedRequiredTime = getTimeForNewDesign();
		else
			calculatedRequiredTime = getTimeForExistingDesign();
		return calculatedRequiredTime;
	}

	private long getTimeForNewDesign() {
		return 3 * 60;
	}

	private long getTimeForExistingDesign() {
		return 30;
	}
}
