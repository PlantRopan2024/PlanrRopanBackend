package com.plants.customer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Service.NotifyTemplateService;

@RestController
@RequestMapping("/notifyTemplateCtrl")
public class NotifyTemplateCtrl {
	
	@Autowired
	private NotifyTemplateService notifyTemplateService;

	
	@PostMapping("/addNotifyTemplate")
	public ResponseEntity<Map<String, Object>> addNotify(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.notifyTemplateService.addNotifyTemplate(request);
		return response;
	}
	
	@GetMapping("/ViewNotifyTemplate")
	public ResponseEntity<Map<String, Object>> ViewNotifyTemplate() {
		ResponseEntity<Map<String, Object>> response = this.notifyTemplateService.ViewNotifyTemplate();
		return response;
	}
	
	@PostMapping("/getApprovedNotifyTemplate")
	public ResponseEntity<Map<String, Object>> getApprovedNotifyTemplate(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.notifyTemplateService.getApprovedNotifyTemplate(request);
		return response;
	}
	
	@PostMapping("/getDisApprovedNotifyTemplate")
	public ResponseEntity<Map<String, Object>> getDisApprovedNotifyTemplate(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.notifyTemplateService.getDisApprovedNotifyTemplate(request);
		return response;
	}
	
	@PostMapping("/updateNotifyTemplate")
	public ResponseEntity<Map<String, Object>> updateNotifyTemplate(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> response = this.notifyTemplateService.updateNotifyTemplate(request);
		return response;
	}
}
