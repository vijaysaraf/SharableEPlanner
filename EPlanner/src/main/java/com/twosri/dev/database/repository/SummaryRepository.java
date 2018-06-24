package com.twosri.dev.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.twosri.dev.database.model.CalculationEntity;
import com.twosri.dev.database.model.SummaryEntity;

public interface SummaryRepository extends MongoRepository<SummaryEntity, String> {

	void delete(SummaryEntity toBeDeleted);

	List<SummaryEntity> findAll();

	SummaryEntity findOne(String id);

	SummaryEntity save(SummaryEntity saved);

	List<SummaryEntity> save(List<SummaryEntity> saved);

	void deleteAll();
	
	void deleteByOrderId(String orderId);

}
