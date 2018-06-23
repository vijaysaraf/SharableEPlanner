package com.twosri.dev.database.repository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.WriteResult;
import com.twosri.dev.database.model.EventEntity;
import com.twosri.dev.database.repository.interfaces.IUtilityRepository;

@Service
public class UtilityRepository implements IUtilityRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public EventEntity findAnyGreaterThanGivenDate(String phaseId, Date givenDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("phaseId").is(phaseId).and("endDate").gte(givenDate));
		query.with(new Sort(Sort.Direction.DESC, "endDate"));
		query.limit(1);
		EventEntity event = mongoTemplate.findOne(query, EventEntity.class);
		return event;
	}

}
