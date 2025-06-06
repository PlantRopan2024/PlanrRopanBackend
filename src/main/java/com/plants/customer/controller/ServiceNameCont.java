package com.plants.customer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plants.Service.PlansServices;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/serviceNameCont")
public class ServiceNameCont {
	
	@Autowired
	PlansServices plansServices;
	
	@GetMapping("/viewServiceName")
	public ResponseEntity<Map<String, Object>> viewServiceName(HttpServletRequest request) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.viewService(request);
		return response;
	}
	
	@GetMapping("/serviceByPlanPk/{servicePk}")
	public ResponseEntity<Map<String, Object>> serviceByPlanPk(@PathVariable int servicePk) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.serviceByGetPlans(servicePk);
		return response;
	}
	
	@GetMapping("/viewPlansByPk/{planPk}")
	public ResponseEntity<Map<String, Object>> viewPlansByPk(@PathVariable int planPk) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.viewPlansByPk(planPk);
		return response;
	}
	
	@PostMapping("/addServiceName")
	public ResponseEntity<Map<String, Object>> addServiceName(@RequestPart("services") String serviceJson, @RequestPart(value = "serviceImage", required = false) MultipartFile serviceImage) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.addService(serviceJson,serviceImage);
		return response;
	}
	
	@PostMapping("/updateServiceName")
	public ResponseEntity<Map<String, Object>> updateServiceName(@RequestPart("services") String serviceJson, @RequestPart(value = "serviceImage", required = false) MultipartFile serviceImage) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.updateServiceName(serviceJson,serviceImage);
		return response;
	}
	
	@PostMapping("/addPlans")
	public ResponseEntity<Map<String, Object>> addPlan(@RequestParam("plans") String plansJson, @RequestPart(value = "planImage", required = false) MultipartFile planImage) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.addPlans(plansJson,planImage);
		return response;
	}
	
	@PostMapping("/updatePlans")
	public ResponseEntity<Map<String, Object>> updatePlans(@RequestParam("plans") String plansJson, @RequestPart(value = "planImage", required = false) MultipartFile planImage) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.updatePlans(plansJson,planImage);
		return response;
	}
	@PostMapping("/getApprovedServiceName")
	public ResponseEntity<Map<String, Object>> getApprovedServiceName(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.getApprovedServiceName(request);
		return response;
	}
	
	@PostMapping("/getDisApprovedServiceName")
	public ResponseEntity<Map<String, Object>> getDisApprovedServiceName(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.plansServices.getDisApprovedServiceName(request);
		return response;
	}
	
}
