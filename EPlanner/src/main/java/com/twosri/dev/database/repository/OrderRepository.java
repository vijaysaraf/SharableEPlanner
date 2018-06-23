package com.twosri.dev.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.twosri.dev.database.model.OrderEntity;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {
	
	void delete(OrderEntity deleted);

	List<OrderEntity> findAll();

	OrderEntity findOne(String id);

	OrderEntity save(OrderEntity saved);
}
