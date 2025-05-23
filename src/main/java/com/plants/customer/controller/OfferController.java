package com.plants.customer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Service.OfferService;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;
import com.plants.entities.Offers;

@RestController
@RequestMapping("/offerApi")
public class OfferController {
	
	@Value("${pagination.page}")
	private int page;
	
	@Value("${pagination.size}")
	private int size;
	
	@Autowired
	public OfferService offerService;

	@PostMapping("/addOffer")
	@ResponseBody
	public Map<String, Object> addOffer(@ModelAttribute Offers offers) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (offers.getTypeID().equals("Agent")) {
				String agentOfferCode = generateAgentOfferCode();
				offers.setOfferCode(agentOfferCode);
			} else if (offers.getTypeID().equals("Customer")) {
				String cusOfferCode = generateCusOfferCode();
				offers.setOfferCode(cusOfferCode);
			}
			this.offerService.saveOffer(offers);
			response.put("message", "Offer Added Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Error while adding plans");
		}
		return response;
	}
	
	@GetMapping("/getoffersAgentsMobApi")
	public ResponseEntity<Map<String, Object>> getOffersAgentsMobApi() {
	    Map<String, Object> response = new HashMap<>();
	    List<Offers> offersAgent = offerService.getAllOffersAgentMobApi();
	    if (offersAgent == null || offersAgent.isEmpty()) {
	        response.put("status", true);
	        response.put("message", "No Offer Available!");
	        response.put("data", offersAgent);
	        response.put("currentPage", 0);
	        response.put("totalPages", 0);
	        response.put("totalElements", 0);
	        return ResponseEntity.ok(response);
	    }
	    // Implement pagination
	    int totalElements = offersAgent.size();
	    Map<String, Object> pagination = Utils.paganationInApi(size, page, totalElements);
	    int startIndex = (int) pagination.get("startIndex");
	    int endIndex = (int) pagination.get("endIndex");
	    int totalPages = (int) pagination.get("totalPages");
	    int currentPage = (int) pagination.get("currentPage");
	    List<Offers> paginatedList = offersAgent.subList(startIndex, Math.min(endIndex, totalElements));
	    response.put("status", true);
	    response.put("message", "Offers Available!");
	    response.put("data", paginatedList);
	    response.put("currentPage", currentPage);
	    response.put("totalPages", totalPages);
	    response.put("totalElements", totalElements);

	    return ResponseEntity.ok(response);
	}


	@GetMapping("/getoffersCusMobApi")
	public ResponseEntity<Map<String, Object>> getOffersCusMobApi() {
	    Map<String, Object> response = new HashMap<>();
	    List<Offers> offersCus = offerService.getAllOffersCusMobApi();
	    if (offersCus == null || offersCus.isEmpty()) {
	        response.put("status", true);
	        response.put("message", "No Offer Available!");
	        response.put("data", offersCus);
	        response.put("currentPage", 0);
	        response.put("totalPages", 0);
	        response.put("totalElements", 0);
	        return ResponseEntity.ok(response);
	    }
	    // Implement pagination
	    int totalElements = offersCus.size();
	    Map<String, Object> pagination = Utils.paganationInApi(size, page, totalElements);
	    int startIndex = (int) pagination.get("startIndex");
	    int endIndex = (int) pagination.get("endIndex");
	    int totalPages = (int) pagination.get("totalPages");
	    int currentPage = (int) pagination.get("currentPage");
	    List<Offers> paginatedList = offersCus.subList(startIndex, Math.min(endIndex, totalElements));
	    response.put("status", true);
	    response.put("message", "Offers Available!");
	    response.put("data", paginatedList);
	    response.put("currentPage", currentPage);
	    response.put("totalPages", totalPages);
	    response.put("totalElements", totalElements);
	    return ResponseEntity.ok(response);
	}


	@GetMapping("/getoffersAgents")
	public ResponseEntity<Map<String, Object>> getoffersAgents() {
		Map<String, Object> response = new HashMap<String, Object>();
		List<Offers> offersAgent = offerService.getAllOffersAgent();
		if (offersAgent.isEmpty()) {
			response.put("status", "warning");
			response.put("Offer", "No Offer Avaliable!");
			return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		}
		response.put("status", "success");
		response.put("Offer", offersAgent);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getoffersCus")
	public ResponseEntity<Map<String, Object>> getoffersCus() {
		Map<String, Object> response = new HashMap<String, Object>();
		List<Offers> offersCus = offerService.getAllOffersCus();
		if (offersCus.isEmpty()) {
			response.put("status", "warning");
			response.put("Offer", "No Offer Avaliable!");
			return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		}
		response.put("status", "success");
		response.put("Offer", offersCus);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/offersAgentVsCusApproved")
	@ResponseBody
	public String offersAgentVsCusApproved(@RequestBody Map<String, String> request) {
		String primaryKey = request.get("primaryKey");
		String message = this.offerService.offersAgentVsCusApproved(primaryKey);
		return null;
	}

	
	@PostMapping("/offersAgentVsCusDisApprovedAgent")
	@ResponseBody
	public String offersAgentVsCusDisApprovedAgent(@RequestBody Map<String, String> request) {
		String primaryKey = request.get("primaryKey");
		String message = this.offerService.offersAgentVsCusDisApprovedAgent(primaryKey);
		return null;
	}
	
	@PostMapping("/getoffersId")
	public ResponseEntity<Offers> getoffersId(@RequestBody Map<String, String> request) {
		String primaryKey = request.get("primaryKey");
		Offers offersAgent = offerService.getIdOffers(primaryKey);
		return ResponseEntity.ok(offersAgent);
	}


	private String generateAgentOfferCode() {
		return "AGN" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
	}

	private String generateCusOfferCode() {
		return "CUS" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
	}
}
