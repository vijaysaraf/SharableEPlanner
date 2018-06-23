package com.twosri.dev.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.twosri.dev.database.model.ReferenceEntity;

public interface ReferenceRepository extends MongoRepository<ReferenceEntity, String> {
	
	void delete(ReferenceEntity deleted);

	List<ReferenceEntity> findAll();

	ReferenceEntity findOne(String id);

	ReferenceEntity save(ReferenceEntity saved);
	
	List<ReferenceEntity> findByDomain(String domain);
	
}
