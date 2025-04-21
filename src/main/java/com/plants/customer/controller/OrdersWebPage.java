package com.plants.customer.controller;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Service.OrdersWebPageService;
import com.plants.Service.PaymentServices;
import com.plants.entities.AgentMain;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/orderWebPage")
public class OrdersWebPage {
	
	@Autowired
	private PaymentServices paymentServices;
	
	@Autowired
	private OrdersWebPageService ordersWebPageService;
	
	@GetMapping("upComingOrders")
	public ResponseEntity<Map<String, Object>> upComingOrders(
			@RequestParam(defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(defaultValue = "10" ,required = false) Integer pageSize ,HttpServletRequest request) {
		AgentMain agentRecords = null;
		
		 String baseUrl = request.getScheme() + "://" + request.getServerName() +
		            (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +request.getContextPath() + "/paymentCont/upComingOrders";
		ResponseEntity<Map<String, Object>> response = paymentServices.upComingOrders(agentRecords, pageNumber, pageSize, baseUrl);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping("/getListAccpetedOrderList")
	public ResponseEntity<Map<String, Object>> getListAccpetedOrderList(
			@RequestParam(defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(defaultValue = "10" ,required = false) Integer pageSize ,HttpServletRequest request) {

	
		 // **Construct Dynamic Base URL**
	    String baseUrl = request.getScheme() + "://" + request.getServerName() +
	            (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
	            request.getContextPath() + "/paymentCont/getListAccpetedOrderList";
	    ResponseEntity<Map<String, Object>> response = ordersWebPageService.getListAccpetOrder(pageNumber, pageSize, baseUrl);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping("/getCompletedOrderList")
	public ResponseEntity<Map<String, Object>> getCompletedOrderList(
			@RequestParam(defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(defaultValue = "10" ,required = false) Integer pageSize ,HttpServletRequest request) {

		
		 // **Construct Dynamic Base URL**
	    String baseUrl = request.getScheme() + "://" + request.getServerName() +
	            (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
	            request.getContextPath() + "/paymentCont/getCompletedOrderList";
	    ResponseEntity<Map<String, Object>> response = ordersWebPageService.getCompletedOrder(pageNumber, pageSize, baseUrl);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping("/getRejectedOrderList")
	public ResponseEntity<Map<String, Object>> getRejectedOrderList(
			@RequestParam(defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(defaultValue = "10" ,required = false) Integer pageSize ,HttpServletRequest request) {

	
		 // **Construct Dynamic Base URL**
	    String baseUrl = request.getScheme() + "://" + request.getServerName() +
	            (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
	            request.getContextPath() + "/paymentCont/getRejectedOrderList";
	    ResponseEntity<Map<String, Object>> response = ordersWebPageService.getRejectedOrderList(pageNumber, pageSize, baseUrl);
		return ResponseEntity.ok(response.getBody());
	}
}
