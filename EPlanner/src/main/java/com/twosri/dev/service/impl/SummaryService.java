package com.twosri.dev.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.twosri.dev.bean.Summary;
import com.twosri.dev.database.model.SummaryEntity;
import com.twosri.dev.database.repository.SummaryRepository;
import com.twosri.dev.service.ISummaryService;
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
	public List<Summary> findAll() {
		List<SummaryEntity> entityList = repository.findAll();
		return entityList.stream().map(entity -> mMapper.map(entity, Summary.class)).collect(Collectors.toList());
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

	private void validateSummary(Summary toBeSaved) {
	}
}
