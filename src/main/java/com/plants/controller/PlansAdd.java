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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.CustomerDao;
import com.plants.Dao.FertilizerRepo;
import com.plants.Dao.OfferDao;
import com.plants.Dao.serviceNameDao;
import com.plants.Dao.userDao;
import com.plants.entities.AgentMain;
import com.plants.entities.Fertilizer;
import com.plants.entities.Offers;
import com.plants.entities.Plans;
import com.plants.entities.PlansDto;
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
	
	@Autowired
	FertilizerRepo fertlizerRepo;
	
	@PostMapping("/addPlans")
	@ResponseBody
	public Map<String, String> addPlan(@RequestBody PlansDto plans) {
		Map<String, String> response = new HashMap<>();
		try {
		    serviceName serviceList = this.serviceNameDao.getServiceId(plans.getServicesName());

			
			System.out.println(" plans -- "+ plans);
			Plans savePlan = new Plans();
			savePlan.setPlansName(plans.getPlansName());
			savePlan.setPlanPacks(plans.getPlanPacks());
			savePlan.setPlansRs(plans.getPlansRs());
			savePlan.setPlanType(plans.getPlanType());
			savePlan.setTimeDuration(plans.getTimeDuration());
			savePlan.setUptoPots(plans.getUptoPots());
			savePlan.setActive(plans.isActive());
			savePlan.setIncludingServicesName(plans.getIncludingServicesName());
			savePlan.setServicesName(serviceList);
			
	        List<Fertilizer> fertilizers = plans.getFertilizers().stream().map(fertilizerDto -> {
	            Fertilizer fertilizer = new Fertilizer();
	            fertilizer.setFertilizerName(fertilizerDto.getFertilizerName());
	            fertilizer.setAmount(fertilizerDto.getAmount());
	            fertilizer.setKg(fertilizerDto.getKg());
	            return fertilizer;
	        }).collect(Collectors.toList());

	        savePlan.setFertilizers(fertilizers);
	        	
			Plans savedPlan = this.userdao.save(savePlan);
			
			for(Fertilizer fera : fertilizers) {
				fera.setAmount(fera.getAmount());
				fera.setFertilizerName(fera.getFertilizerName());
				fera.setKg(fera.getKg());
				fera.setPlans(savedPlan);
				this.fertlizerRepo.save(fera);
			}
			response.put("message", "Plans Add Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Error while adding plans");
		}
		return response;
	}
	
	@GetMapping("/getServiceName")
	public ResponseEntity<?> getServiceName() {

	    List<serviceName> serviceList = this.serviceNameDao.getallService();

	    List<Map<String, Object>> filteredServices = serviceList.stream().map(service -> {
	        Map<String, Object> serviceMap = new HashMap<>();
	        serviceMap.put("primaryKey", service.getPrimaryKey());
	        serviceMap.put("name", service.getName());
	        serviceMap.put("active", service.isActive());
	        return serviceMap;
	    }).collect(Collectors.toList());
	    if (filteredServices.isEmpty()) {
	        return ResponseEntity.ok("No Data Found");
	    } else {
	        return ResponseEntity.ok(filteredServices);
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
	public ResponseEntity<Map<String, Object>> getServiceIdPlan(@PathVariable String id) {
	    List<Plans> getPlanId = this.customerDao.getPlansListId(id);

	    Map<String, Object> response = new HashMap<>();
	    List<Plans> dailyPlans = getPlanId.stream()
	        .filter(plan -> "DAILY".equalsIgnoreCase(plan.getPlanPacks()))
	        .collect(Collectors.toList());

	    List<Plans> monthlyPlans = getPlanId.stream()
	        .filter(plan -> "MONTHLY".equalsIgnoreCase(plan.getPlanPacks()))
	        .collect(Collectors.toList());
	   	    

	    if (dailyPlans.isEmpty() && monthlyPlans.isEmpty()) {
	    	response.put("status", "false");
	    	response.put("message","No Data Found");
	        return ResponseEntity.ok(response);
	    } else {
	 	    response.put("AllPlans", getPlanId);
	    	response.put("DailyPlan", dailyPlans);
		    response.put("Monthly", monthlyPlans);
	    	response.put("status", "true");
	        return ResponseEntity.ok(response);
	    }
	}

	
	@GetMapping("/getPlanID/{id}") 
	public ResponseEntity<?> getPlanID(@PathVariable String id) {
	    Map<String, Object> response = new HashMap<>();
		Plans getPlan = this.customerDao.getPlansId(id);
		if(Objects.isNull(getPlan)) {
			response.put("status", "false");
	    	response.put("message","No Data Found");
	        return ResponseEntity.ok(response);
	    }else {
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
