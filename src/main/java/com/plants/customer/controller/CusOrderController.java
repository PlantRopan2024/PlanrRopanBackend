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

import com.plants.Dao.CustomerDao;
import com.plants.Dao.userDao;
import com.plants.Service.CustomerService;
import com.plants.Service.LocationService;
import com.plants.config.JwtUtil;

import com.plants.entities.CustomerMain;

import jakarta.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/CusOrderController")
public class CusOrderController {

	@Autowired
	userDao userdao;

	@Autowired
	LocationService locationService;

	@Autowired
	CustomerService customerService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	CustomerDao customerDao;

	@GetMapping("/getActiveOrderCus")
	public ResponseEntity<Map<String, Object>> getActiveOrderCus(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestParam(defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(defaultValue = "10", required = false) Integer pageSize, HttpServletRequest request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);

		if (Objects.isNull(exitsCustomer) || !jwtToken.equals(exitsCustomer.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		// **Construct Dynamic Base URL**
		String baseUrl = request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort())
				+ request.getContextPath() + "/CusOrderController/getActiveOrderCus";
		ResponseEntity<Map<String, Object>> response = customerService.getActiveOrder(exitsCustomer, pageNumber,
				pageSize, baseUrl);
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping("/getListCompletedOrderCus")
	public ResponseEntity<Map<String, Object>> getListCompletedOrderCus(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestParam(defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(defaultValue = "10", required = false) Integer pageSize, HttpServletRequest request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		CustomerMain exitsCustomer = customerDao.findMobileNumber(mobileNumber);

		if (Objects.isNull(exitsCustomer) || !jwtToken.equals(exitsCustomer.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		// **Construct Dynamic Base URL**
		String baseUrl = request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort())
				+ request.getContextPath() + "/CusOrderController/getListCompletedOrderCus";
		ResponseEntity<Map<String, Object>> response = customerService.getCompeletedOrder(exitsCustomer, pageNumber,
				pageSize, baseUrl);
		return ResponseEntity.ok(response.getBody());
	}
	

}
