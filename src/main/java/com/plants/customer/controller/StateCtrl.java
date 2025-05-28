package com.plants.customer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Service.StateService;

@RestController
@RequestMapping("/stateCtrl")
public class StateCtrl {
	
	@Autowired
	StateService stateService;
	
	@PostMapping("/addState")
	public ResponseEntity<Map<String, Object>> addState(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.stateService.addState(request);
		return response;
	}
	
	@PostMapping("/updateState")
	public ResponseEntity<Map<String, Object>> updateState(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.stateService.updateState(request);
		return response;
	}
	
	@GetMapping("/ViewStateList")
	public ResponseEntity<Map<String, Object>> ViewStateList() {
		ResponseEntity<Map<String, Object>> response = this.stateService.ViewStateList();
		return response;
	}
	
	@PostMapping("/getApprovedState")
	public ResponseEntity<Map<String, Object>> getApprovedState(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.stateService.getApprovedState(request);
		return response;
	}
	
	@PostMapping("/getDisApprovedState")
	public ResponseEntity<Map<String, Object>> getDisApprovedState(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.stateService.getDisApprovedState(request);
		return response;
	}
	
	@GetMapping("/stateByCity/{id}")
	public ResponseEntity<Map<String, Object>> serviceByPlanPk(@PathVariable int id) {
		ResponseEntity<Map<String, Object>> response = this.stateService.stateByGetCity(id);
		return response;
	}
	
	@PostMapping("/addCity")
	public ResponseEntity<Map<String, Object>> addCity(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.stateService.addCity(request);
		return response;
	}
	
	@PostMapping("/updateCity")
	public ResponseEntity<Map<String, Object>> updateCity(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.stateService.updateCity(request);
		return response;
	}
	
	@PostMapping("/getApprovedCity")
	public ResponseEntity<Map<String, Object>> getApprovedCity(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.stateService.getApprovedCity(request);
		return response;
	}
	
	@PostMapping("/getDisApprovedCity")
	public ResponseEntity<Map<String, Object>> getDisApprovedCity(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.stateService.getDisApprovedCity(request);
		return response;
	}
	
	@GetMapping("/getStateList")
	public ResponseEntity<Map<String, Object>> getStateList() {
		ResponseEntity<Map<String, Object>> response = this.stateService.getStateList();
		return response;
	}
	
	@GetMapping("/getCityList/{id}")
	public ResponseEntity<Map<String, Object>> getCityList(@PathVariable int id) {
		ResponseEntity<Map<String, Object>> response = this.stateService.getCityList(id);
		return response;
	}
}
