package com.twosri.dev.service.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.twosri.dev.bean.Customer;
import com.twosri.dev.bean.Phase;
import com.twosri.dev.bean.Reference;
import com.twosri.dev.service.impl.CustomerService;
import com.twosri.dev.service.impl.PhaseService;
import com.twosri.dev.service.impl.ReferenceService;
import com.twosri.dev.util.Mode;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Data
public class CacheManager {

	private Map<String, Phase> phaseMap;
	private Map<String, Reference> productMap;
	private Map<String, Customer> customerMap;

	@Autowired
	PhaseService pService;
	@Autowired
	ReferenceService rService;
	@Autowired
	CustomerService cService;

	@EventListener(ApplicationReadyEvent.class)
	public void initCacheManager() {
		log.info("Initializing Cache...");
		List<Phase> phases = pService.findAll();
		phaseMap = new HashMap<String, Phase>();
		phases.forEach(entity -> phaseMap.put(entity.getId(), entity));

		List<Reference> products = rService.findAll();
		productMap = new HashMap<String, Reference>();
		products.forEach(entity -> productMap.put(entity.getId(), entity));

		List<Customer> customers = cService.findAll();
		customerMap = new HashMap<String, Customer>();
		customers.forEach(entity -> customerMap.put(entity.getId(), entity));
		log.info("Cache initalized...");
	}

	public void updatePhases(Mode mode, String id, Phase phase) {
		if (mode == Mode.ADD || mode == Mode.EDIT)
			phaseMap.put(id, phase);
		else if (mode == Mode.DELETE)
			phaseMap.remove(id);

	}

	public void updateProducts(Mode mode, String id, Reference reference) {
		if (mode == Mode.ADD || mode == Mode.EDIT)
			productMap.put(id, reference);
		else if (mode == Mode.DELETE)
			productMap.remove(id);

	}

	public void updateCustomers(Mode mode, String id, Customer customer) {
		if (mode == Mode.ADD || mode == Mode.EDIT)
			customerMap.put(id, customer);
		else if (mode == Mode.DELETE)
			customerMap.remove(id);
	}

	public Phase getPhase(String id) {
		return phaseMap.get(id) != null ? phaseMap.get(id) : new Phase();
	}

	public Reference getProduct(String id) {
		return productMap.get(id) != null ? productMap.get(id) : new Reference();
	}

	public Customer getCustomer(String id) {
		return customerMap.get(id) != null ? customerMap.get(id) : new Customer();
	}

}
