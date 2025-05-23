package com.plants.controller;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.AppRatingRepo;
import com.plants.Dao.MobileApiDao;
import com.plants.Service.PayoutService;
import com.plants.config.JwtUtil;
import com.plants.entities.AgentMain;

@RestController
@RequestMapping("/payoutCont")
public class PayoutController {
	
	
	@Autowired
	private MobileApiDao mobileApiDao;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PayoutService payoutService;
	
	
	// scheduling task by orders
	
	 // Run every 5 seconds
    //@Scheduled(fixedRate = 5000)
    public void rollupDataOrderEarning() {
    	
    	payoutService.insertDataOrderDaily();
    	payoutService.insertDataOrderWeekly();
    	payoutService.insertDataOrderMonthly();
    }

    // Run every day at 2 AM
   // @Scheduled(cron = "0 0 2 * * *")
    public void runAtTwoAM() {
        System.out.println("Scheduled task at 2 AM: " + System.currentTimeMillis());
    }
	
	
	@GetMapping("/dailyOrderEarning/{date}")
	public ResponseEntity<Map<String, Object>> dailyOrderEarning(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String date) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> getupdatedLoc = payoutService.dailyOrderEarning(agentRecords,date);
		return ResponseEntity.ok(getupdatedLoc.getBody());
	}
	
	@GetMapping("/weeklyOrderEarning/{date}")
	public ResponseEntity<Map<String, Object>> weeklyOrderEarning(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String date) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> getupdatedLoc = payoutService.weeklyOrderEarning(agentRecords,date);
		return ResponseEntity.ok(getupdatedLoc.getBody());
	}
	
	@GetMapping("/monthlyOrderEarning/{date}")
	public ResponseEntity<Map<String, Object>> monthlyOrderEarning(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String date) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> getupdatedLoc = payoutService.monthlyOrderEarning(agentRecords,date);
		return ResponseEntity.ok(getupdatedLoc.getBody());
	}
	
}
