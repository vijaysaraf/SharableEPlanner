package com.twosri.dev.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twosri.dev.bean.Order;
import com.twosri.dev.database.model.OrderEntity;
import com.twosri.dev.database.repository.OrderRepository;
import com.twosri.dev.service.IOrderService;
import com.twosri.dev.service.cache.CacheManager;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ErrorCode;
import com.twosri.dev.util.ExceptionFactory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService implements IOrderService {

	@Autowired
	OrderRepository repository;

	@Autowired
	ModelMapper mMapper;

	@Autowired
	ExceptionFactory exceptionFactory;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	EventService eventService;

	@Autowired
	EventAdviser eventAdviser;

	@Autowired
	SummaryService summaryService;

	@Override
	public void delete(Order deleted) {
		try {
			summaryService.deleteAll();
			summaryService.deleteByOrderId(deleted.getId());
			eventService.deleteByOrderId(deleted.getId());
			repository.delete(deleted.getId());
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	@Override
	public List<Order> findAll() {
		List<OrderEntity> entityList = repository.findAll();
		List<Order> orders = entityList.stream().map(entity -> {
			Order order = mMapper.map(entity, Order.class);
			order.setProductName(cacheManager.getProduct(entity.getProductType()).getName());
			order.setCustomerName(cacheManager.getCustomer(entity.getCustomerId()).getName());
			try {
				log.info("date in database {}", entity.getStartDate());
				order.setDisplayStartDate(new SimpleDateFormat("dd/MM/yyyy").format(entity.getStartDate()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return order;
		}).collect(Collectors.toList());
		return orders;
	}

	@Override
	public Order findOne(String id) {
		OrderEntity entity = repository.findOne(id);
		if (entity == null)
			return new Order();
		return mMapper.map(entity, Order.class);
	}

	@Override
	public Order save(Order toBeSaved) {
		validateOrder(toBeSaved);
		try {
			updateDate(toBeSaved);
			OrderEntity entity = repository.save(mMapper.map(toBeSaved, OrderEntity.class));
			log.info("Saved in db {}", entity);
			eventAdviser.generateEvents(toBeSaved).forEach(event -> {
				log.info("event created as {}", event);
			});
			return mMapper.map(entity, Order.class);
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	private void updateDate(Order toBeSaved) {
		try {
			String displayStartDate = toBeSaved.getDisplayStartDate();
			Date startDate = new Date(new SimpleDateFormat("dd/MM/yyyy").parse(displayStartDate).getTime());
			toBeSaved.setStartDate(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void validateOrder(Order toBeSaved) {
		String msg = "";
		if (toBeSaved.getCustomerId() == null || toBeSaved.getCustomerId().trim().length() <= 0) {
			msg = CustomMessage.getMessage(CustomMessage.ORDER_CUSTOMER_IS_NOT_VALID, null);
		} else if (toBeSaved.getOrderNumber() == null || toBeSaved.getOrderNumber().trim().length() <= 0) {
			msg = CustomMessage.getMessage(CustomMessage.ORDER_NUMBER_IS_NOT_VALID, null);
		} else if (toBeSaved.getJobNumber() == null || toBeSaved.getJobNumber().trim().length() <= 0) {
			msg = CustomMessage.getMessage(CustomMessage.ORDER_JOB_NO_IS_NOT_VALID, null);
		} else if (toBeSaved.getWidth() == 0) {
			msg = CustomMessage.getMessage(CustomMessage.ORDER_WIDTH_IS_NOT_VALID, null);
		} else if (toBeSaved.getDepth() == 0) {
			msg = CustomMessage.getMessage(CustomMessage.ORDER_DEPTH_IS_NOT_VALID, null);
		} else if (toBeSaved.getHeight() == 0) {
			msg = CustomMessage.getMessage(CustomMessage.ORDER_HEIGHT_IS_NOT_VALID, null);
		} else if (toBeSaved.getQuantity() == 0) {
			msg = CustomMessage.getMessage(CustomMessage.ORDER_QUANTITY_IS_NOT_VALID, null);
		} else if (toBeSaved.getProductType() == null || toBeSaved.getProductType().trim().length() <= 0) {
			msg = CustomMessage.getMessage(CustomMessage.ORDER_PRODUCT_IS_NOT_VALID, null);
		} /*
			 * else if (toBeSaved.getPrice() == 0) { msg =
			 * CustomMessage.getMessage(CustomMessage.ORDER_PRICE_IS_NOT_VALID, null); }
			 */
		if (!msg.equals("")) {
			throw exceptionFactory.createException(ErrorCode.VALIDATION, msg, null);
		}

	}

}
