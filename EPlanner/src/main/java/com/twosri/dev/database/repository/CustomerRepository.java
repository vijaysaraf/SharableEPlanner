package com.twosri.dev.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.twosri.dev.database.model.CustomerEntity;

public interface CustomerRepository extends MongoRepository<CustomerEntity, String> {

	void delete(CustomerEntity deleted);

	List<CustomerEntity> findAll();

	CustomerEntity findOne(String id);

	CustomerEntity save(CustomerEntity saved);
}
