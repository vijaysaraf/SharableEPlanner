package com.twosri.dev.service;

import java.util.List;

import com.twosri.dev.bean.Customer;

public interface ICustomerService {

	void delete(Customer deleted);

	List<Customer> findAll();

	Customer findOne(String id);

	Customer save(Customer saved);
}
