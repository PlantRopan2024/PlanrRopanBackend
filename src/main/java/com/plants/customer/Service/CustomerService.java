package com.plants.customer.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.plants.Dao.CustomerDao;
import com.plants.Service.OTPService;
import com.plants.Service.SmsService;
import com.plants.config.JwtUtil;
import com.plants.entities.AgentMain;
import com.plants.entities.CustomerMain;

@Service
public class CustomerService {

	@Autowired
	private OTPService otpService;

	@Autowired
	private SmsService smsService;
	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private JwtUtil jwtUtil;

	public ResponseEntity<Map<String, String>> sentOtpCus(String mobileNumber) {
		Map<String, String> response = new HashMap<>();
		if (mobileNumber == null || mobileNumber.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Mobile number is required"));
		}
		String otp = otpService.generateOTP(mobileNumber);
		// smsService.sendOtp(mobileNumber, otp);
		response.put("message", "OTP sent successfully!");
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> verifiedOtpDetailCustomer(String mobileNumber, String otp) {
		Map<String, Object> response = new HashMap<>();
		if (mobileNumber == null || mobileNumber.isEmpty() || otp == null || otp.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Mobile number and OTP are required"));
		}
		boolean isOtpValid = otpService.verifyOtp(mobileNumber, otp);
		if (isOtpValid) {
			CustomerMain findMob = customerDao.findMobileNumber(mobileNumber);
			if (Objects.isNull(findMob)) {
				String token = jwtUtil.generateToken(mobileNumber);
				System.out.println(" TOKEN ----- " + token);

				CustomerMain customerMain = new CustomerMain();
				customerMain.setToken(token);
				customerMain.setMobileNumber(mobileNumber);
				customerMain.setProfileCompleted(false);
				CustomerMain customerSave = this.customerDao.save(customerMain);
				Map<String, Object> data = Map.of("isProfileCompleted", customerSave.isProfileCompleted(), "token",
						customerSave.getToken());
				response.put("data", data);
				response.put("message", "OTP Verified Successfully");
			} else {
				Map<String, Object> data = Map.of("isProfileCompleted", findMob.isProfileCompleted(), "token",
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

	public ResponseEntity<Map<String, Object>> ProfileInfoSaveCustomer(CustomerMain exitsCustomer,
			CustomerMain RequestcustomerMain) {
		Map<String, Object> response = new HashMap<>();
		try {

			if (Objects.nonNull(exitsCustomer)) {
				CustomerMain customermain = exitsCustomer;
				customermain.setFirstName(RequestcustomerMain.getFirstName());
				customermain.setLastName(RequestcustomerMain.getLastName());
				customermain.setEmailId(RequestcustomerMain.getEmailId());
				customermain.setAddress(RequestcustomerMain.getAddress());
				customermain.setCity(RequestcustomerMain.getCity());
				customermain.setLatitude(RequestcustomerMain.getLatitude());
				customermain.setLoggitude(RequestcustomerMain.getLoggitude());
				customermain.setProfileCompleted(true);
				customermain.setFirebasetokenCus(RequestcustomerMain.getFirebasetokenCus());
				CustomerMain save = this.customerDao.save(customermain);
				response.put("isProfileCompleted", save.isProfileCompleted());
				response.put("message", "Save Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("error", "Something Went Wrong");
		}
		return ResponseEntity.ok(response);
	}

	public CustomerMain saveCustomerProfile(CustomerMain customerMain) {
		return customerDao.save(customerMain);
	}

	public ResponseEntity<Map<String, Object>> getUpdateLiveLocationCust(CustomerMain exitsCustomer,Map<String, String> request) {
	    Map<String, Object> response = new HashMap<>();
	    String custLatitude = request.get("custLatitude");
	    String custLongtitude = request.get("custLongtitude");
	    String city = request.get("city");
	    String address = request.get("address");

	    if (Objects.nonNull(exitsCustomer)) {
	        exitsCustomer.setLatitude(Double.parseDouble(custLatitude));
	        exitsCustomer.setLoggitude(Double.parseDouble(custLongtitude));
	        exitsCustomer.setAddress(address);
	        exitsCustomer.setCity(city);
	        CustomerMain save = this.customerDao.save(exitsCustomer);
	        Map<String, String> data = Map.of(
	            "address", save.getAddress(),
	            "city", save.getCity(),
	            "custLatitude", String.valueOf(save.getLatitude()),
	            "custLongtitude", String.valueOf(save.getLoggitude())
	        );
	        response.put("data", data);
	        response.put("message", "Location Updated");
	    } else {
	        response.put("message", "No Record Found for Customer");
	    }
	    return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, String>> getFirebaseDeviceToken(CustomerMain exitsCustomer, Map<String, String> request) {
		Map<String, String> response = new HashMap<>();
		String firebaseDeviceToken =  request.get("firebaseDeviceToken");		
		if (Objects.nonNull(exitsCustomer)) {
			exitsCustomer.setFirebasetokenCus(firebaseDeviceToken);
	        this.customerDao.save(exitsCustomer);
			response.put("message", "Firebase Device Token Store");
		} else {
			response.put("message", "No Record Found Agent");
		}
		return ResponseEntity.ok(response);
	}
}
