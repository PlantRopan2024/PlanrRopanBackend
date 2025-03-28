package com.plants.Service;

import java.math.BigDecimal;
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

import com.plants.Dao.AppRatingRepo;
import com.plants.Dao.CustomerDao;
import com.plants.Dao.OffersAppliedRepo;
import com.plants.Dao.userDao;
import com.plants.config.JwtUtil;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;
import com.plants.entities.AppRating;
import com.plants.entities.BookingRequest;
import com.plants.entities.CustomerMain;
import com.plants.entities.FertilizerRequest;
import com.plants.entities.Offers;
import com.plants.entities.OffersApplied;
import com.plants.entities.Plans;

@Service
public class CustomerService {
	
	@Value("${google.api.key}")
	private String googleApiKey;
	
	@Value("${google.url.address}")
	private String googleUrlAddress;
	
	@Value("${google.url.latidude}")
	private String getLatidudeOrLongitutdeUrl;
	
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
	private CustomerDao customerDao;
	
	@Autowired
	private AppRatingRepo appRatingRepo;

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

	public ResponseEntity<Map<String, Object>> getUpdateLiveLocationCust(CustomerMain exitsCustomer,Map<String, String> request) {
		Map<String, Object> response = new HashMap<>();
		double custLatitude = Double.parseDouble(request.get("custLatitude"));
		double custLongitude = Double.parseDouble(request.get("custLongtitude"));
		
		CustomerMain customerMain = null;
		if (Objects.nonNull(exitsCustomer)) {
			exitsCustomer.setLatitude(custLatitude);
			exitsCustomer.setLoggitude(custLongitude);
			exitsCustomer.setAddress(Utils.getAddressFromCoordinates(custLatitude, custLongitude, googleApiKey, googleUrlAddress));
			customerMain = this.customerDao.save(exitsCustomer);
		}
		
		List<AgentMain> activeAgents = this.userdao.activeAgent();

		if (!activeAgents.isEmpty()) {
			for (AgentMain agent : activeAgents) {
				if (agent.isActiveAgent()) {
					double arrivalTime = locationService.estimateArrivalTime(custLatitude, custLongitude,agent.getLatitude(), agent.getLongitude());
					int roundedTime = (int) Math.ceil(arrivalTime);
					if (arrivalTime != -1) {
							Map<String, String> data = Map.of("address", customerMain.getAddress(),
								"GardenerAvaliable", "Gardener avaliable in " + roundedTime + " minutes");
							response.put("data", data);
							response.put("status", true);
							response.put("message", "Location Updated");
						
						System.out.println("Gardener avaliable in " + roundedTime + " minutes");
					} else {
						response.put("status", true);
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
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			//Plans getPlan = this.customerDao.getPlansId(bookingRequest.getPlanId());
			//int serviceCharge = Integer.parseInt(getPlan.getPlansRs());
		//	double totalFertilizerCost = 0;
		//	List<String> fertilizerDetails = new ArrayList<>();
//			for (FertilizerRequest fertilizer : bookingRequest.getFertilizers()) {
//				String name = fertilizer.getName();
//				String cleanedName = removeQuantityFromName(fertilizer.getName()); // Remove quantity
//				fertilizerDetails.add(cleanedName + " - ₹" + fertilizer.getPrice() * fertilizer.getQuantity());
//				totalFertilizerCost += fertilizer.getPrice() * fertilizer.getQuantity();
//			}
			int platformFee = Integer.parseInt(platformFees);
		//	double gstAmount = (serviceCharge * Double.parseDouble(gstRate)) / 100.0;
		//	double grandTotal = serviceCharge + totalFertilizerCost + platformFee + gstAmount;
			Map<String, Object> data = new HashMap<>();
			data.put("Platform Fee", platformFee);	
			data.put("Gst Fee", Double.parseDouble(gstRate));			
			finalResponse.put("BillingDetails", data); 
			finalResponse.put("Offers", getDiscountOffers(exitsCustomer)); 
		//    finalResponse.put("GardeningLocation", getGardeningLocation(exitsCustomer));
		    finalResponse.put("status", true);
		}catch(Exception e) {
			e.printStackTrace();
			finalResponse.put("status", false);
			finalResponse.put("message", "Something Went Wrong");
		}
		return ResponseEntity.ok(finalResponse);
	}
	
	public ResponseEntity<Map<String, Object>> applyOffersCustomer(CustomerMain existingCustomer, Map<String, Object> request) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	String offerId = (String) request.get("offerId");
		    String typeMessage = (String) request.get("typeMessage");
		    Object grandTotalObj = request.get("grandTotal");
		    BigDecimal grandTotal = (grandTotalObj instanceof Number) 
		        ? BigDecimal.valueOf(((Number) grandTotalObj).doubleValue()) 
		        : BigDecimal.ZERO;
		    
	        if (offerId == null || offerId.isEmpty()) {
		        return Utils.createErrorResponse(response, "Offer ID is required");
		    }

		    Offers offer = offerService.getIdOffers(offerId);
		    if (offer == null) {
		        return Utils.createErrorResponse(response, "Invalid Offer ID");
		    }
		    
		    Map<String, Object> billingDetails = new HashMap<String, Object>();

		    OffersApplied existingAppliedOffer = offersAppliedRepo.getAppliedOffers(offer.getPrimarykey(), existingCustomer.getPrimarykey());

			if ("APPLY".equalsIgnoreCase(typeMessage)) {
		        if (existingAppliedOffer == null) {
		            OffersApplied appliedOffer = new OffersApplied();
		            appliedOffer.setCustomerMain(existingCustomer);
		            appliedOffer.setOfferStatus("APPLY");
		            appliedOffer.setAppTypeId("Customer");
		            appliedOffer.setOffers(offer);
		            offersAppliedRepo.save(appliedOffer);
		            // Subtract discount from Grand Total if the discount value is not null
		            grandTotal = grandTotal.subtract(BigDecimal.valueOf(offer.getDisAmountRs()));
		            billingDetails.put("Coupon Applied", offer.getDisAmountRs());
		            response.put("message", "Offer applied successfully.");
		        } else {
		        	grandTotal = grandTotal.subtract(BigDecimal.valueOf(offer.getDisAmountRs()));
		            billingDetails.put("Coupon Applied", offer.getDisAmountRs());
		            response.put("message", "You have already applied this offer.");
		        }
		    }

		    if ("REMOVE".equalsIgnoreCase(typeMessage)) {
		        if (existingAppliedOffer != null) {
		            offersAppliedRepo.deleteById(existingAppliedOffer.getPrimaryKey());
		            response.put("message", "Offer has been removed.");
		        }
		    }

		    billingDetails.put("Grand Total", String.format("%.2f", grandTotal));
		    response.put("BillingDetails", billingDetails);
		    response.put("status", true);
		    response.put("offerId", offerId);
		    response.put("typeMessage", typeMessage);

	    } catch (NumberFormatException e) {
	        return Utils.createErrorResponse(response, "Invalid Grand Total value");
	    } catch(Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Wrong");
		}
	   return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> appRatingServicesCus(CustomerMain existingCustomer, Map<String, Object> request) {
	    Map<String, Object> response = new HashMap<>();
	    try {
		    String comment = (String) request.get("comment");
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
	        appRating.setCustomerMain(existingCustomer);
	        appRating.setRating(rating);
	        appRating.setComment(comment);
	        appRatingRepo.save(appRating);

	        response.put("status", true);
	        response.put("message", "Thank You For Giving Rating");
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", false);
	        response.put("message", "Something went wrong, please try again later");
	    }

	    return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> changesAddessOrder(CustomerMain existingCustomer, Map<String, Object> request) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	double latitudeCus = (double) request.get("latitude");
	    	double longitudeCus = (double) request.get("longitude");
	    	String addressType = (String) request.get("addressType");
	    	String otherAddress = (String) request.get("otherAddress");

	    	if(addressType.equals("SELF_ADDRESS")) {
	    		Map<String, Object> gardeningLocation = getGardeningLocation(latitudeCus,longitudeCus);
	    		response.put("GardeningLocation", gardeningLocation);
	    		response.put("status", true);
	    	}
	    	
	    	if(addressType.equals("OTHER_ADDRESS")) {
	    		Map<String, Object> gardeningLocation = Utils.getCoordinates(getLatidudeOrLongitutdeUrl,googleApiKey,otherAddress);
	    		double latitude = (double) gardeningLocation.get("latitude");
	            double longitude = (double) gardeningLocation.get("longitude");
	    		Map<String, Object> gardeningLocationOther = getGardeningLocation(latitude,longitude);
	            response.put("GardeningLocation", gardeningLocationOther);
	    		response.put("status", true);
	    	}
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", false);
	        response.put("message", "Something went wrong, please try again later");
	    }

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
	
	private Map<String, Object> getGardeningLocation(double latitudeCus,double longitudeCus) {
	    List<AgentMain> activeAgents = this.userdao.activeAgent();
	    Map<String, Object> agentAvailResponse = new HashMap<>();

	    if (!activeAgents.isEmpty()) {
	        for (AgentMain agent : activeAgents) {
	            if (agent.isActiveAgent()) {
	                double arrivalTime = locationService.estimateArrivalTime(latitudeCus,longitudeCus,agent.getLatitude(),agent.getLongitude());
	                int roundedTime = (int) Math.ceil(arrivalTime);
	                if (arrivalTime != -1) {
	                    agentAvailResponse.put("GardenerAvailable", "Gardener available in " + roundedTime + " minutes");
	                } else {
	                    agentAvailResponse.put("GardenerAvailable", "Gardener is not available for your location");
	                }
	            }
	        }
	    }
	    return agentAvailResponse;
	}
	
	// discount offer 
	private List<Offers> getDiscountOffers(CustomerMain exitsCustomer) {
				List<Offers> getOffers = this.offerService.getAllOffersCusMobApi();
				List<Offers> unusedOffers = new ArrayList<>();
				for (Offers offer : getOffers) {
				    long appliedCount = this.offersAppliedRepo.countAppliedOffers(offer.getPrimarykey(), exitsCustomer.getPrimarykey());
				    if (appliedCount == 0) {
				        unusedOffers.add(offer);
				    }
				}
	    return unusedOffers;
	}
}
