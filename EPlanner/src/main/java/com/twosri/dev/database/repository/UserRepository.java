package com.twosri.dev.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.twosri.dev.database.model.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, String> {
	
	void delete(UserEntity deleted);

	List<UserEntity> findAll();

	UserEntity findOne(String id);

	UserEntity save(UserEntity saved);
}
