package com.twosri.dev.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.twosri.dev.bean.Customer;
import com.twosri.dev.bean.RestResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DefaultRestController {


	@PostMapping("/user/loadPO")
	public ResponseEntity<?> getPurchaseOrderDetails(@RequestBody Customer customer) {
		RestResponse restResponse = new RestResponse();
		/*log.info("Customer {} reuested for PO Details ", customer.getName());
		List<PurchaseOrderDetails> orders = store.getPODetails(customer.getName(), customer.getEmail());
		log.info("Customer has {} PO in the box", orders.size());

		if (orders.isEmpty()) {
			restResponse.setErrorMessage("No Purchase order found. Please contact administrator");
		} else {
			restResponse.setInfoMessage("Success");
		}
		restResponse.setObject(orders);
*/
		return ResponseEntity.ok(restResponse);
	}

}
