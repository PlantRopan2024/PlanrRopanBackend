package com.plants.customer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plants.Dao.CustomerDao;
import com.plants.Service.CustomerService;
import com.plants.Service.OTPService;
import com.plants.Service.SmsService;
import com.plants.config.JwtUtil;
import com.plants.config.MessageService;
import com.plants.entities.AgentMain;
import com.plants.entities.BookingRequest;
import com.plants.entities.CustomerMain;
import com.plants.entities.Plans;

@RestController
@RequestMapping("/CusMobLoginApi")
public class CusMobLoginApi {

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	@GetMapping("/getallDetailCustomer")
	public ResponseEntity<Map<String, Object>> getAllDetailAgent(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
	        String mobileNumber = jwtUtil.extractUsername(jwtToken);
			CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);
	        // Validate token
	        if (exitsCustomer == null || !jwtToken.equals(exitsCustomer.getToken())) {
	            response.put("error", "Invalid or expired token");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	        
	        
	        Map<String, Object> data = new HashMap<>();
			data.put("primarykey", exitsCustomer.getPrimarykey());
			data.put("firstName", exitsCustomer.getFirstName());
			data.put("lastName", exitsCustomer.getLastName());
			data.put("emailId", exitsCustomer.getEmailId());
			data.put("mobileNumber", exitsCustomer.getMobileNumber());
			data.put("address", exitsCustomer.getAddress());
			data.put("city", exitsCustomer.getCity());
			data.put("latitude", exitsCustomer.getLatitude());
			data.put("loggitude", exitsCustomer.getLoggitude());
			data.put("token", exitsCustomer.getToken());
			data.put("firebasetokenCus", exitsCustomer.getFirebasetokenCus());
			data.put("profileCompleted", exitsCustomer.isProfileCompleted());
			
			response.put("data", data);
			return ResponseEntity.ok(response);

	  	    } catch (Exception e) {
	        response.put("error", "An unexpected error occurred");
	        response.put("details", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	@PostMapping("/sendOTP")
	public ResponseEntity<Map<String, String>> sendOtp(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, String>> otpResponse = this.customerService.sentOtpCus(request.get("mobileNumber"));
		return otpResponse;
	}
	
	@PostMapping("/verifyOTP")
	public ResponseEntity<Map<String, Object>> verifyOTP(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> validVerify = this.customerService.verifiedOtpDetailCustomer(request.get("mobileNumber") ,request.get("otp") );
		return validVerify;
	}
	
	@PostMapping(value ="/profileInfoCustomer")
	public ResponseEntity<Map<String, Object>> profileInfoDetailsCust(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CustomerMain RequestcustomerMain){
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);		
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);
	    if (Objects.isNull(exitsCustomer)   || !jwtToken.equals(exitsCustomer.getToken())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
	    }
		ResponseEntity<Map<String, Object>> getprofileDetails = customerService.ProfileInfoSaveCustomer(exitsCustomer ,RequestcustomerMain);
		return ResponseEntity.ok(getprofileDetails.getBody());
	}
	
	@PostMapping("/getliveLocationUpdateCoustomer")
	public ResponseEntity<Map<String, Object>> getliveLocationLatiAndLong(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, String> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);

		if (Objects.isNull(exitsCustomer) || !jwtToken.equals(exitsCustomer.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = customerService.getUpdateLiveLocationCust(exitsCustomer ,request);
		return ResponseEntity.ok(response.getBody());
	}
	
	@PostMapping("/getCusFirebaseTokenDevice")
	public ResponseEntity<Map<String, String>> getFirebaseTokenDevice(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, String> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);

		if (Objects.isNull(exitsCustomer) || !jwtToken.equals(exitsCustomer.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, String>> response = customerService.getFirebaseDeviceToken(exitsCustomer,request);
		return ResponseEntity.ok(response.getBody());
	}
	
	@PostMapping("/OrderSummaryPage") 
	public ResponseEntity<?> OrderSummaryPage(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody BookingRequest bookingRequest) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);

		if (Objects.isNull(exitsCustomer) || !jwtToken.equals(exitsCustomer.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		
		ResponseEntity<Map<String, Object>> response = customerService.orderSummaryCalculation(exitsCustomer,bookingRequest);
		return ResponseEntity.ok(response.getBody());
	}
	
	@PostMapping("/applyOffersCust") 
	public ResponseEntity<?> applyOffersCust(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, Object> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);

		if (Objects.isNull(exitsCustomer) || !jwtToken.equals(exitsCustomer.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		
		ResponseEntity<Map<String, Object>> response = customerService.applyOffersCustomer(exitsCustomer,request);
		return ResponseEntity.ok(response.getBody());
	}
	
}
