package com.twosri.dev.database.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.twosri.dev.database.model.EventEntity;

public interface EventRepository extends MongoRepository<EventEntity, String> {

	void delete(EventEntity deleted);

	List<EventEntity> findAll();

	EventEntity findOne(String id);

	EventEntity save(EventEntity saved);
	
	void deleteByOrderId(String orderId);
	
	List<EventEntity> findByOrderIdOrderByStartDate(String orderId);

	void deleteAll();

	@Query("{'startDate' : {gt : ?0}, endDate' : {$lt : ?0}}")
	List<EventEntity> findAllOrderByStartDate(Date startDate, Date endDate);
	
	
	List<EventEntity> findByStartDateBetween(Date startDate, Date endDate);

}
