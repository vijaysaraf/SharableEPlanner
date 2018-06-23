package com.twosri.dev.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.twosri.dev.database.model.CalculationEntity;

public interface CalculationRepository extends MongoRepository<CalculationEntity, String> {

	void delete(CalculationEntity deleted);

	List<CalculationEntity> findAll();

	CalculationEntity findOne(String id);

	CalculationEntity save(CalculationEntity saved);

	CalculationEntity findByProductTypeAndPhaseId(String productType, String phaseId);
}
