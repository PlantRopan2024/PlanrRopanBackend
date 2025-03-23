package com.plants.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.http.MediaType;

import com.plants.Dao.AppRatingRepo;
import com.plants.Dao.MobileApiDao;
import com.plants.Dao.userDao;
import com.plants.Service.AgentLoginService;
import com.plants.config.JwtUtil;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;

import jakarta.servlet.http.HttpServletRequest;

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
	private AppRatingRepo appRatingRepo;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Value("${file.upload-dir}")
    private String uploadDir;

	@GetMapping("/getallDetailAgent")
	public ResponseEntity<Map<String, Object>> getAllDetailAgent(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token,HttpServletRequest request) {
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
			
			String selfieImageUrl = Utils.findImgPath(uploadDir, agentRecords.getMobileNumber(), agentRecords.getSelfieImg());
			String aadharFrontImageUrl = Utils.findImgPath(uploadDir, agentRecords.getMobileNumber(), agentRecords.getAadharImgFrontSide());
			String aadharBackImageUrl = Utils.findImgPath(uploadDir, agentRecords.getMobileNumber(), agentRecords.getAadharImgBackSide());
			String bankImageUrl = Utils.findImgPath(uploadDir, agentRecords.getMobileNumber(), agentRecords.getBankPassBookImage());

			Map<String, Object> data = new HashMap<>();
			data.put("agentIDPk", agentRecords.getAgentIDPk());
			data.put("firstName", agentRecords.getFirstName());
			data.put("lastName", agentRecords.getLastName());
			data.put("gender", agentRecords.getGender());
			data.put("selfieImg", agentRecords.getSelfieImg());
			data.put("selfieImg_type", agentRecords.getSelfieImg_type());
			data.put("selfiImageUrlPath", agentRecords.getSelfieImagePath());
			data.put("emailId", agentRecords.getEmailId());
			data.put("mobileNumber", agentRecords.getMobileNumber());
			data.put("agentApproved", agentRecords.isAgentApproved());
			data.put("activeAgent", agentRecords.isActiveAgent());
			data.put("state", agentRecords.getState());
			data.put("city", agentRecords.getCity());
			data.put("selefiImageUrl", selfieImageUrl);
			data.put("aadharFrontImageUrl", aadharFrontImageUrl);
			data.put("aadharBackImageUrl", aadharBackImageUrl);
			data.put("bankPassImageUrl", bankImageUrl);
			data.put("address", agentRecords.getAddress());
			data.put("pincode", agentRecords.getPincode());
			data.put("latitude", agentRecords.getLatitude());
			data.put("longitude", agentRecords.getLongitude());
			data.put("aadhaarNumber", agentRecords.getAadhaarNumber());
			data.put("aadharImgFrontSide", agentRecords.getAadharImgFrontSide());
			data.put("aadharImgFrontSide_type", agentRecords.getAadharImgFrontSide_type());
			data.put("aadharImgFrontSideUrlPath", agentRecords.getAadharImagFrontSidePath());
			data.put("aadharImgBackSide", agentRecords.getAadharImgBackSide());
			data.put("aadharImgBackSide_type", agentRecords.getAadharImgBackSide_type());
			data.put("aadharImgBackSideUrlPath", agentRecords.getAadharImagBackSidePath());
			data.put("token", agentRecords.getToken());
			data.put("accHolderName", agentRecords.getAccHolderName());
			data.put("accNumber", agentRecords.getAccNumber());
			data.put("bankName", agentRecords.getBankName());
			data.put("ifscCode", agentRecords.getIfscCode());
			data.put("bankPassBookImage", agentRecords.getBankPassBookImage());
			data.put("bankPassBookImage_type", agentRecords.getBankPassBookImage_type());
			data.put("bankPassBookImageUrlPath", agentRecords.getBankPassBookImagePath());
			data.put("profileCompleted", agentRecords.isProfileCompleted());
			data.put("fcmTokenAgent", agentRecords.getFcmTokenAgent());
			data.put("profileInfoStepFirst", agentRecords.isProfileInfoStepFirst());
			data.put("aadharInfoStepSecond", agentRecords.isAadharInfoStepSecond());
			data.put("bankInfoStepThird", agentRecords.isBankInfoStepThird());
			response.put("data", data);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("error", "An unexpected error occurred");
			response.put("details", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/sendOTP")
	public ResponseEntity<Map<String, String>> sendOTP(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, String>> otpResponse = this.agentLoginService.sentOtp(request.get("mobileNumber"));
		return otpResponse;
	}

	@PostMapping("/verifyOTP")
	public ResponseEntity<Map<String, Object>> verifyOTP(@RequestBody Map<String, String> request) {
		ResponseEntity<Map<String, Object>> validVerify = this.agentLoginService.verifiedOtpDetailAgent(request.get("mobileNumber"), request.get("otp"));
		return validVerify;
	}

	@PostMapping(value = "/profileInfoAgent", consumes = "multipart/form-data")
	public ResponseEntity<Map<String, Object>> profileInfoDetails(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestPart("agentPersonalDetails") String agentPersonalDetails,
			@RequestPart(value = "selfieImg", required = false) MultipartFile selfieImg) {

		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}

		ResponseEntity<Map<String, Object>> getprofileDetails = agentLoginService.profileInfoDetailsAgent(agentRecords,
				agentPersonalDetails, selfieImg);
		return ResponseEntity.ok(getprofileDetails.getBody());
	}

	@PostMapping(value = "/aadhaarDetailsAgents", consumes = "multipart/form-data")
	public ResponseEntity<Map<String, Object>> aadhaarDetailsAgents(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestPart("agentPersonalDetails") String AadhaardetailsAgents,
			@RequestPart(value = "aadharImgFrontSide", required = false) MultipartFile aadharImgFrontSide,
			@RequestPart(value = "aadharImgBackSide", required = false) MultipartFile aadharImgBackSide) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}

		ResponseEntity<Map<String, Object>> getprofileDetails = agentLoginService.AadhaarDetailFill(agentRecords,
				AadhaardetailsAgents, aadharImgFrontSide, aadharImgBackSide);
		return ResponseEntity.ok(getprofileDetails.getBody());
	}

	@PostMapping(value = "/bankDetailAgents", consumes = "multipart/form-data")
	public ResponseEntity<Map<String, Object>> bankDetailAgents(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
			@RequestPart("bankDetailsAgent") String bankDetailsAgent,
			@RequestPart(value = "bankPassBookImage", required = false) MultipartFile bankPassBookImage) {

		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}

		ResponseEntity<Map<String, Object>> getprofileDetails = agentLoginService.bankDetailFill(agentRecords,
				bankDetailsAgent, bankPassBookImage);
		return ResponseEntity.ok(getprofileDetails.getBody());
	}

	@PostMapping("/getliveLocationLatiAndLong")
	public ResponseEntity<Map<String, String>> getliveLocationLatiAndLong(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, String> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, String>> getupdatedLoc = agentLoginService.getUpdateLiveLocation(agentRecords,
				request);
		return ResponseEntity.ok(getupdatedLoc.getBody());
	}

	@PostMapping("/getActiveAgentToggle")
	public ResponseEntity<Map<String, String>> getActiveAgentToggle(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, String> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, String>> activeAgent = agentLoginService.activeAgentToggle(agentRecords, request);
		return ResponseEntity.ok(activeAgent.getBody());
	}

	@PostMapping("/getFirebaseTokenDevice")
	public ResponseEntity<Map<String, String>> getFirebaseTokenDevice(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, String> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, String>> activeAgent = agentLoginService.getFirebaseDeviceToken(agentRecords,
				request);
		return ResponseEntity.ok(activeAgent.getBody());
	}
	
	@PostMapping("/appRatingAgent")
	public ResponseEntity<Map<String, Object>> appRatingAgentCon(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Map<String, String> request) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String mobileNumber = jwtUtil.extractUsername(jwtToken);
		AgentMain agentRecords = mobileApiDao.findMobileNumberValidateToken(mobileNumber);

		if (Objects.isNull(agentRecords) || !jwtToken.equals(agentRecords.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
		}
		ResponseEntity<Map<String, Object>> activeAgent = agentLoginService.appRatingAgent(agentRecords,
				request);
		return ResponseEntity.ok(activeAgent.getBody());
	}

}
