package com.plants.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plants.Dao.AppRatingRepo;
import com.plants.Dao.MobileApiDao;
import com.plants.Dao.userDao;
import com.plants.config.JwtUtil;
import com.plants.config.Utils;
import com.plants.entities.AgentJsonRequest;
import com.plants.entities.AgentMain;
import com.plants.entities.AppRating;
import com.plants.entities.CustomerMain;

import jakarta.transaction.Transactional;

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
	
	@Autowired
	private AppRatingRepo appRatingRepo;

	@Value("${file.upload-dir}")
	private String uploadDir;
	
	@Value("${google.api.key}")
	private String googleApiKey;
	
	@Value("${google.url.address}")
	private String googleUrlAddress;
	
	@Autowired
	private S3Service s3Service;

	public ResponseEntity<Map<String, String>> sentOtp(String mobileNumber) {
		Map<String, String> response = new HashMap<>();
		if (mobileNumber == null || mobileNumber.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Mobile number is required"));
		}
		String otp = otpService.generateOTP(mobileNumber);
		// smsService.sendOtp(mobileNumber, otp);
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
				agent.setAgentApproved(false);
				agent.setProfileInfoStepFirst(false);
				agent.setAadharInfoStepSecond(false);
				agent.setBankInfoStepThird(false);
				agent.setAgentReferralCode(generateReferralCode());
				AgentMain saAgent = this.userdao.save(agent);
				// response.put("AgentIdPk", saAgent.getAgentIDPk());
				// response.put("isProfileCompleted", saAgent.isProfileCompleted());
				// response.put("isProfileInfoStepFirst", saAgent.isProfileInfoStepFirst());
				// response.put("isAadharInfoStepSecond", saAgent.isAadharInfoStepSecond());
				// response.put("isBankInfoStepThird", saAgent.isBankInfoStepThird());
				// response.put("token", saAgent.getToken());
				// response.put("Object", saAgent);

				Map<String, Object> data = Map.of("isProfileCompleted", saAgent.isProfileCompleted(),
						"isProfileInfoStepFirst", saAgent.isProfileInfoStepFirst(), "isAadharInfoStepSecond",
						saAgent.isAadharInfoStepSecond(), "isBankInfoStepThird", saAgent.isBankInfoStepThird(), "token",
						saAgent.getToken());

				response.put("data", data);
				response.put("message", "OTP Verified Successfully");
			} else {
				// findMob.setBankPassBookImage(Utils.findImgPath(findMob.getBankPassBookImage()));
				// findMob.setSelfieImg(Utils.findImgPath(findMob.getSelfieImg()));
				// findMob.setAadharImgFrontSide(Utils.findImgPath(findMob.getAadharImgFrontSide()));
				// findMob.setAadharImgBackSide(Utils.findImgPath(findMob.getAadharImgBackSide()));
				// response.put("Object", findMob);
				Map<String, Object> data = Map.of("isProfileCompleted", findMob.isProfileCompleted(),
						"isProfileInfoStepFirst", findMob.isProfileInfoStepFirst(), "isAadharInfoStepSecond",
						findMob.isAadharInfoStepSecond(), "isBankInfoStepThird", findMob.isBankInfoStepThird(), "token",
						findMob.getToken());
				response.put("data", data);
				response.put("message", "OTP Verified Successfully");
			}
			return ResponseEntity.ok(response);
		} else {
			response.put("message", "Invalid or Expired OTP");
			return ResponseEntity.ok(response);
		}
	}

	public ResponseEntity<Map<String, Object>> profileInfoDetailsAgent(AgentMain existingAgent,String agentPersonalDetails, MultipartFile selfieImg) {
		Map<String, Object> response = new HashMap<>();
		if (selfieImg.isEmpty()) {
			response.put("message", "Please select your selfie image.");
			return ResponseEntity.badRequest().body(response);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			AgentJsonRequest agentJsonRequest = objectMapper.readValue(agentPersonalDetails, AgentJsonRequest.class);
			if (Objects.nonNull(existingAgent)) {
				AgentMain agentMain = existingAgent;
				agentMain.setFirstName(agentJsonRequest.getAgentPersonalDetails().getFirstName());
				agentMain.setLastName(agentJsonRequest.getAgentPersonalDetails().getLastName());
				agentMain.setGender(agentJsonRequest.getAgentPersonalDetails().getGender());
				agentMain.setMobileNumber(agentJsonRequest.getAgentPersonalDetails().getMobileNumber());
				agentMain.setEmailId(agentJsonRequest.getAgentPersonalDetails().getEmailId());
				agentMain.setState(agentJsonRequest.getAgentPersonalDetails().getState());
				agentMain.setCity(agentJsonRequest.getAgentPersonalDetails().getCity());
				String address = Utils.getAddressFromCoordinates(agentJsonRequest.getAgentPersonalDetails().getLatitude(), agentJsonRequest.getAgentPersonalDetails().getLongitude(), googleApiKey, googleUrlAddress);
				agentMain.setAddress(address);
				agentMain.setPincode(agentJsonRequest.getAgentPersonalDetails().getPincode());
				agentMain.setLatitude(agentJsonRequest.getAgentPersonalDetails().getLatitude());
				agentMain.setLongitude(agentJsonRequest.getAgentPersonalDetails().getLongitude());
				agentMain.setFcmTokenAgent(agentJsonRequest.getAgentPersonalDetails().getFcmtoken());
				//set image Name
				String subDirectoryFloderName = agentJsonRequest.getAgentPersonalDetails().getMobileNumber();
		        String fileName = agentJsonRequest.getAgentPersonalDetails().getMobileNumber() + System.currentTimeMillis() + "_" + selfieImg.getOriginalFilename();
				String selfieImageUrl = Utils.saveImgFile(selfieImg,uploadDir,subDirectoryFloderName,fileName);
				
				agentMain.setSelfieImg(fileName);
				agentMain.setSelfieImagePath(selfieImageUrl);
				agentMain.setSelfieImg_type(selfieImg.getContentType());
				agentMain.setProfileInfoStepFirst(true);
				AgentMain getid = this.UserDao.save(agentMain);
				response.put("isProfileInfoStepFirst", getid.isProfileInfoStepFirst());
				response.put("message", "Agent profile saved successfully!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> AadhaarDetailFill(AgentMain existingAgent, String AadhaardetailsAgents,
			MultipartFile aadharImgFrontSide, MultipartFile aadharImgBackSide) {
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
			if (Objects.nonNull(existingAgent)) {
				AgentMain agentMain = existingAgent;
				agentMain.setAadhaarNumber(agentJsonRequest.getAadharIdentityDetail().getAadhaarNumber());
				
				// set image Name
				
				String subDirectoryFloderName = existingAgent.getMobileNumber();
		        String aadharFrontfileName = existingAgent.getMobileNumber() + System.currentTimeMillis() + "_" + aadharImgFrontSide.getOriginalFilename();
				String aadharFrontImagePath = Utils.saveImgFile(aadharImgFrontSide,uploadDir,subDirectoryFloderName,aadharFrontfileName);
				
				agentMain.setAadharImgFrontSide(aadharFrontfileName);
				agentMain.setAadharImagFrontSidePath(aadharFrontImagePath);
				agentMain.setAadharImgFrontSide_type(aadharImgFrontSide.getContentType());;
				
				
				String aadharBackFileName = existingAgent.getMobileNumber() + System.currentTimeMillis() + "_" + aadharImgBackSide.getOriginalFilename();
				String aadharBackImagePath = Utils.saveImgFile(aadharImgBackSide,uploadDir,subDirectoryFloderName,aadharBackFileName);
			
				agentMain.setAadharImgBackSide(aadharBackFileName);
				agentMain.setAadharImagBackSidePath(aadharBackImagePath);
				agentMain.setAadharImgBackSide_type(aadharImgBackSide.getContentType());
				agentMain.setAadharInfoStepSecond(true);
				AgentMain getid = this.UserDao.save(agentMain);
				response.put("isAadharInfoStepSecond", getid.isAadharInfoStepSecond());
				response.put("message", "Agent Aadhaar saved successfully!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> bankDetailFill(AgentMain existingAgent, String bankDetailsAgent,
			MultipartFile bankPassBookImage) {
		Map<String, Object> response = new HashMap<>();
		if (bankPassBookImage.isEmpty()) {
			response.put("message", "Please select your Bank Pass book image.");
			return ResponseEntity.badRequest().body(response);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			AgentJsonRequest agentJsonRequest = objectMapper.readValue(bankDetailsAgent, AgentJsonRequest.class);
			if (Objects.nonNull(existingAgent)) {
				AgentMain agentMain = existingAgent;
				// upload seli image path folder
				// String pathbankimg = Utils.saveImgFile(bankPassBookImage);
				agentMain.setAccHolderName(agentJsonRequest.getAccountDetail().getAccHolderName());
				agentMain.setAccNumber(agentJsonRequest.getAccountDetail().getAccNumber());
				agentMain.setBankName(agentJsonRequest.getAccountDetail().getBankName());
				agentMain.setIfscCode(agentJsonRequest.getAccountDetail().getIfscCode());
				
				String subDirectoryFloderName = existingAgent.getMobileNumber();
		        String fileName = existingAgent.getMobileNumber() + System.currentTimeMillis() + "_" + bankPassBookImage.getOriginalFilename();
				String bankPassBookImagePath = Utils.saveImgFile(bankPassBookImage,uploadDir,subDirectoryFloderName,fileName);
				
				agentMain.setBankPassBookImage(fileName);
				agentMain.setBankPassBookImagePath(bankPassBookImagePath);
				agentMain.setBankPassBookImage_type(bankPassBookImage.getContentType());
				agentMain.setProfileCompleted(true);
				agentMain.setBankInfoStepThird(true);
				AgentMain getAgent = this.UserDao.save(agentMain);
				response.put("isBankInfoStepThird", getAgent.isBankInfoStepThird());
				response.put("message", "Bank Account saved successfully!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, String>> getUpdateLiveLocation(AgentMain existingAgent,
			Map<String, String> request) {
		Map<String, String> response = new HashMap<>();
		String Agentlatitude = request.get("Agentlatitude");
		String AgentLongtitude = request.get("AgentLongtitude");
		if (Objects.nonNull(existingAgent)) {
			AgentMain agentMain = null;
			agentMain = existingAgent; // Update existing agent

			if (agentMain.isActiveAgent() == true) {
				agentMain.setLatitude(Double.parseDouble(Agentlatitude));
				agentMain.setLongitude(Double.parseDouble(AgentLongtitude));
				this.userdao.save(agentMain);
				response.put("message", "Location Updated.");
			} else {
				response.put("message", "Agent is Not Active");
			}
		} else {
			response.put("message", "No Record Found Agent");
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, String>> activeAgentToggle(AgentMain existingAgent, Map<String, String> request) {
		Map<String, String> response = new HashMap<>();
		boolean isActiveAgent = Boolean.parseBoolean(request.get("isActiveAgent"));
		String Agentlatitude = request.get("Agentlatitude");
		String AgentLongtitude = request.get("AgentLongtitude");
		if (Objects.nonNull(existingAgent)) {
			AgentMain agentMain = null;
			agentMain = existingAgent; // Update existing agent
			if (isActiveAgent) {
				agentMain.setActiveAgent(isActiveAgent);
				agentMain.setLatitude(Double.parseDouble(Agentlatitude));
				agentMain.setLongitude(Double.parseDouble(AgentLongtitude));
				this.userdao.save(agentMain);
				response.put("message", "Agent Active.");
			} else {
				agentMain.setActiveAgent(isActiveAgent);
				this.userdao.save(agentMain);
				response.put("message", "Agent Deactive");
			}

		} else {
			response.put("message", "No Record Found Agent");
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, String>> getFirebaseDeviceToken(AgentMain existingAgent,
			Map<String, String> request) {
		Map<String, String> response = new HashMap<>();
		String firebaseDeviceToken = request.get("firebaseDeviceToken");
		if (Objects.nonNull(existingAgent)) {
			AgentMain agentMain = null;
			agentMain = existingAgent; // Update existing agent
			agentMain.setFcmTokenAgent(firebaseDeviceToken);
			this.userdao.save(agentMain);
			response.put("message", "Firebase Device Token Store");
		} else {
			response.put("message", "No Record Found Agent");
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> appRatingAgent(AgentMain existingAgent,Map<String, String> request) {
		    Map<String, Object> response = new HashMap<>();
		    try {
		        if (!request.containsKey("rating")) {
		            response.put("status", false);
		            response.put("message", "Rating is required");
		            return ResponseEntity.badRequest().body(response);
		        }
		        int rating;
		        try {
		            rating = Integer.parseInt(request.get("rating").toString());
		            if (rating < 1 || rating > 5) {
		                response.put("status", false);
		                response.put("message", "Rating must be between 1 and 5");
		                return ResponseEntity.badRequest().body(response);
		            }
		        } catch (NumberFormatException e) {
		            response.put("status", false);
		            response.put("message", "Invalid rating format");
		            return ResponseEntity.badRequest().body(response);
		        }

		        AppRating appRating = new AppRating();
		        appRating.setAgentMain(existingAgent);
		        appRating.setRating(rating);
		        appRatingRepo.save(appRating);

		        response.put("status", true);
		        response.put("message", "Thank You For Giving Rating");
		    } catch (Exception e) {
		        e.printStackTrace();
		        response.put("status", false);
		        response.put("message", "Something Went Wrong");
		    }
		    return ResponseEntity.ok(response);
	}


	private String generateReferralCode() {
		return "gar50" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
	}
}
