package com.plants.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.plants.Dao.MobileApiDao;
import com.plants.Dao.userDao;
import com.plants.Service.AgentLoginService;
import com.plants.config.JwtUtil;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;


@RestController
@RequestMapping("/MobileLoginApi")
public class MobileLoginApiCont {

	@Autowired
	userDao userdao;
	
	@Autowired
	private AgentLoginService agentLoginService;
	
	@Autowired
	private MobileApiDao mobileApiDao;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	@GetMapping("/getallDetailAgent")
	public ResponseEntity<Map<String, Object>> getAllDetailAgent(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
	        String mobileNumber = jwtUtil.extractUsername(jwtToken);

	        AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

	        // Validate token and agent records
	        if (agentRecords == null || !jwtToken.equals(agentRecords.getToken())) {
	            response.put("error", "Invalid or expired token");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	        agentRecords.setBankPassBookImage(Utils.findImgPath(agentRecords.getBankPassBookImage()));
	        agentRecords.setSelfieImg(Utils.findImgPath(agentRecords.getSelfieImg()));
	        agentRecords.setAadharImgFrontSide(Utils.findImgPath(agentRecords.getAadharImgFrontSide()));
	        agentRecords.setAadharImgBackSide(Utils.findImgPath(agentRecords.getAadharImgBackSide()));
	        response.put("data", agentRecords); // Ensure AgentMain is serializable or convert to DTO
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        response.put("error", "An unexpected error occurred");
	        response.put("details", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	@PostMapping("/sendOTP")
	public ResponseEntity<Map<String, String>> sendOTP(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, String>> otpResponse = this.agentLoginService.sentOtp(request.get("mobileNumber") );
		return otpResponse;
	}

	@PostMapping("/verifyOTP")
	public ResponseEntity<Map<String, Object>> verifyOTP(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> validVerify = this.agentLoginService.verifiedOtpDetailAgent(request.get("mobileNumber") ,request.get("otp") );
		return validVerify;
	}
	
	@PostMapping(value ="/profileInfoAgent" ,consumes ="multipart/form-data")
	public ResponseEntity<Map<String, Object>> profileInfoDetails(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestPart("agentPersonalDetails") String agentPersonalDetails,  @RequestPart("selfieImg") MultipartFile selfieImg){
		
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);		
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);
		
	    if (Objects.isNull(agentRecords)   || !jwtToken.equals(agentRecords.getToken())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
	    }
		
		ResponseEntity<Map<String, Object>> getprofileDetails = agentLoginService.profileInfoDetailsAgent(agentRecords,agentPersonalDetails, selfieImg);
		return ResponseEntity.ok(getprofileDetails.getBody());
	}
	
	@PostMapping(value ="/aadhaarDetailsAgents" ,consumes ="multipart/form-data")
	public ResponseEntity<Map<String, Object>> aadhaarDetailsAgents(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestPart("agentPersonalDetails") String AadhaardetailsAgents,  
			@RequestPart("aadharImgFrontSide") MultipartFile aadharImgFrontSide,
            @RequestPart("aadharImgBackSide") MultipartFile aadharImgBackSide){
		
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);		
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);
		
	    if (Objects.isNull(agentRecords)   || !jwtToken.equals(agentRecords.getToken())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
	    }
		
		ResponseEntity<Map<String, Object>> getprofileDetails = agentLoginService.AadhaarDetailFill(agentRecords,AadhaardetailsAgents, aadharImgFrontSide , aadharImgBackSide);
		return ResponseEntity.ok(getprofileDetails.getBody());
	}
	
	@PostMapping(value ="/bankDetailAgents" ,consumes ="multipart/form-data")
	public ResponseEntity<Map<String, Object>> bankDetailAgents(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestPart("bankDetailsAgent") String bankDetailsAgent,  
			@RequestPart("bankPassBookImage") MultipartFile bankPassBookImage){
		
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);		
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);
		
	    if (Objects.isNull(agentRecords)   || !jwtToken.equals(agentRecords.getToken())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
	    }
		
		ResponseEntity<Map<String, Object>> getprofileDetails = agentLoginService.bankDetailFill(agentRecords,bankDetailsAgent, bankPassBookImage);
		return ResponseEntity.ok(getprofileDetails.getBody());
	}

	@PostMapping("/getliveLocationLatiAndLong")
	public ResponseEntity<Map<String, String>> getliveLocationLatiAndLong(@RequestBody Map<String, String> request) {
		Map<String, String> response = new HashMap<>();
		String AgentIDPk = request.get("AgentIDPk");
		String Agentlatitude = request.get("Agentlatitude");
		String AgentLongtitude = request.get("AgentLongtitude");

		AgentMain getActiveRecord = this.userdao.findAgentID(AgentIDPk);

		if (getActiveRecord != null) {
			if (getActiveRecord.isActiveAgent() == true) {
				System.out.println("---AgentIDPk--- " + AgentIDPk);
				System.out.println("---Agentlatitude--- " + Agentlatitude);
				System.out.println("---AgentLongtitude--- " + AgentLongtitude);
				this.userdao.updateliveLocation(Agentlatitude, AgentLongtitude, AgentIDPk);
				response.put("message", "Location Updated.");
			} else {
				response.put("message", "Agent is Not Active");
			}

		} else {
			response.put("message", "No Record Found Agent");
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/getActiveAgentToggle")
	public ResponseEntity<Map<String, String>> getActiveAgentToggle(@RequestBody Map<String, String> request) {
		Map<String, String> response = new HashMap<>();
		String AgentIDPk = request.get("AgentIDPk");
		boolean isActiveAgent = request.get("isActiveAgent") != null;

		AgentMain getagentRecord = this.userdao.findAgentID(AgentIDPk);

		if (getagentRecord != null) {
			System.out.println("---AgentIDPk--- " + AgentIDPk);
			System.out.println("---isActiveAgent--- " + isActiveAgent);

			this.userdao.UpdateagentActive(isActiveAgent, getagentRecord.getAgentIDPk());
			response.put("message", "Agent Active.");
		} else {
			response.put("message", "No Record Found Agent");
		}

		return ResponseEntity.ok(response);
	}
	
}
