package com.twosri.dev.service;

import java.util.List;

import com.twosri.dev.bean.Reference;

public interface IReferenceService {

	void delete(Reference deleted);

	List<Reference> findAll();

	Reference findOne(String id);

	Reference save(Reference saved);
	
	List<Reference> findByDomain(String domain);
}
