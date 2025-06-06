package com.plants.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Dao.MobileApiDao;
import com.plants.Service.AgentReferralService;
import com.plants.config.JwtUtil;
import com.plants.entities.AgentMain;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/agentReferral")
public class AgentReferralController {
	
	@Autowired
	private AgentReferralService agentReferralService;
	
	@Autowired
	private MobileApiDao mobileApiDao;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/assignReferralCode")
    public ResponseEntity<Map<String, Object>> assignReferralCode(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, String> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);
		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> savedReferral = agentReferralService.assignReferralCode(agentRecords, request);
		return ResponseEntity.ok(savedReferral.getBody());
    }
	
	@GetMapping("/getReferralCode")
    public ResponseEntity<Map<String, Object>> getReferralCode(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		Map<String, Object> response = new HashMap<>();
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);
		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		 response.put("status", true);
         response.put("ReferralCode", agentRecords.getAgentReferralCode());
		return ResponseEntity.ok(response);
    }
	
	@GetMapping("/getWalletHistoryAgent")
    public ResponseEntity<Map<String, Object>> getWalletHistoryAgent(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
    		@RequestParam(value = "pageNumber",defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(value = "pageSize",defaultValue = "15" ,required = false) Integer pageSize) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);
		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> wallethist = agentReferralService.walletHistoryAgent(agentRecords,pageNumber,pageSize);
		return ResponseEntity.ok(wallethist.getBody());
    }
	
	@GetMapping("/getNotificationHistoryAgent")
	public ResponseEntity<Map<String, Object>> getNotificationHistoryAgent(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestParam(value = "pageNumber",defaultValue = "0" ,required = false) Integer pageNumber,
			@RequestParam(value = "pageSize",defaultValue = "15" ,required = false) Integer pageSize,HttpServletRequest request) {
	    String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
	    String mobileNumber = jwtUtil.extractUsername(jwtToken);
	    AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);
	    if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
	    }
	    
	    String baseUrl = request.getScheme() + "://" + request.getServerName() +
	            (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
	            request.getContextPath() + "/agentReferral/getNotificationHistoryAgent";
	    return agentReferralService.getNotificationHistoryAgent(agentRecords,pageNumber,pageSize,baseUrl);
	}

}
