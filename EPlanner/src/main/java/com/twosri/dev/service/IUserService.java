package com.twosri.dev.service;

import java.util.List;

import com.twosri.dev.bean.User;

public interface IUserService {

	void delete(User deleted);

	List<User> findAll();

	User findOne(String id);

	User save(User saved);
}
