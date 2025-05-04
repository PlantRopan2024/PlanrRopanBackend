package com.plants.customer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Service.PlansServices;

@RestController
@RequestMapping("/includingServiceName")
public class IncludingServiceCont {
	
	@Autowired
	PlansServices plansServices;
	
	@GetMapping("/viewIncludingServiceMain")
	public ResponseEntity<Map<String, Object>> viewIncludingServiceMain() {
		ResponseEntity<Map<String, Object>> response = this.plansServices.viewIncludServiceMain();
		return response;
	}
	
	@GetMapping("/getActiveIncludingServiceMain")
	public ResponseEntity<Map<String, Object>> getActiveIncludingServiceMain() {
		ResponseEntity<Map<String, Object>> response = this.plansServices.getActiveIncludingService();
		return response;
	}
	
	@PostMapping("/addIncludingServiceMain")
	public ResponseEntity<Map<String, Object>> addIncludingService(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.addIncludingService(request);
		return response;
	}
	
	@PostMapping("/includingServiceMainActive")
	public ResponseEntity<Map<String, Object>> includingServiceMainActive(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.includingServiceActive(request);
		return response;
	}
	
	@PostMapping("/includingServiceMainDisActive")
	public ResponseEntity<Map<String, Object>> includingServiceMainDisActive(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.includingServiceDisActive(request);
		return response;
	}
	
	@PostMapping("/getApprovedPlans")
	public ResponseEntity<Map<String, Object>> getApprovedPlans(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.getApprovedPlans(request);
		return response;
	}
	
	@PostMapping("/getDisApprovedPlans")
	public ResponseEntity<Map<String, Object>> getDisApprovedPlans(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.getDisApprovedPlans(request);
		return response;
	}
	
}
