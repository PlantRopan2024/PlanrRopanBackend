package com.plants.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Service.RollupService;

@RestController
@RequestMapping("/rollupCtrl")
public class RollupCtrl {

	@Autowired
	private RollupService rollupService;
	// scheduling task by orders
	
	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Kolkata") // Executes every hour on the hour (IST)
	public void hourlyDataRollupOrders() {
		ResponseEntity<Map<String, Object>> hourlyDataRollup = this.rollupService.hourlyDataRollup();
		System.out.println("hourly rollup completed");
	}
	
	// daily task run 
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Kolkata") // Executes every day at 12:00 AM IST
	public void dailyDataRollupOrders() {
	    ResponseEntity<Map<String, Object>> dailyDataRollup = this.rollupService.dailyRollupData();
	    System.out.println("daily rollup completed at 12:00 AM");
	}
	
	
}
