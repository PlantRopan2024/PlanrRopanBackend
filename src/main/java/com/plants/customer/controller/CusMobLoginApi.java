package com.plants.customer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.CustomerDao;
import com.plants.Service.OTPService;
import com.plants.Service.SmsService;
import com.plants.entities.CustomerMain;
import com.plants.entities.Plans;

@RestController
@RequestMapping("/CusMobLoginApi")
public class CusMobLoginApi {
	@Autowired
	private OTPService otpService;

	@Autowired
	private SmsService smsService;
	@Autowired
	private CustomerDao customerDao;
	 @PostMapping("/sendOTP")
	 public ResponseEntity<Map<String, String>> sendOTP(@RequestBody Map<String, String> request) {     
		 Map<String, String> response = new HashMap<>();  
		 String mobileNumber = request.get("mobileNumber");
	        
	     if (mobileNumber == null || mobileNumber.isEmpty()) {
	         return ResponseEntity.badRequest().body(Map.of("error", "Mobile number is required"));
	     }
	     String otp = otpService.generateOTP(mobileNumber);

	     smsService.sendOtp(mobileNumber, otp);
	    
	     response.put("message", "OTP sent successfully!");
	    
	     return ResponseEntity.ok(response);
	 }
	 @PostMapping("/verifyOTP")
	    public ResponseEntity<Map<String, Object>> verifyOTP(@RequestBody Map<String, String> request) {
		 Map<String, Object> response = new HashMap<>();  
	        String mobileNumber = request.get("mobileNumber");
	        String otp = request.get("otp");
	        List<CustomerMain>findMob=customerDao.findMobileNumber(mobileNumber);

	        if (mobileNumber == null || mobileNumber.isEmpty() || otp == null || otp.isEmpty()) {
	            return ResponseEntity.badRequest().body(Map.of("error","Mobile number and OTP are required"));
	        }

	        boolean isOtpValid = otpService.verifyOtp(mobileNumber, otp);
	        
	        List<Plans> getPlans = this.customerDao.getallPlans();
		    List<Plans> activePlans = new ArrayList<>();
		    for (Plans pl : getPlans) {
		        if (pl.getIsActive().equals("Yes")) {
		            activePlans.add(pl);
		        }
		    }
		   
	        
	        
	        if (isOtpValid) {
	        	if(findMob.isEmpty()) {
	        		response.put("CustomerExit", "false");
	        		response.put("message", "OTP Verified Successfully");
	        	}
	        	else {
	        		response.put("Object", findMob);
	        		response.put("All Plans", activePlans);
	        		response.put("CustomerExit", "true");
	        		response.put("message", "OTP Verified Successfully");
	        	}
	            return ResponseEntity.ok(response);
	        } else {
	        	response.put("message", "Invalid or Expired OTP");
	        	return ResponseEntity.ok(response);
	        }
	    }

}
