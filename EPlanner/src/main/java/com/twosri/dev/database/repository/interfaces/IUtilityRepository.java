package com.twosri.dev.database.repository.interfaces;

import java.util.Date;

import com.twosri.dev.database.model.EventEntity;

public interface IUtilityRepository {
	EventEntity findAnyGreaterThanGivenDate(String phaseId, Date givenDate);

}
