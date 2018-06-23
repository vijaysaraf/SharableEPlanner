package com.twosri.dev.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.twosri.dev.bean.Reference;
import com.twosri.dev.database.model.ReferenceEntity;
import com.twosri.dev.database.repository.ReferenceRepository;
import com.twosri.dev.service.IReferenceService;
import com.twosri.dev.service.cache.CacheManager;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ErrorCode;
import com.twosri.dev.util.ExceptionFactory;
import com.twosri.dev.util.Mode;

@Service
public class ReferenceService implements IReferenceService {

	@Autowired
	ReferenceRepository repository;

	@Autowired
	ModelMapper mMapper;

	@Autowired
	ExceptionFactory exceptionFactory;
	
	@Autowired
	CacheManager cacheManager;

	@Override
	public void delete(Reference deleted) {
		try {
			repository.delete(deleted.getId());
			cacheManager.updateProducts(Mode.DELETE, deleted.getId(), null);
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	@Override
	public List<Reference> findAll() {
		List<ReferenceEntity> entityList = repository.findAll();
		return entityList.stream().map(entity -> mMapper.map(entity, Reference.class)).collect(Collectors.toList());
	}

	@Override
	public Reference findOne(String id) {
		ReferenceEntity entity = repository.findOne(id);
		return mMapper.map(entity, Reference.class);
	}

	@Override
	public Reference save(Reference toBeSaved) {
		validateReference(toBeSaved);
		try {
			ReferenceEntity entity = repository.save(mMapper.map(toBeSaved, ReferenceEntity.class));
			cacheManager.updateProducts(Mode.ADD, entity.getId(), toBeSaved);
			return mMapper.map(entity, Reference.class);
		} catch (DuplicateKeyException e) {
			throw exceptionFactory.createException(ErrorCode.VALIDATION, CustomMessage
					.getMessage(CustomMessage.REFERENCE_ALREADY_EXIST, new String[] { toBeSaved.getName() }), e);
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	private void validateReference(Reference toBeSaved) {
		String msg = "";
		if (toBeSaved.getName() == null || toBeSaved.getName().trim().length() <= 0) {
			msg = CustomMessage.getMessage(CustomMessage.REFERENCE_NAME_IS_NOT_VALID,
					new String[] { toBeSaved.getName() });
		} else if (toBeSaved.getValue() == null || toBeSaved.getValue().trim().length() <= 0) {
			msg = CustomMessage.getMessage(CustomMessage.REFERENCE_VALUE_NOT_VALID,
					new String[] { toBeSaved.getValue() });
		}
		if (!msg.equals("")) {
			throw exceptionFactory.createException(ErrorCode.VALIDATION, msg, null);
		}

	}

	@Override
	public List<Reference> findByDomain(String domain) {

		return null;
	}

}
