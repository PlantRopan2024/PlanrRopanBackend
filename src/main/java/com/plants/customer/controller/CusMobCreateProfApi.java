package com.plants.customer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.CustomerDao;
import com.plants.customer.Service.CustomerService;
import com.plants.entities.CustomerMain;
import com.plants.entities.Plans;



@RestController
@RequestMapping("/CusMobCreateProfApi")
public class CusMobCreateProfApi {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerDao customerDao;

	@PostMapping("/createProfile")
	public ResponseEntity<HashMap<String, Object>> createProfile(@RequestBody CustomerMain customerMain) {
		HashMap<String, Object> response = new HashMap<>();
		CustomerMain findEmailAndMobileCus = this.customerDao.findEmailAndMobileCus(customerMain.getEmailId(),customerMain.getMobileNumber());
		if (findEmailAndMobileCus != null) {
			if (customerMain.getEmailId().equals(findEmailAndMobileCus.getEmailId())) {
				response.put("message", "Email Already Exit !!");
				return ResponseEntity.ok(response);
			}
		}
		if (findEmailAndMobileCus != null) {
			if (customerMain.getMobileNumber().equals(findEmailAndMobileCus.getMobileNumber())) {
				response.put("message", "Mobile Number Exit !!");
				return ResponseEntity.ok(response);
			}
		}
		CustomerMain saveCustomer = customerService.saveCustomerProfile(customerMain);
		
		List<Plans> getPlans = this.customerDao.getallPlans();
	    List<Plans> activePlans = new ArrayList<>();
	    for (Plans pl : getPlans) {
	        if (pl.getIsActive().equals("Yes")) {
	            activePlans.add(pl);
	        }
	    }
	    
	    response.put("All Plans", activePlans);
		response.put("Create Customer Profile", saveCustomer);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	
	
}
