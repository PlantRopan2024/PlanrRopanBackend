package com.plants.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.plants.Dao.CustomerDao;
import com.plants.Dao.OffersAppliedRepo;
import com.plants.Dao.userDao;
import com.plants.config.JwtUtil;
import com.plants.entities.AgentMain;
import com.plants.entities.BookingRequest;
import com.plants.entities.CustomerMain;
import com.plants.entities.FertilizerRequest;
import com.plants.entities.Offers;
import com.plants.entities.OffersApplied;
import com.plants.entities.Plans;

@Service
public class CustomerService {

	@Value("${platform.fee}")
	private String platformFees;

	@Value("${gst.rate}")
	private String gstRate;

	@Autowired
	private OTPService otpService;

	@Autowired
	userDao userdao;

	@Autowired
	LocationService locationService;
	@Autowired
	private SmsService smsService;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired OfferService offerService;
	@Autowired OffersAppliedRepo offersAppliedRepo;

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
				customerMain.setCusReferralCode(generateReferralCodeCus());
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

	public ResponseEntity<Map<String, Object>> getUpdateLiveLocationCust(CustomerMain exitsCustomer,
			Map<String, String> request) {
		Map<String, Object> response = new HashMap<>();
		double custLatitude = Double.parseDouble(request.get("custLatitude"));
		double custLongitude = Double.parseDouble(request.get("custLongtitude"));
		String city = request.get("city");
		String address = request.get("address");
		double speedKmPerHour = 25.0;
		List<AgentMain> activeAgents = this.userdao.activeAgent();

		if (!activeAgents.isEmpty()) {
			for (AgentMain agent : activeAgents) {
				if (agent.isActiveAgent()) {
					double arrivalTime = locationService.estimateArrivalTime(custLatitude, custLongitude,
							agent.getLatitude(), agent.getLongitude(), speedKmPerHour);
					int roundedTime = (int) Math.ceil(arrivalTime);
					if (arrivalTime != -1) {
						if (Objects.nonNull(exitsCustomer)) {
							exitsCustomer.setLatitude(custLatitude);
							exitsCustomer.setLoggitude(custLongitude);
							exitsCustomer.setAddress(address);
							exitsCustomer.setCity(city);
							CustomerMain save = this.customerDao.save(exitsCustomer);
							Map<String, String> data = Map.of("address", save.getAddress(), "city", save.getCity(),
									"custLatitude", String.valueOf(save.getLatitude()), "custLongtitude",
									String.valueOf(save.getLoggitude()), "GardenerAvaliable",
									"Gardener avaliable in " + roundedTime + " minutes");
							response.put("data", data);
							response.put("status", "true");
							response.put("message", "Location Updated");
						}
						System.out.println("Gardener avaliable in " + roundedTime + " minutes");
					} else {
						response.put("status", "false");
						response.put("message", "Gardener is not Avaliable for Your Location");
					}
				}
			}
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, String>> getFirebaseDeviceToken(CustomerMain exitsCustomer,Map<String, String> request) {
		Map<String, String> response = new HashMap<>();
		String firebaseDeviceToken = request.get("firebaseDeviceToken");
		if (Objects.nonNull(exitsCustomer)) {
			exitsCustomer.setFirebasetokenCus(firebaseDeviceToken);
			this.customerDao.save(exitsCustomer);
			response.put("status", "true");
			response.put("message", "Firebase Device Token Store");
		} else {
			response.put("status", "false");
			response.put("message", "No Record Found Agent");
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> orderSummaryCalculation(CustomerMain exitsCustomer,BookingRequest bookingRequest) {
		System.out.println("Booking received: " + bookingRequest);
		Map<String, Object> finalResponse = new HashMap<>();
		Plans getPlan = this.customerDao.getPlansId(bookingRequest.getPlanId());
		int serviceCharge = Integer.parseInt(getPlan.getPlansRs());
		double totalFertilizerCost = 0;
		List<String> fertilizerDetails = new ArrayList<>();
		for (FertilizerRequest fertilizer : bookingRequest.getFertilizers()) {
			String name = fertilizer.getName();
			String cleanedName = removeQuantityFromName(fertilizer.getName()); // Remove quantity
			fertilizerDetails.add(cleanedName + " - ₹" + fertilizer.getPrice() * fertilizer.getQuantity());
			totalFertilizerCost += fertilizer.getPrice() * fertilizer.getQuantity();
		}
		int platformFee = Integer.parseInt(platformFees);

		double gstAmount = (serviceCharge * Double.parseDouble(gstRate)) / 100.0;
		double grandTotal = serviceCharge + totalFertilizerCost + platformFee + gstAmount;

		Map<String, Object> data = new HashMap<>();
		data.put("Service Charge", "₹" + serviceCharge);
		data.put("Fertilizer", "₹" + (int) totalFertilizerCost);
		data.put("Fertilizer Details", fertilizerDetails);
		data.put("Platform Fee", "₹" + platformFee);
		data.put("GST 18%", "₹" + String.format("%.2f", gstAmount));
		data.put("Grand Total", "₹" + String.format("%.2f", grandTotal));
		
		
		finalResponse.put("BillingDetails", data); 
		
		// discount offer 
		List<Offers> getOffers = this.offerService.getAllOffersCusMobApi();
		List<Offers> unusedOffers = new ArrayList<>();
		for (Offers offer : getOffers) {
		    long appliedCount = this.offersAppliedRepo.countAppliedOffers(offer.getPrimarykey(), exitsCustomer.getPrimarykey());
		    if (appliedCount == 0) {
		        unusedOffers.add(offer);
		    }
		}
		
		finalResponse.put("Offers", unusedOffers); 

		return ResponseEntity.ok(finalResponse);
	}
	
	public ResponseEntity<Map<String, Object>> applyOffesCustomer(CustomerMain exitsCustomer, Map<String, Object> request) {
	    Map<String, Object> response = new HashMap<>();

	    // Extracting values from the request
	    String offerId = (String) request.get("offerId");
	    String typeMessage = (String) request.get("typeMessage");

	    // Extract BillingDetails safely
	    Map<String, Object> billingDetails = (Map<String, Object>) request.get("BillingDetails");

	    // Ensure BillingDetails exists before accessing it
	    if (billingDetails == null || !billingDetails.containsKey("Grand Total")) {
	        response.put("status", false);
	        response.put("message", "Billing details are missing or incorrect");
	        return ResponseEntity.ok(response);
	    }

	    // Convert Grand Total to Double for calculations
	    double grandTotal = Double.parseDouble(billingDetails.get("Grand Total").toString());

	    if (offerId == null || offerId.isEmpty()) {
	        response.put("status", false);
	        response.put("message", "Offer ID is required");
	        return ResponseEntity.ok(response);
	    }

	    // Fetch Offer Details
	    Offers getOfferId = this.offerService.getIdOffers(offerId);
	    
	    if (getOfferId == null) {
	        response.put("status", false);
	        response.put("message", "Invalid Offer ID");
	        return ResponseEntity.ok(response);
	    }

	    if ("USED".equals(typeMessage)) {
	        OffersApplied offersApplied = new OffersApplied();
	        offersApplied.setCustomerMain(exitsCustomer);
	        offersApplied.setOfferStatus("USED");
	        offersApplied.setAppTypeId("Customer");
	        offersApplied.setOffers(getOfferId);
	        this.offersAppliedRepo.save(offersApplied);

	        // Subtract the discount from Grand Total
	        grandTotal -= getOfferId.getDisAmountRs();
	    }

	    // Update response with new values
	    response.put("status", true);
	    response.put("message", "Offer applied successfully.");
	    response.put("offerId", offerId);
	    response.put("typeMessage", typeMessage);

	    // Update Billing Details with new Grand Total
	    billingDetails.put("Grand Total", String.format("₹%.2f", grandTotal));
	    response.put("BillingDetails", billingDetails);

	    return ResponseEntity.ok(response);
	}


	private String extractQuantity(String name) {
		Pattern pattern = Pattern.compile("\\((.*?)\\)"); // Match text inside parentheses
		Matcher matcher = pattern.matcher(name);

		if (matcher.find()) {
			return matcher.group(1); // Extract "5 kg", "1 kg", etc.
		}
		return ""; // Return empty if no match
	}

	private String removeQuantityFromName(String name) {
		return name.replaceAll("\\(\\d+ kg\\)", "").trim(); // Remove "(5 kg)" and trim extra spaces
	}
	
	private String generateReferralCodeCus() {
		return "cus50" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
	}
}
