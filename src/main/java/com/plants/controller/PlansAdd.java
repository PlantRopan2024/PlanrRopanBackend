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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.CustomerDao;
import com.plants.Dao.FertilizerRepo;
import com.plants.Dao.OfferDao;
import com.plants.Dao.serviceNameDao;
import com.plants.Dao.userDao;
import com.plants.Service.PlansServices;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;
import com.plants.entities.Fertilizer;
import com.plants.entities.Offers;
import com.plants.entities.Plans;
import com.plants.entities.PlansDto;
import com.plants.entities.serviceName;

import jakarta.servlet.http.HttpServletRequest;

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
	
	@Autowired
	FertilizerRepo fertlizerRepo;
	
	@Autowired
	PlansServices plansServices;
	
	@Value("${file.upload-dir}")
    private String uploadDir;
	
	@GetMapping("/getServiceName")
	public ResponseEntity<Map<String, Object>> getServiceName(HttpServletRequest request) {
	    Map<String, Object> response = new HashMap<>();
	    
	    List<serviceName> serviceList = this.serviceNameDao.getallService();
	    List<Map<String, Object>> filteredServices = serviceList.stream().map(service -> {
	        Map<String, Object> serviceMap = new HashMap<>();
	        serviceMap.put("primaryKey", service.getPrimaryKey());
	        serviceMap.put("name", service.getName());
	        serviceMap.put("active", service.isActive());
	        serviceMap.put("servicePlanImage", Utils.findImgPath(request,uploadDir, service.getName(), service.getServiceImage()));
	        return serviceMap;
	    }).collect(Collectors.toList());

	    if (filteredServices.isEmpty()) {
	        response.put("status", "error");
	        response.put("message", "No Data Found");
	    } else {
	        response.put("status", "success");
	        response.put("service", filteredServices);
	    }

	    return ResponseEntity.ok(response);
	}

	@GetMapping("/getDashboardDataUser")
	public ResponseEntity<Map<String, Object>> getDashboardDataUser(HttpServletRequest request) {
	    Map<String, Object> response = new HashMap<>();

	    List<serviceName> serviceList = this.serviceNameDao.getallService();
	    List<Offers> offerList = this.offerdao.getListActiveOffer();

	    List<Map<String, Object>> filteredServices = serviceList.stream().map(service -> {
	        Map<String, Object> serviceMap = new HashMap<>();
	        serviceMap.put("primaryKey", service.getPrimaryKey());
	        serviceMap.put("servicePlanImage", Utils.findImgPath(request,uploadDir, service.getName(), service.getServiceImage()));
	        serviceMap.put("name", service.getName());
	        serviceMap.put("active", service.isActive());
	        return serviceMap;
	    }).collect(Collectors.toList());

	    response.put("serviceName", filteredServices);
	    response.put("offerData", offerList);
    	response.put("status", "true");

	    if (filteredServices.isEmpty()) {
	    	response.put("status", "false");
	    	response.put("message","No Data Found");
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.ok(response);
	    }
	}
	
	@GetMapping("/getServiceIdPlan/{id}") 
	public ResponseEntity<Map<String, Object>> getServiceIdPlan(@PathVariable Integer id,HttpServletRequest request) {
		serviceName service = this.serviceNameDao.getById(id);
	    List<Plans> getPlanId = this.customerDao.getPlansListId(id);

	    Map<String, Object> response = new HashMap<>();
	    List<Map<String, Object>> dailyPlansList = getPlanId.stream()
	            .filter(plan -> "DAILY".equalsIgnoreCase(plan.getPlanPacks()))
	            .map(plan -> {
	                Map<String, Object> planDetails = new HashMap<>();
	                planDetails.put("primaryKey", plan.getPrimaryKey());
	                planDetails.put("plansName", plan.getPlansName());
	                planDetails.put("plansRs", plan.getPlansRs());
	                planDetails.put("planImage", Utils.findImgPath(request,uploadDir, service.getName(), plan.getPlanImage()));
	                planDetails.put("ratingStar", plan.getRatingStar());
	                planDetails.put("uptoPots", plan.getUptoPots());
	                return planDetails;
	            })
	            .collect(Collectors.toList());

	    List<Plans> monthlyPlans = getPlanId.stream()
	        .filter(plan -> "MONTHLY".equalsIgnoreCase(plan.getPlanPacks()))
	        .collect(Collectors.toList());
	   	    

	    if (dailyPlansList.isEmpty() && monthlyPlans.isEmpty()) {
	    	response.put("status", false);
	    	response.put("message","No Data Found");
	        return ResponseEntity.ok(response);
	    } else {
	 	    //response.put("AllPlans", getPlanId);
	    	response.put("DailyPlan", dailyPlansList);
		    //response.put("Monthly", monthlyPlans);
	    	response.put("service", service.getName());
	    	response.put("status", true);
	        return ResponseEntity.ok(response);
	    }
	}

	
	@GetMapping("/getPlanID/{id}") 
	public ResponseEntity<?> getPlanID(@PathVariable String id,HttpServletRequest request) {
	    Map<String, Object> response = new HashMap<>();
		Plans getPlan = this.customerDao.getPlansId(id);
		if(Objects.isNull(getPlan)) {
			response.put("status", "false");
	    	response.put("message","No Data Found");
	        return ResponseEntity.ok(response);
	    }else {
	    	getPlan.setPlanImagePath(Utils.findImgPath(request,uploadDir, getPlan.getServicesName().getName(), getPlan.getPlanImage()));
	    	response.put("Plan", getPlan);
	     	response.put("status", "true");
			return ResponseEntity.ok(response);
		}
	}
	
	
	
	@GetMapping("/getPlans")
	public ResponseEntity<HashMap<String, Object>> getAllPlans() {
		HashMap<String, Object> response = new HashMap<>();
		List<serviceName> getPlans = this.serviceNameDao.getallPlans();
		for (serviceName s : getPlans) {
			List<Plans> activePlans = new ArrayList<>();
			for (Plans p : s.getPlans()) {
				if (p.isActive()) {
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
