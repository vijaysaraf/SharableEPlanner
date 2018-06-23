package com.twosri.dev.service;

import java.util.List;

import com.twosri.dev.bean.Summary;

public interface ISummaryService {

	void delete(Summary deleted);

	List<Summary> findAll();

	Summary findOne(String id);

	Summary save(Summary saved);
}
