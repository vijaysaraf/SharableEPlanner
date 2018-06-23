package com.twosri.dev.service;

import java.util.List;

import com.twosri.dev.bean.Calculation;
import com.twosri.dev.database.model.CalculationEntity;

public interface ICalculationService {

	void delete(Calculation deleted);

	List<Calculation> findAll();

	Calculation findOne(String id);

	Calculation save(Calculation saved);
	
	Calculation findByProductTypeAndPhaseId(String productType, String phaseId);
}
