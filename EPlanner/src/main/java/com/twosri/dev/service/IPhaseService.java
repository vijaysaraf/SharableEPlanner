package com.twosri.dev.service;

import java.util.List;

import com.twosri.dev.bean.Phase;

public interface IPhaseService {

	void delete(Phase deleted);

	List<Phase> findAll();

	Phase findOne(String id);

	Phase save(Phase saved);
	
	String[] getPhaseNames();
}
