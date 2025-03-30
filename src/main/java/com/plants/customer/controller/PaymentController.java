package com.plants.customer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.CustomerDao;
import com.plants.Dao.MobileApiDao;
import com.plants.Service.PaymentServices;
import com.plants.config.JwtUtil;
import com.plants.entities.AgentMain;
import com.plants.entities.CustomerMain;
import com.plants.entities.FertilizerRequest;
import com.plants.entities.Order;
import com.plants.entities.OrderSummaryRequest;
import com.plants.entities.PaymentRequest;
import com.twilio.rest.api.v2010.account.call.PaymentUpdater;

@RestController
@RequestMapping("/paymentCont")
public class PaymentController {
	
	@Autowired
	private PaymentServices paymentServices;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private MobileApiDao mobileApiDao;

	
	@PostMapping("/initiatePayment")
	public ResponseEntity<Map<String, Object>> initiatePayment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody PaymentRequest request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);

		if (Objects.isNull(exitsCustomer) || !jwtToken.equals(exitsCustomer.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.paymentInitiate(exitsCustomer,request);
		return ResponseEntity.ok(response.getBody());
	}
	
	@PostMapping("/sendOrderAgent")
	public ResponseEntity<Map<String, Object>> orderAssignedAgent(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, Object> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);

		if (Objects.isNull(exitsCustomer) || !jwtToken.equals(exitsCustomer.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.OrderAssigned(exitsCustomer,request);
		return ResponseEntity.ok(response.getBody());
	}
	
	@PostMapping("/accpetOrRejectOrder")
	public ResponseEntity<Map<String, Object>> accpetOrRejectOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, Object> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.accpetOrRejctOrder(agentRecords,request);
		return ResponseEntity.ok(response.getBody());
	}
	
	
}
