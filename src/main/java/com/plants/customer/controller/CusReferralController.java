package com.plants.customer.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.CustomerDao;
import com.plants.Service.cusReferralService;
import com.plants.config.JwtUtil;
import com.plants.entities.CustomerMain;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cusReferral")
public class CusReferralController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	cusReferralService cusreferralService;
	
	@PostMapping("/assignReferralCodeCus")
    public ResponseEntity<Map<String, Object>> assignReferralCodeCus(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, String> request) {
		Map<String, Object> response = new HashMap<>();
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);
		if (exitsCustomer == null || !jwtToken.equals(exitsCustomer.getToken())) {
			response.put("error", "Invalid or expired token");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		ResponseEntity<Map<String, Object>> savedReferral =  this.cusreferralService.assignReferralCustomer(exitsCustomer, request);
		return ResponseEntity.ok(savedReferral.getBody());
    }
	
	@GetMapping("/getCusReferralCode")
	public ResponseEntity<Map<String, Object>> getCusReferralCode(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		Map<String, Object> response = new HashMap<>();
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);
		// Validate token
		if (exitsCustomer == null || !jwtToken.equals(exitsCustomer.getToken())) {
			response.put("error", "Invalid or expired token");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		response.put("status", true);
		response.put("ReferralCode", exitsCustomer.getCusReferralCode());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/getWalletHistoryCust")
    public ResponseEntity<Map<String, Object>> getWalletHistoryCust(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
    		@RequestParam(value = "pageNumber",defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(value = "pageSize",defaultValue = "15" ,required = false) Integer pageSize) {
		Map<String, Object> response = new HashMap<>();
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);
		// Validate token
		if (exitsCustomer == null || !jwtToken.equals(exitsCustomer.getToken())) {
			response.put("error", "Invalid or expired token");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		ResponseEntity<Map<String, Object>> wallethist = this.cusreferralService.walletHistoryCust(exitsCustomer,pageNumber,pageSize);
		return ResponseEntity.ok(wallethist.getBody());
    }
	
	
	
	@GetMapping("/getNotificationHistoryCus")
	public ResponseEntity<Map<String, Object>> getNotificationHistoryCus(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestParam(defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(defaultValue = "15" ,required = false) Integer pageSize,HttpServletRequest request) {
	    Map<String, Object> response = new HashMap<>();
	    String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
	    String mobileNumber = jwtUtil.extractUsername(jwtToken);
	    CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);
	    // Validate token
	    if (exitsCustomer == null || !jwtToken.equals(exitsCustomer.getToken())) {
	        response.put("error", "Invalid or expired token");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	    
	 // **Construct Dynamic Base URL**
	 		String baseUrl = request.getScheme() + "://" + request.getServerName()
	 				+ (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort())
	 				+ request.getContextPath() + "/cusReferral/getNotificationHistoryCus";
	    return cusreferralService.getNotificationHistoryCus(exitsCustomer,pageNumber,pageSize,baseUrl);
	}


}
