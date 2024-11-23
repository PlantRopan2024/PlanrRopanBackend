package com.plants.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plants.Dao.MobileApiDao;
import com.plants.Dao.userDao;
import com.plants.config.JwtUtil;
import com.plants.config.Utils;
import com.plants.entities.AgentJsonRequest;
import com.plants.entities.AgentMain;

@Service
public class AgentLoginService {

	@Autowired
	private userDao UserDao;
	@Autowired
	private OTPService otpService;

	@Autowired
	private SmsService smsService;

	@Autowired
	private MobileApiDao mobileApiDao;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	userDao userdao;

	public ResponseEntity<Map<String, String>> sentOtp(String mobileNumber) {
		Map<String, String> response = new HashMap<>();
		if (mobileNumber == null || mobileNumber.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Mobile number is required"));
		}
		String otp = otpService.generateOTP(mobileNumber);
		smsService.sendOtp(mobileNumber, otp);
		response.put("message", "OTP sent successfully!");
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> verifiedOtpDetailAgent(String mobileNumber, String otp) {

		Map<String, Object> response = new HashMap<>();

		if (mobileNumber == null || mobileNumber.isEmpty() || otp == null || otp.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Mobile number and OTP are required"));
		}

		boolean isOtpValid = otpService.verifyOtp(mobileNumber, otp);

		if (isOtpValid) {
			AgentMain findMob = mobileApiDao.findMobileNumber(mobileNumber);
			if (Objects.isNull(findMob)) {
				String token = jwtUtil.generateToken(mobileNumber);
				System.out.println(" TOKEN ----- " + token);

				// save the token in DB
				AgentMain agent = new AgentMain();
				agent.setToken(token);
				agent.setMobileNumber(mobileNumber);
				agent.setProfileCompleted(false);
				agent.setActiveAgent(false);
				agent.setAgentVerified(false);
				AgentMain saAgent = this.userdao.save(agent);

				response.put("isProfileCompleted", saAgent.isProfileCompleted());
				response.put("token", saAgent.getToken());
				response.put("message", "OTP Verified Successfully");
			} else {
				findMob.setBankPassBookImage(Utils.findImgPath(findMob.getBankPassBookImage()));
				findMob.setSelfieImg(Utils.findImgPath(findMob.getSelfieImg()));
				findMob.setAadharImgFrontSide(Utils.findImgPath(findMob.getAadharImgFrontSide()));
				findMob.setAadharImgBackSide(Utils.findImgPath(findMob.getAadharImgBackSide()));
				response.put("Object", findMob);
				response.put("message", "OTP Verified Successfully");
			}
			return ResponseEntity.ok(response);
		} else {
			response.put("message", "Invalid or Expired OTP");
			return ResponseEntity.ok(response);
		}
	}

	public ResponseEntity<Map<String, Object>> profileInfoDetailsAgent(String agentPersonalDetails,MultipartFile selfieImg) {
		Map<String, Object> response = new HashMap<>();

		if (selfieImg.isEmpty()) {
			response.put("message", "Please select your selfie image.");
			return ResponseEntity.badRequest().body(response);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			AgentJsonRequest agentJsonRequest = objectMapper.readValue(agentPersonalDetails, AgentJsonRequest.class);
			AgentMain existingAgent = this.UserDao.findEmailAndMobileAg(agentJsonRequest.getAgentPersonalDetails().getEmailId(),agentJsonRequest.getAgentPersonalDetails().getMobileNumber());

			if (existingAgent != null) {

				// if
				// (agentJsonRequest.getAgentPersonalDetails().getEmailId().equals(existingAgent.getEmailId()))
				// {
				// response.put("message", "Email Already Exit !!");
				// return ResponseEntity.ok(response);
				// }
				// if
				// (agentJsonRequest.getAgentPersonalDetails().getMobileNumber().equals(existingAgent.getMobileNumber()))
				// {
				// response.put("message", "Mobile Number Exit !!");
				// return ResponseEntity.ok(response);
				// }

				AgentMain agentMain = null;

				agentMain = existingAgent; // Update existing agent

				// upload seli image path folder
				String selfieImageUrl = Utils.saveImgFile(selfieImg);
				//Utils.saveImgFile(selfieImg);

				agentMain.setFirstName(agentJsonRequest.getAgentPersonalDetails().getFirstName());
				agentMain.setLastName(agentJsonRequest.getAgentPersonalDetails().getLastName());
				agentMain.setGender(agentJsonRequest.getAgentPersonalDetails().getGender());
				agentMain.setMobileNumber(agentJsonRequest.getAgentPersonalDetails().getMobileNumber());
				agentMain.setEmailId(agentJsonRequest.getAgentPersonalDetails().getEmailId());
				agentMain.setState(agentJsonRequest.getAgentPersonalDetails().getState());
				agentMain.setCity(agentJsonRequest.getAgentPersonalDetails().getCity());
				agentMain.setAddress(agentJsonRequest.getAgentPersonalDetails().getAddress());
				agentMain.setPincode(agentJsonRequest.getAgentPersonalDetails().getPincode());
				agentMain.setLatitude(agentJsonRequest.getAgentPersonalDetails().getLatitude());
				agentMain.setLongitude(agentJsonRequest.getAgentPersonalDetails().getLongitude());
				agentMain.setFcmTokenAgent(agentJsonRequest.getAgentPersonalDetails().getFcmtoken());
				agentMain.setSelfieImg(selfieImg.getOriginalFilename());
				AgentMain getid = this.UserDao.save(agentMain);
			//	getid.setSelfieImg(selfieImageUrl);
				//response.put("Object", getid);
				response.put("AgentIdPk", getid.getAgentIDPk());
				response.put("message", "Agent profile saved successfully!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> AadhaarDetailFill(String AadhaardetailsAgents,MultipartFile aadharImgFrontSide, MultipartFile aadharImgBackSide) {
		Map<String, Object> response = new HashMap<>();

		if (aadharImgFrontSide.isEmpty()) {
			response.put("message", "Please select your Aadhaar front-side image.");
			return ResponseEntity.badRequest().body(response);
		}
		if (aadharImgBackSide.isEmpty()) {
			response.put("message", "Please select your Aadhaar back-side image.");
			return ResponseEntity.badRequest().body(response);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			AgentJsonRequest agentJsonRequest = objectMapper.readValue(AadhaardetailsAgents, AgentJsonRequest.class);

			System.out.println(" agent id ---  " + agentJsonRequest.getAadharIdentityDetail().getAgentIdPk());

			AgentMain existingAgent = this.UserDao.findAgentID(agentJsonRequest.getAadharIdentityDetail().getAgentIdPk());

			if (existingAgent != null) {

				AgentMain agentMain = null;

				agentMain = existingAgent; // Update existing agent

				// upload seli image path folder
				Utils.saveImgFile(aadharImgFrontSide);
				Utils.saveImgFile(aadharImgBackSide);
				agentMain.setAadhaarNumber(agentJsonRequest.getAadharIdentityDetail().getAadhaarNumber());
				agentMain.setAadharImgFrontSide(aadharImgFrontSide.getOriginalFilename());
				agentMain.setAadharImgBackSide(aadharImgBackSide.getOriginalFilename());

				AgentMain getid = this.UserDao.save(agentMain);
				
				///response.put("Object", getid);
				response.put("AgentIdPk", getid.getAgentIDPk());
				response.put("message", "Agent Aadhaar saved successfully!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(response);
	}

	
	public ResponseEntity<Map<String, Object>> bankDetailFill(String bankDetailsAgent,MultipartFile bankPassBookImage) {
		Map<String, Object> response = new HashMap<>();

		if (bankPassBookImage.isEmpty()) {
			response.put("message", "Please select your Bank Pass book image.");
			return ResponseEntity.badRequest().body(response);
		}
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			AgentJsonRequest agentJsonRequest = objectMapper.readValue(bankDetailsAgent, AgentJsonRequest.class);

			System.out.println(" agent id ---  " + agentJsonRequest.getAccountDetail().getAgentIdPk());

			AgentMain existingAgent = this.UserDao.findAgentID(agentJsonRequest.getAccountDetail().getAgentIdPk());

			if (existingAgent != null) {

				AgentMain agentMain = null;

				agentMain = existingAgent; // Update existing agent

				// upload seli image path folder
				String pathbankimg = Utils.saveImgFile(bankPassBookImage);
				agentMain.setAccHolderName(agentJsonRequest.getAccountDetail().getAccHolderName());
				agentMain.setAccNumber(agentJsonRequest.getAccountDetail().getAccNumber());
				agentMain.setBankName(agentJsonRequest.getAccountDetail().getBankName());
				agentMain.setIfscCode(agentJsonRequest.getAccountDetail().getIfscCode());
				agentMain.setBankPassBookImage(bankPassBookImage.getOriginalFilename());
				agentMain.setProfileCompleted(true);

				AgentMain getAgent = this.UserDao.save(agentMain);
				getAgent.setBankPassBookImage(Utils.findImgPath(getAgent.getBankPassBookImage()));
				getAgent.setSelfieImg(Utils.findImgPath(getAgent.getSelfieImg()));
				getAgent.setAadharImgFrontSide(Utils.findImgPath(getAgent.getAadharImgFrontSide()));
				getAgent.setAadharImgBackSide(Utils.findImgPath(getAgent.getAadharImgBackSide()));
				response.put("Agent", getAgent);
				response.put("message", "Bank Account saved successfully!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(response);
	}
	
}
