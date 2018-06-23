package com.twosri.dev.service;

import java.util.List;

import com.twosri.dev.bean.Order;

public interface IOrderService {

	void delete(Order deleted);

	List<Order> findAll();

	Order findOne(String id);

	Order save(Order saved);
}
