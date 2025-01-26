package com.plants.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.CustomerDao;
import com.plants.Dao.serviceNameDao;
import com.plants.Dao.userDao;
import com.plants.entities.AgentMain;
import com.plants.entities.Plans;
import com.plants.entities.serviceName;

@RestController
@RequestMapping("/ShowPlans")
public class PlansAdd {
	
	 @Autowired
	 userDao userdao;
	 
	 @Autowired
	 private CustomerDao customerDao;
	 
	 @Autowired
	 private serviceNameDao serviceNameDao;
	 
	 String serviceKey;
	 
	 @PostMapping("/addPlans")
	 @ResponseBody
	 public Map<String, String> addPlan(@ModelAttribute Plans plans) {
	     Map<String, String> response = new HashMap<>();
	     try {
	         Plans savedPlan = this.userdao.save(plans);
	         response.put("message", "Plans Add Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         response.put("message", "Error while adding plans");
	     }
	     return response;
	 }
	
	  @GetMapping("/getPlans")
	   public ResponseEntity<HashMap<String, Object>> getAllPlans() {
	       HashMap<String, Object> response = new HashMap<>();
	       List<serviceName> getPlans = this.serviceNameDao.getallPlans();
	       for (serviceName s : getPlans) {
	           List<Plans> activePlans = new ArrayList<>();
	           for (Plans p : s.getPlans()) {
	               if (p.getIsActive().equals("Yes")) {
	                   activePlans.add(p);
	               }
	           }
	           response.put(s.getName(), activePlans);
	       }

	       return ResponseEntity.ok(response);
	   }
		
		@GetMapping("/monthWiseRecordFetch")
		@ResponseBody
		public List<Plans> monthWiseRecordFetch() {
			List<Plans> getmonthPlans= this.customerDao.getMonthVSDailyfetch("MONTHLY");
			return getmonthPlans;
		}
		
		@GetMapping("/dailyRecordFetch")
		@ResponseBody
		public List<Plans> dailyRecordFetch() {
			List<Plans> getdailyPlans= this.customerDao.getMonthVSDailyfetch("DAILY");
			return getdailyPlans;
		}
}
