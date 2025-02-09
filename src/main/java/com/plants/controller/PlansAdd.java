package com.plants.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.CustomerDao;
import com.plants.Dao.OfferDao;
import com.plants.Dao.serviceNameDao;
import com.plants.Dao.userDao;
import com.plants.entities.AgentMain;
import com.plants.entities.Offers;
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
	
	@Autowired
	private OfferDao offerdao;

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
	
	@GetMapping("/getServiceName")
	public ResponseEntity<?> getServiceNameList() {
		List<serviceName> getListServiceName = this.serviceNameDao.getallService();
		if(getListServiceName.isEmpty()) {
			return ResponseEntity.ok("No Service Found");
		}else {
			return ResponseEntity.ok(getListServiceName);
		}
	}
	
	@GetMapping("/getDashboardDataUser")
	public ResponseEntity<Map<String, Object>> getDashboardDataUser() {
	    Map<String, Object> response = new HashMap<>();

	    List<serviceName> serviceList = this.serviceNameDao.getallService();
	    List<Offers> offerList = this.offerdao.getListActiveOffer();

	    List<Map<String, Object>> filteredServices = serviceList.stream().map(service -> {
	        Map<String, Object> serviceMap = new HashMap<>();
	        serviceMap.put("primaryKey", service.getPrimaryKey());
	        serviceMap.put("name", service.getName());
	        serviceMap.put("active", service.isActive());
	        return serviceMap;
	    }).collect(Collectors.toList());

	    response.put("serviceName", filteredServices);
	    response.put("offerData", offerList);

	    if (filteredServices.isEmpty()) {
	        return ResponseEntity.ok(Collections.singletonMap("message", "No Data Found"));
	    } else {
	        return ResponseEntity.ok(response);
	    }
	}


	
	
	@GetMapping("/getServiceIdPlan/{id}") 
	public ResponseEntity<Map<String, Object>> getServiceIdPlan(@PathVariable String id) {
	    List<Plans> getPlanId = this.customerDao.getPlansListId(id);

	    Map<String, Object> response = new HashMap<>();
	    List<Plans> dailyPlans = getPlanId.stream()
	        .filter(plan -> "DAILY".equalsIgnoreCase(plan.getPlanPacks()))
	        .collect(Collectors.toList());

	    List<Plans> monthlyPlans = getPlanId.stream()
	        .filter(plan -> "MONTHLY".equalsIgnoreCase(plan.getPlanPacks()))
	        .collect(Collectors.toList());
	    
	    response.put("AllPlans", getPlanId);
	    response.put("DailyPlan", dailyPlans);
	    response.put("Monthly", monthlyPlans);

	    if (dailyPlans.isEmpty() && monthlyPlans.isEmpty()) {
	        return ResponseEntity.ok(Collections.singletonMap("message", "No Plans Found"));
	    } else {
	        return ResponseEntity.ok(response);
	    }
	}

	
	@GetMapping("/getPlanID/{id}") 
	public ResponseEntity<?> getPlanID(@PathVariable String id) {
		Plans getPlan = this.customerDao.getPlansId(id);
		if(Objects.isNull(getPlan)) {
			return ResponseEntity.ok("No Plans Found");
		}else {
			return ResponseEntity.ok(getPlan);
		}
	}
	
	@GetMapping("/getPlans")
	public ResponseEntity<HashMap<String, Object>> getAllPlans() {
		HashMap<String, Object> response = new HashMap<>();
		List<serviceName> getPlans = this.serviceNameDao.getallPlans();
		for (serviceName s : getPlans) {
			List<Plans> activePlans = new ArrayList<>();
			for (Plans p : s.getPlans()) {
				if (p.getIsActive()) {
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
		List<Plans> getmonthPlans = this.customerDao.getMonthVSDailyfetch("MONTHLY");
		return getmonthPlans;
	}

	@GetMapping("/dailyRecordFetch")
	@ResponseBody
	public List<Plans> dailyRecordFetch() {
		List<Plans> getdailyPlans = this.customerDao.getMonthVSDailyfetch("DAILY");
		return getdailyPlans;
	}
}
