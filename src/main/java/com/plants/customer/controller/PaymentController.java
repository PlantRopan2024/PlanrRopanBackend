package com.plants.customer.controller;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plants.Dao.CustomerDao;
import com.plants.Dao.MobileApiDao;
import com.plants.Service.PaymentServices;
import com.plants.config.HelperToken;
import com.plants.config.JwtUtil;
import com.plants.entities.AgentMain;
import com.plants.entities.CustomerMain;
import com.plants.entities.PaymentRequest;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;


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
	private HelperToken helperToken;

	@Autowired
	private MobileApiDao mobileApiDao;

	@PostMapping("/initiatePayment")
	public ResponseEntity<Map<String, Object>> initiatePayment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestBody PaymentRequest request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);

		if (Objects.isNull(exitsCustomer) || !jwtToken.equals(exitsCustomer.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.paymentInitiate(exitsCustomer, request);
		return ResponseEntity.ok(response.getBody());
	}

	/*@PostMapping("/sendOrderNotifactionAgent")
	public ResponseEntity<Map<String, Object>> sendOrderNotifactionAgent(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, Object> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);

		if (Objects.isNull(exitsCustomer) || !jwtToken.equals(exitsCustomer.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.OrderAssignedNotify(exitsCustomer, request);
		return ResponseEntity.ok(response.getBody());
	} */
	
	@GetMapping("upComingOrders")
	public ResponseEntity<Map<String, Object>> upComingOrders(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestParam(defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(defaultValue = "10" ,required = false) Integer pageSize ,HttpServletRequest request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		
		 String baseUrl = request.getScheme() + "://" + request.getServerName() +
		            (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +request.getContextPath() + "/paymentCont/upComingOrders";
		ResponseEntity<Map<String, Object>> response = paymentServices.upComingOrders(agentRecords, pageNumber, pageSize, baseUrl);
		return ResponseEntity.ok(response.getBody());
	}

	@PostMapping("/checkOrderAgent")
	public ResponseEntity<Map<String, Object>> checkOrderAgent(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestBody Map<String, Object> request
			,HttpServletRequest requestUrl) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);

		if (Objects.isNull(exitsCustomer) || !jwtToken.equals(exitsCustomer.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.checkOrderAssigned(exitsCustomer, request,requestUrl);
		return ResponseEntity.ok(response.getBody());
	}
	

	@PostMapping("/accpetedOrder")
	public ResponseEntity<Map<String, Object>> accpetedOrder(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, Object> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.accpetedOrder(agentRecords, request);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping("/getListAccpetedOrderList")
	public ResponseEntity<Map<String, Object>> getListAccpetedOrderList(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestParam(defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(defaultValue = "10" ,required = false) Integer pageSize ,HttpServletRequest request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		 // **Construct Dynamic Base URL**
	    String baseUrl = request.getScheme() + "://" + request.getServerName() +
	            (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
	            request.getContextPath() + "/paymentCont/getListAccpetedOrderList";
	    ResponseEntity<Map<String, Object>> response = paymentServices.getListAccpetOrder(agentRecords, pageNumber, pageSize, baseUrl);
		return ResponseEntity.ok(response.getBody());
	}
	
	@PostMapping("/viewOrderDetails")
	public ResponseEntity<Map<String, Object>> viewOrderDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, Object> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.viewDetailOrders(agentRecords, request);
		return ResponseEntity.ok(response.getBody());
	}
	
	@PostMapping("/rejectedOrders")
	public ResponseEntity<Map<String, Object>> rejectedOrders(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, Object> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.rejectedOrders(agentRecords, request);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping("/getRejectedOrderList")
	public ResponseEntity<Map<String, Object>> getRejectedOrderList(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestParam(defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(defaultValue = "10" ,required = false) Integer pageSize ,HttpServletRequest request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		 // **Construct Dynamic Base URL**
	    String baseUrl = request.getScheme() + "://" + request.getServerName() +
	            (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
	            request.getContextPath() + "/paymentCont/getRejectedOrderList";
	    ResponseEntity<Map<String, Object>> response = paymentServices.getRejectedOrderList(agentRecords, pageNumber, pageSize, baseUrl);
		return ResponseEntity.ok(response.getBody());
	}
	
	@PostMapping("/wayToLocation")
	public ResponseEntity<Map<String, Object>> wayToLocation(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, Object> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.wayToLocation(agentRecords, request);
		return ResponseEntity.ok(response.getBody());
	}
	
	@PostMapping("/reachedLocationAgent")
	public ResponseEntity<Map<String, Object>> reachedLocationAgent(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, Object> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.reachedLocation(agentRecords, request);
		return ResponseEntity.ok(response.getBody());
	}

	@PostMapping("/OtpCodeShared")
	public ResponseEntity<Map<String, Object>> OtpCodeShared(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestBody Map<String, Object> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.OtpCodeShared(agentRecords, request);
		return ResponseEntity.ok(response.getBody());
	}
	
	@PostMapping("/endTimeWork")
	public ResponseEntity<Map<String, Object>> endTimeWork(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestBody Map<String, Object> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> response = paymentServices.endTimeWork(agentRecords, request);
		return ResponseEntity.ok(response.getBody());
	}

	@PostMapping(value = "/workCompletedPhotoUpload", consumes = "multipart/form-data")
	public ResponseEntity<Map<String, Object>> workCompletedPhotoUpload(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestPart("OrderNumber") String OrderNumber,
			@RequestPart(value = "workCompletdPhoto1", required = false) MultipartFile workCompletdPhoto1,
			@RequestPart(value = "workCompletdPhoto2", required = false) MultipartFile workCompletdPhoto2) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}

		ResponseEntity<Map<String, Object>> getprofileDetails = paymentServices.workPhotoUpload(agentRecords,
				OrderNumber, workCompletdPhoto1, workCompletdPhoto2);
		return ResponseEntity.ok(getprofileDetails.getBody());
	}
	
	@GetMapping("/getCompletedOrderList")
	public ResponseEntity<Map<String, Object>> getCompletedOrderList(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestParam(defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(defaultValue = "10" ,required = false) Integer pageSize ,HttpServletRequest request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		 // **Construct Dynamic Base URL**
	    String baseUrl = request.getScheme() + "://" + request.getServerName() +
	            (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
	            request.getContextPath() + "/paymentCont/getCompletedOrderList";
	    ResponseEntity<Map<String, Object>> response = paymentServices.getCompletedOrder(agentRecords, pageNumber, pageSize, baseUrl);
		return ResponseEntity.ok(response.getBody());
	}
	
	@PostMapping("/OurServicesRating")
	public ResponseEntity<Map<String, Object>> OurServicesRating(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, Object> request) {
		CustomerMain existingCustomer = this.helperToken.validateCustomerToken(token);
	    if (existingCustomer == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
	    }

		ResponseEntity<Map<String, Object>> getprofileDetails = paymentServices.ourServiceRating(existingCustomer,request);
		return ResponseEntity.ok(getprofileDetails.getBody());
	}

}
