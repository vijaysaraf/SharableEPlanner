package com.twosri.dev.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.twosri.dev.bean.Customer;
import com.twosri.dev.database.model.CustomerEntity;
import com.twosri.dev.database.repository.CustomerRepository;
import com.twosri.dev.service.ICustomerService;
import com.twosri.dev.service.cache.CacheManager;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ErrorCode;
import com.twosri.dev.util.ExceptionFactory;
import com.twosri.dev.util.Mode;

@Service
public class CustomerService implements ICustomerService {

	@Autowired
	CustomerRepository repository;

	@Autowired
	ModelMapper mMapper;

	@Autowired
	ExceptionFactory exceptionFactory;
	
	@Autowired
	CacheManager cacheManager;

	@Override
	public void delete(Customer deleted) {
		try {
			repository.delete(deleted.getId());
			cacheManager.updateCustomers(Mode.DELETE, deleted.getId(), null);
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	@Override
	public List<Customer> findAll() {
		List<CustomerEntity> entityList = repository.findAll();
		return entityList.stream().map(entity -> mMapper.map(entity, Customer.class)).collect(Collectors.toList());
	}

	@Override
	public Customer findOne(String id) {
		CustomerEntity entity = repository.findOne(id);
		return mMapper.map(entity, Customer.class);
	}

	@Override
	public Customer save(Customer toBeSaved) {
		validateCustomer(toBeSaved);
		try {
			CustomerEntity entity = repository.save(mMapper.map(toBeSaved, CustomerEntity.class));
			cacheManager.updateCustomers(Mode.ADD, entity.getId(), toBeSaved);
			return mMapper.map(entity, Customer.class);
		} catch (DuplicateKeyException e) {
			throw exceptionFactory.createException(ErrorCode.VALIDATION, CustomMessage
					.getMessage(CustomMessage.CUSTOMER_ALREADY_EXIST, new String[] { toBeSaved.getName() }), e);
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	private void validateCustomer(Customer toBeSaved) {
		if (toBeSaved.getName() == null || toBeSaved.getName().trim().length() <= 0) {
			throw exceptionFactory.createException(ErrorCode.VALIDATION, CustomMessage
					.getMessage(CustomMessage.CUSTOMER_NAME_IS_NOT_VALID, new String[] { toBeSaved.getName() }), null);
		}

	}

}
