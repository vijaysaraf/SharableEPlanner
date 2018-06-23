package com.twosri.dev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.twosri.dev.util.AppEnum;

@Controller
public class AdminController {

	@GetMapping("/admin/users")
	public String users() {
		return AppEnum._PATH_USER.getValue();
	}

	@GetMapping("/admin/customers")
	public String customers() {
		return AppEnum._PATH_CUSTOMER.getValue();
	}

	@GetMapping("/admin/phases")
	public String phases() {
		return AppEnum._PATH_PHASE.getValue();
	}

	@GetMapping("/admin/references")
	public String references() {
		return AppEnum._PATH_REFERENCE.getValue();
	}

	@GetMapping("/admin/calculations")
	public String calculations() {
		return AppEnum._PATH_CALCULATION.getValue();
	}

	@GetMapping("/admin/orders")
	public String orders() {
		return AppEnum._PATH_ORDER.getValue();
	}
	@GetMapping("/admin/events")
	public String events() {
		return AppEnum._PATH_EVENT.getValue();
	}

	@GetMapping("/admin/events/propose")
	public String proposeEvents() {
		return AppEnum._PATH_PROPOSE_EVENT.getValue();
	}

	@GetMapping("/admin/schedule")
	public String loadEvents() {
		return AppEnum._PATH_SCHEDULE.getValue();
	}

	@GetMapping("/admin/summary")
	public String summary() {
		return AppEnum._PATH_SUMMARY.getValue();
	}

}
