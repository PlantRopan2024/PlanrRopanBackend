package com.plants.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.plants.Dao.AppRatingRepo;
import com.plants.Dao.CustomerDao;
import com.plants.Dao.OffersAppliedRepo;
import com.plants.Dao.OrderRepo;
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
import com.plants.entities.Order;
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
	private SmsService smsService;

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
	
	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	OfferService offerService;
	@Autowired
	OffersAppliedRepo offersAppliedRepo;

	public ResponseEntity<Map<String, String>> sentOtpCus(String mobileNumber) {
		Map<String, String> response = new HashMap<>();
		if (mobileNumber == null || mobileNumber.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Mobile number is required"));
		}
		String otp = otpService.generateOTP(mobileNumber);
		if (mobileNumber.equals("+917860487487")) {
			response.put("message", "OTP sent successfully!");
		} else {
			this.smsService.sendOtp(mobileNumber, otp);
		}
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
				customermain.setGender(RequestcustomerMain.getGender());
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
		CustomerMain customerMain = null;
		if (Objects.nonNull(exitsCustomer)) {
			exitsCustomer.setLatitude(custLatitude);
			exitsCustomer.setLoggitude(custLongitude);
			exitsCustomer.setAddress(
					Utils.getAddressFromCoordinates(custLatitude, custLongitude, googleApiKey, googleUrlAddress));
			customerMain = this.customerDao.save(exitsCustomer);
		}
		List<AgentMain> activeAgents = this.userdao.activeAgent();
		if (!activeAgents.isEmpty()) {
			List<Integer> validArrivalTimes = new ArrayList<>();
			boolean gardenerAvailable = false;
			for (AgentMain agent : activeAgents) {
				if (agent.isActiveAgent()) {
					double arrivalTime = locationService.estimateArrivalTime(custLatitude, custLongitude,
							agent.getLatitude(), agent.getLongitude());
					int roundedTime = (int) Math.ceil(arrivalTime);
					if (arrivalTime != -1) {
						validArrivalTimes.add(roundedTime);
						gardenerAvailable = true;
					}
				}
			}
			if (gardenerAvailable) {
				int minTime = Collections.min(validArrivalTimes); // Get the nearest gardener
				Map<String, String> data = Map.of("address", customerMain.getAddress(), "GardenerAvailable",
						"Gardener available in " + minTime + " minutes", "message", "Location Updated");
				response.put("data", data);
				response.put("status", true);
			} else {
				response.put("status", false);
				response.put("message", "Gardener is not available at your location");
			}
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, String>> getFirebaseDeviceToken(CustomerMain exitsCustomer,
			Map<String, String> request) {
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

	public ResponseEntity<Map<String, Object>> orderSummaryCalculation(CustomerMain exitsCustomer,
			BookingRequest bookingRequest) {
		Map<String, Object> finalResponse = new HashMap<>();
		try {
			// Plans getPlan = this.customerDao.getPlansId(bookingRequest.getPlanId());
			// int serviceCharge = Integer.parseInt(getPlan.getPlansRs());
			// double totalFertilizerCost = 0;
			// List<String> fertilizerDetails = new ArrayList<>();
//			for (FertilizerRequest fertilizer : bookingRequest.getFertilizers()) {
//				String name = fertilizer.getName();
//				String cleanedName = removeQuantityFromName(fertilizer.getName()); // Remove quantity
//				fertilizerDetails.add(cleanedName + " - ₹" + fertilizer.getPrice() * fertilizer.getQuantity());
//				totalFertilizerCost += fertilizer.getPrice() * fertilizer.getQuantity();
//			}
			int platformFee = Integer.parseInt(platformFees);
			// double gstAmount = (serviceCharge * Double.parseDouble(gstRate)) / 100.0;
			// double grandTotal = serviceCharge + totalFertilizerCost + platformFee +
			// gstAmount;
			Map<String, Object> data = new HashMap<>();
			data.put("Platform Fee", platformFee);
			data.put("Gst Fee", Double.parseDouble(gstRate));
			finalResponse.put("BillingDetails", data);
			finalResponse.put("Offers", getDiscountOffers(exitsCustomer));
			// finalResponse.put("GardeningLocation", getGardeningLocation(exitsCustomer));
			finalResponse.put("status", true);
		} catch (Exception e) {
			e.printStackTrace();
			finalResponse.put("status", false);
			finalResponse.put("message", "Something Went Wrong");
		}
		return ResponseEntity.ok(finalResponse);
	}

	public ResponseEntity<Map<String, Object>> applyCuopanCalulation(CustomerMain existingCustomer,
			Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> billingDetails = new HashMap<String, Object>();
		String offerCode = (String) request.get("offerCode");
		String typeMessage = (String) request.get("typeMessage");
		Object grandTotalObj = request.get("grandTotal");
		BigDecimal grandTotal = (grandTotalObj instanceof Number)
				? BigDecimal.valueOf(((Number) grandTotalObj).doubleValue())
				: BigDecimal.ZERO;
		try {
			if (offerCode == null || offerCode.isEmpty()) {
				billingDetails.put("Grand Total", grandTotal);
				billingDetails.put("Coupon Applied", 0.0);
				response.put("BillingDetails", billingDetails);
				response.put("OfferCode", offerCode);
				response.put("typeMessage", typeMessage);
				response.put("message", "Offer Code is Required");
				response.put("status", true);
				return ResponseEntity.ok(response);
			}

			Offers offer = offerService.getOffersCode(offerCode);
			if (offer == null) {
				billingDetails.put("Grand Total", grandTotal);
				billingDetails.put("Coupon Applied", 0.0);
				response.put("BillingDetails", billingDetails);
				response.put("OfferCode", offerCode);
				response.put("typeMessage", typeMessage);
				response.put("status", true);
				response.put("message", "Invalid Coupon");
				return ResponseEntity.ok(response);
			}

			if ("APPLY".equalsIgnoreCase(typeMessage)) {
				// Subtract discount from Grand Total if the discount value is not null
				grandTotal = grandTotal.subtract(BigDecimal.valueOf(offer.getDisAmountRs()));
				billingDetails.put("Grand Total", Double.parseDouble(String.format("%.2f", grandTotal)));
				billingDetails.put("Coupon Applied", offer.getDisAmountRs());
				response.put("message", "Offer applied successfully.");
			}

			if ("REMOVE".equalsIgnoreCase(typeMessage)) {
				billingDetails.put("Grand Total", grandTotal);
				billingDetails.put("Coupon Applied", 0.0);
				response.put("message", "Offer has been removed.");
			}

//			OffersApplied existingAppliedOffer = offersAppliedRepo.getAppliedOffers(offer.getPrimarykey(),existingCustomer.getPrimarykey());
//			if ("APPLY".equalsIgnoreCase(typeMessage)) {
//				if (existingAppliedOffer == null) {
//					OffersApplied appliedOffer = new OffersApplied();
//					appliedOffer.setCustomerMain(existingCustomer);
//					appliedOffer.setOfferStatus("APPLY");
//					appliedOffer.setAppTypeId("Customer");
//					appliedOffer.setOffers(offer);
//					offersAppliedRepo.save(appliedOffer);
//					// Subtract discount from Grand Total if the discount value is not null
//					grandTotal = grandTotal.subtract(BigDecimal.valueOf(offer.getDisAmountRs()));
//					billingDetails.put("Coupon Applied", offer.getDisAmountRs());
//					response.put("message", "Offer applied successfully.");
//				} else {
//					grandTotal = grandTotal.subtract(BigDecimal.valueOf(offer.getDisAmountRs()));
//					billingDetails.put("Coupon Applied", offer.getDisAmountRs());
//					response.put("message", "You have already applied this offer.");
//				}
//			}
//			if ("REMOVE".equalsIgnoreCase(typeMessage)) {
//				if (existingAppliedOffer != null) {
//					offersAppliedRepo.deleteById(existingAppliedOffer.getPrimaryKey());
//					billingDetails.put("Coupon Applied", 0.0);
//					response.put("message", "Offer has been removed.");
//				}
//			}
			response.put("BillingDetails", billingDetails);
			response.put("status", true);
			response.put("OfferCode", offer.getOfferCode());
			response.put("typeMessage", typeMessage);

		} catch (NumberFormatException e) {
			billingDetails.put("Grand Total", grandTotal);
			billingDetails.put("Coupon Applied", 0.0);
			response.put("BillingDetails", billingDetails);
			response.put("OfferCode", offerCode);
			response.put("typeMessage", typeMessage);
			response.put("status", true);
			response.put("message", "Invalid Grand Total value");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Wrong");
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> applyOffersCustomer(CustomerMain existingCustomer,
			Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> billingDetails = new HashMap<String, Object>();
		String offerCode = (String) request.get("offerCode");
		String typeMessage = (String) request.get("typeMessage");
		Object grandTotalObj = request.get("grandTotal");
		BigDecimal grandTotal = (grandTotalObj instanceof Number)
				? BigDecimal.valueOf(((Number) grandTotalObj).doubleValue())
				: BigDecimal.ZERO;
		try {
			if (offerCode == null || offerCode.isEmpty()) {
				response.put("message", "Offer Code is required");
				response.put("status", true);
				return ResponseEntity.ok(response);
			}

			Offers offer = offerService.getOffersCode(offerCode);
			if (offer == null) {
				billingDetails.put("Grand Total", grandTotal);
				billingDetails.put("Coupon Applied", 0.0);
				response.put("BillingDetails", billingDetails);
				response.put("status", true);
				response.put("OfferCode", offerCode);
				response.put("typeMessage", typeMessage);
				response.put("status", true);
				response.put("message", "Invalid Coupon");
				return ResponseEntity.ok(response);
			}

			OffersApplied existingAppliedOffer = offersAppliedRepo.getAppliedOffers(offer.getPrimarykey(),
					existingCustomer.getPrimarykey());
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
					billingDetails.put("Coupon Applied", 0.0);
					response.put("message", "Offer has been removed.");
				}
			}
			billingDetails.put("Grand Total", Double.parseDouble(String.format("%.2f", grandTotal)));
			response.put("BillingDetails", billingDetails);
			response.put("status", true);
			response.put("OfferCode", offer.getOfferCode());
			response.put("typeMessage", typeMessage);

		} catch (NumberFormatException e) {
			billingDetails.put("Grand Total", grandTotal);
			billingDetails.put("Coupon Applied", 0.0);
			response.put("BillingDetails", billingDetails);
			response.put("status", true);
			response.put("OfferCode", offerCode);
			response.put("typeMessage", typeMessage);
			response.put("status", true);
			response.put("message", "Invalid Grand Total value");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Wrong");
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> appRatingServicesCus(CustomerMain existingCustomer,
			Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		try {
			String comment = (String) request.get("comment");
			if (!request.containsKey("rating")) {
				response.put("status", false);
				response.put("message", "Rating is required");
				return ResponseEntity.badRequest().body(response);
			}

			double rating;
			try {
				rating = Double.parseDouble(request.get("rating").toString());
				// rating = Integer.parseInt(request.get("rating").toString());
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

	public ResponseEntity<Map<String, Object>> changesAddessOrder(CustomerMain existingCustomer,
			Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		try {
			double latitudeCus = (double) request.get("latitude");
			double longitudeCus = (double) request.get("longitude");
			String addressType = (String) request.get("addressType");
			String otherAddress = (String) request.get("otherAddress");

			if (addressType.equals("SELF_ADDRESS")) {
				Map<String, Object> gardeningLocation = getGardeningLocation(latitudeCus, longitudeCus);
				response.put("GardeningLocation", gardeningLocation);
				response.put("status", true);
			}
			if (addressType.equals("OTHER_ADDRESS")) {
				Map<String, Object> gardeningLocation = Utils.getCoordinates(getLatidudeOrLongitutdeUrl, googleApiKey,
						otherAddress);
				double latitude = (double) gardeningLocation.get("latitude");
				double longitude = (double) gardeningLocation.get("longitude");
				Map<String, Object> gardeningLocationOther = getGardeningLocation(latitude, longitude);
				gardeningLocationOther.put("latitude", latitude);
				gardeningLocationOther.put("longitude", longitude);
				response.put("GardeningLocation", gardeningLocationOther);
				response.put("status", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", true);
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

	private Map<String, Object> getGardeningLocation(double latitudeCus, double longitudeCus) {
		List<AgentMain> activeAgents = this.userdao.activeAgent();
		Map<String, Object> agentAvailResponse = new HashMap<>();
		if (!activeAgents.isEmpty()) {
			List<Integer> validArrivalTimes = new ArrayList<>();
			for (AgentMain agent : activeAgents) {
				if (agent.isActiveAgent()) {
					System.out.println("Agent name: " + agent.getFirstName());
					double arrivalTime = locationService.estimateArrivalTime(latitudeCus, longitudeCus,
							agent.getLatitude(), agent.getLongitude());

					int roundedTime = (int) Math.ceil(arrivalTime);
					if (arrivalTime != -1) {
						validArrivalTimes.add(roundedTime);
					}
				}
			}
			// Check if any valid gardeners are available
			if (!validArrivalTimes.isEmpty()) {
				int minTime = Collections.min(validArrivalTimes); // Find the shortest arrival time
				agentAvailResponse.put("GardenerAvailable", "Gardener available in " + minTime + " minutes");
				agentAvailResponse.put("Avalibality", true);
			} else {
				agentAvailResponse.put("GardenerAvailable", "Gardener is not available for your location");
				agentAvailResponse.put("Avalibality", false);
			}
		}
		return agentAvailResponse;
	}

	// discount offer
	private List<Offers> getDiscountOffers(CustomerMain exitsCustomer) {
		List<Offers> getOffers = this.offerService.getAllOffersCusMobApi();
		List<Offers> unusedOffers = new ArrayList<>();
		for (Offers offer : getOffers) {
			long appliedCount = this.offersAppliedRepo.countAppliedOffers(offer.getPrimarykey(),
					exitsCustomer.getPrimarykey());
			if (appliedCount == 0) {
				unusedOffers.add(offer);
			}
		}
		return unusedOffers;
	}

	public ResponseEntity<Map<String, Object>> getActiveOrder(CustomerMain exitsCustomer, int pageNumber, int pageSize,
			String baseUrl) {
		Map<String, Object> response = new HashMap<>();

		try {
			int pageIndex = Math.max(pageNumber - 1, 0);
			Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending());
			Page<Order> getOrdersDetails = orderRepo.geActiveOrderCustomer(exitsCustomer.getPrimarykey(), pageable);

			if (getOrdersDetails.isEmpty()) {
				List<Map<String, Object>> emptyOrdersList = new ArrayList<>();
				Map<String, Object> pagination = Utils.buildPaginationResponse(getOrdersDetails, baseUrl, pageSize,
						emptyOrdersList);
				pagination.put("message", "You have no orders.");
				response.put("success", true);
				response.put("response", pagination);
				return ResponseEntity.ok(response);
			}
			List<Map<String, Object>> ordersList = new ArrayList<>();
			for (Order order : getOrdersDetails) {
				Map<String, Object> orderDetails = new HashMap<>();
				orderDetails.put("OrderNumber", order.getOrderId());
				orderDetails.put("order_status", order.getOrderStatus());
				orderDetails.put("address", order.getAddress());
				orderDetails.put("date", Utils.formatDateTime(order.getCreatedAt()));
				orderDetails.put("PlanName", order.getPlans().getPlansName());
				orderDetails.put("serviceName", order.getPlans().getServicesName().getName());
				orderDetails.put("PlanPrice", order.getPlans().getPlansRs());
				orderDetails.put("status", true);
				ordersList.add(orderDetails);
			}
			Map<String, Object> pagination = Utils.buildPaginationResponse(getOrdersDetails, baseUrl, pageSize,
					ordersList);
			response.put("success", true);
			response.put("response", pagination);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Something went wrong.");
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> getCompeletedOrder(CustomerMain exitsCustomer, int pageNumber,
			int pageSize, String baseUrl) {
		Map<String, Object> response = new HashMap<>();

		try {
			int pageIndex = Math.max(pageNumber - 1, 0);
			Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending());
			Page<Order> getOrdersDetails = orderRepo.getCompletedOrderCus(exitsCustomer.getPrimarykey(), pageable);

			if (getOrdersDetails.isEmpty()) {
				List<Map<String, Object>> emptyOrdersList = new ArrayList<>();
				Map<String, Object> pagination = Utils.buildPaginationResponse(getOrdersDetails, baseUrl, pageSize,
						emptyOrdersList);
				pagination.put("message", "You have No Order.");
				response.put("success", true);
				response.put("response", pagination);
				return ResponseEntity.ok(response);
			}
			List<Map<String, Object>> ordersList = new ArrayList<>();
			for (Order order : getOrdersDetails) {
				Map<String, Object> orderDetails = new HashMap<>();
				orderDetails.put("OrderNumber", order.getOrderId());
				orderDetails.put("order_status", order.getOrderStatus());
				orderDetails.put("date", Utils.formatDateTime(order.getCreatedAt()));
				orderDetails.put("address", order.getAddress());
				orderDetails.put("PlanName", order.getPlans().getPlansName());
				orderDetails.put("ServiceName", order.getPlans().getServicesName().getName());
				orderDetails.put("PlanPrice", order.getPlans().getPlansRs());
				ordersList.add(orderDetails);
			}
			Map<String, Object> pagination = Utils.buildPaginationResponse(getOrdersDetails, baseUrl, pageSize,
					ordersList);
			response.put("success", true);
			response.put("response", pagination);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Something went wrong.");
		}
		return ResponseEntity.ok(response);
	}
}
