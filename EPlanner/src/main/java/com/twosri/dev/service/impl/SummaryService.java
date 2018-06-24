package com.twosri.dev.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twosri.dev.bean.Order;
import com.twosri.dev.bean.Summary;
import com.twosri.dev.database.model.SummaryEntity;
import com.twosri.dev.database.repository.SummaryRepository;
import com.twosri.dev.service.ISummaryService;
import com.twosri.dev.service.cache.CacheManager;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ErrorCode;
import com.twosri.dev.util.ExceptionFactory;

@Service
public class SummaryService implements ISummaryService {

	@Autowired
	SummaryRepository repository;

	@Autowired
	ModelMapper mMapper;

	@Autowired
	ExceptionFactory exceptionFactory;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	CacheManager cacheManager;
	
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

	@Override
	public void delete(Summary deleted) {
		try {
			repository.delete(deleted.getId());
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}
	@Override
	public void deleteAll() {
		repository.deleteAll();

	}
	@Override
	public void deleteByOrderId(String orderId) {
		repository.deleteByOrderId(orderId);
	}

	@Override
	public List<Summary> findAll() {
		List<SummaryEntity> entityList = repository.findAll();
		return mapFromEntity(entityList);
	}

	private List<Summary> mapFromEntity(List<SummaryEntity> entityList) {
		List<Summary> summaryList = new ArrayList<>();
		for(int i=0;i<entityList.size();i++) {
			SummaryEntity entity = entityList.get(i);
			Order order = orderService.findOne(entity.getOrderId());
			Summary summary = new Summary();
			summary.setOrderNumber(order.getOrderNumber());
			summary.setJobNumber(order.getJobNumber());
			summary.setDesignId(order.getDesignId());
			summary.setNew(order.isNew());
			summary.setWidth(order.getWidth());
			summary.setHeight(order.getHeight());
			summary.setDepth(order.getDepth());
			summary.setQuantity(order.getQuantity());
			summary.setPhaseName(cacheManager.getPhase(entity.getPhaseId()).getName());
			summary.setProductName(cacheManager.getProduct(order.getProductType()).getName());
			summary.setDisplayStartDate(dateFormat.format(entity.getStartDate()));
			summary.setDisplayEndDate(dateFormat.format(entity.getEndDate()));
			summary.setHrsRequired(entity.getHrsRequired());
			summaryList.add(summary);
		}
		return summaryList;/*entityList.stream().map(entity -> {
			
			return summary;
		}).collect(Collectors.toList());*/
	}
	
	

	@Override
	public Summary findOne(String id) {
		SummaryEntity entity = repository.findOne(id);
		return mMapper.map(entity, Summary.class);
	}

	@Override
	public Summary save(Summary toBeSaved) {
		validateSummary(toBeSaved);
		try {
			SummaryEntity entity = repository.save(mMapper.map(toBeSaved, SummaryEntity.class));
			return mMapper.map(entity, Summary.class);
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	@Override
	public List<Summary> save(List<Summary> toBeSaved) {
		toBeSaved.forEach(summary -> validateSummary(summary));
		try {
			List<SummaryEntity> entityList = repository.save(toBeSaved.stream()
					.map(summary -> mMapper.map(summary, SummaryEntity.class)).collect(Collectors.toList()));
			return entityList.stream().map(entity -> mMapper.map(entity, Summary.class)).collect(Collectors.toList());
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	public SummaryEntity save(SummaryEntity toBeSaved) {
		return repository.save(toBeSaved);
	}

	private void validateSummary(Summary toBeSaved) {
	}
}
