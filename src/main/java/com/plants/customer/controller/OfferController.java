package com.plants.customer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.plants.customer.Service.OfferService;
import com.plants.entities.Offers;

@RestController
@RequestMapping("/offerApi")
public class OfferController {
	
	@Autowired
	public OfferService offerService;
	
	@PostMapping("/addOffer")
	 @ResponseBody
	 public Map<String, Object> addOffer(@ModelAttribute Offers offers) {
	     Map<String, Object> response = new HashMap<>();
	     try {
	          this.offerService.saveOffer(offers);
	         response.put("message", "Offer Added Successfully");
	     } catch (Exception e) {
	         e.printStackTrace();
	         response.put("message", "Error while adding plans");
	     }
	     return response;
	 }
	 
	 @GetMapping("/getoffers")
	 public ResponseEntity<List<Offers>> getAllOffers() {
	     List<Offers> offers = offerService.getAllOffers();
	     if (offers.isEmpty()) {
	         return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
	     }
	     return new ResponseEntity<>(offers, HttpStatus.OK); 
	 }

}
