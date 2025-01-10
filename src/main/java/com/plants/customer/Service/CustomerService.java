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

	public ResponseEntity<Map<String, Object>> ProfileInfoSaveCustomer(CustomerMain exitsCustomer, CustomerMain RequestcustomerMain) {
		Map<String, Object> response = new HashMap<>();
		try {

			if (Objects.nonNull(exitsCustomer)) {
				CustomerMain customermain = null;
				customermain = exitsCustomer; // Update existing customer
				customermain.setFirstName(RequestcustomerMain.getFirstName());
				customermain.setLastName(RequestcustomerMain.getLastName());
				customermain.setEmailId(RequestcustomerMain.getEmailId());
				customermain.setAddress(RequestcustomerMain.getAddress());
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
}
