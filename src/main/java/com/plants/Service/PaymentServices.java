package com.plants.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.plants.Dao.CustomerDao;
import com.plants.Dao.GardnerRatingRepo;
import com.plants.Dao.OffersAppliedRepo;
import com.plants.Dao.OrderEarningRepo;
import com.plants.Dao.OrderFertilizersRepo;
import com.plants.Dao.OrderRepo;
import com.plants.Dao.PaymentRepo;
import com.plants.Dao.RejectedOrdersRepo;
import com.plants.Dao.WorkCompletedPhotoRepo;
import com.plants.Dao.userDao;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;
import com.plants.entities.CustomerMain;
import com.plants.entities.FertilizerRequest;
import com.plants.entities.GardnerRating;
import com.plants.entities.Offers;
import com.plants.entities.OffersApplied;
import com.plants.entities.Order;
import com.plants.entities.OrderEarning;
import com.plants.entities.OrderFertilizers;
import com.plants.entities.OrderSummaryRequest;
import com.plants.entities.Payment;
import com.plants.entities.PaymentRequest;
import com.plants.entities.Plans;
import com.plants.entities.RejectedOrders;
import com.plants.entities.WorkCompletedPhoto;

@Service
public class PaymentServices {

	@Value("${file.upload-dir}")
	private String uploadDir;
	
	@Value("${pagination.page}")
	private int pageNumber;
	
	@Value("${pagination.size}")
	private int pageSize;
	
	@Value("${gst.rate}")
	private int gstRate;    
	
	@Value("${margin.each.fertilizer}")
	private int marginEachFertilizer;
	
	@Value("${margin.serviceCharges.per.order}")
	private int marginServiceChargesPerOrderPercent;

	@Value("${reached.location.meter}")
	private double reachedLocation;
	
	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	private OrderFertilizersRepo orderFertilizersRepo;

	@Autowired
	userDao userdao;

	@Autowired
	LocationService locationService;

	@Autowired
	WorkCompletedPhotoRepo workCompletedPhotoRepo;

	@Autowired
	GardnerRatingRepo gardnerRatingRepo;
	
	@Autowired 
	private RejectedOrdersRepo rejectedOrdersRepo;
	
	@Autowired
	private OrderEarningRepo orderEarningRepo;
	
	@Autowired
	OfferService offerService;
	
	@Autowired
	OffersAppliedRepo offersAppliedRepo;
	
	private final List<Map<String, Object>> upComingOrdersStored = new ArrayList<>();


	public ResponseEntity<Map<String, Object>> paymentInitiate(CustomerMain exitsCustomer, PaymentRequest request) {
		Map<String, Object> response = new HashMap<>();
		// String merchantVpa = "merchant@upi"; // Replace with actual UPI ID
		// String merchantName = "Your Business";

		// we get plans form Database
		try {
			Plans selectedPlan = this.customerDao.getPlansId(request.getPlanId());
			if (selectedPlan == null) {
				response.put("status", false);
				response.put("message", "Plans are not avaialbe");
				return ResponseEntity.ok(response);
			}
			System.out.println("  offer cide  = "+ request.getofferCode());
			Offers offer = offerService.getOffersCode(request.getofferCode());
			if (offer == null) {
				response.put("status", false);
				response.put("message", "Offers code are not avaialbe");
				return ResponseEntity.ok(response);
			}
			
			OrderSummaryRequest orderSummaryRequest = request.getOrderSummaryRequest();

			// payment process starting
			// Construct UPI Deep Link
			/*
			 * String upiUrl = "upi://pay?pa=" + merchantVpa + "&pn=" + merchantName +
			 * "&tr=" + orderId + "&tn=Payment for Gardening Service" + "&am=" +
			 * request.getOrderSummaryRequest().getGrandTotal() + "&cu=INR";
			 */

			Order orders = new Order();
			orders.setCustomerMain(exitsCustomer);
			 String orderNumber = generateOrderId();
			orders.setOrderId(orderNumber);
	//		orders.setTotalAmount(orderSummaryRequest.getGrandTotal());
		//	orders.setCoupanAmount(orderSummaryRequest.getCoupanAmount());
			orders.setCouponApplied(orderSummaryRequest.isCouponApplied());
		//	orders.setGstAmount(orderSummaryRequest.getGstAmount());
			orders.setOfferCode(request.getofferCode()); // save offercode customer choose
			orders.setPlans(selectedPlan); // save plans book customer choose
		//	orders.setPlatformfees(orderSummaryRequest.getPlatformfees());
		//	orders.setServicesCharges(orders.getServicesCharges());
			orders.setCreatedAt(LocalDateTime.now());
			orders.setAddress(request.getAddress());
			orders.setLatitude(request.getLatitude());
			orders.setLongtitude(request.getLongtitude());
			orders.setOrderStatus("NOT_ACCPETED");

			Order saveOrders = this.orderRepo.save(orders);

			// fertilizers save
//			for (FertilizerRequest fertilizerRequest : request.getOrderSummaryRequest().getFertilizers()) {
//				OrderFertilizers ordersFertilizers = new OrderFertilizers();
//				ordersFertilizers.setFertilizerName(fertilizerRequest.getName());
//				ordersFertilizers.setAmount(fertilizerRequest.getPrice());
//				ordersFertilizers.setKg(fertilizerRequest.getQuantity());
//				ordersFertilizers.setOrders(saveOrders);
//				this.orderFertilizersRepo.save(ordersFertilizers);
//			}

			// then payment save objects
			Payment payment = new Payment();
			payment.setOrder(saveOrders);
			payment.setPaymentMethod(request.getPaymentMethod());
			payment.setCustomerMain(exitsCustomer);
			payment.setCreatedAt(LocalDateTime.now());
			payment.setPaymentStatus("SUCCESS");
			Payment savePayment = this.paymentRepo.save(payment);
			// response.put("orderId", orderId);
			// response.put("paymentUrl", upiUrl);
			
			
			// per order earning 
			
			// calculate order earning method use
			
			calculateOrderEarning(request,saveOrders);
			// coupun applied by the customer
			appliedOffers(request,saveOrders,offer,exitsCustomer);
			

		//	response.put("amount", saveOrders.getTotalAmount());
			// send notification to agent
			ResponseEntity<Map<String, Object>> orderAssignedNotify = OrderAssignedNotify(exitsCustomer,orderNumber);
			response.put("orderAssignedNotify",orderAssignedNotify.getBody());
			response.put("paymentMethod", savePayment.getPaymentMethod());
			response.put("customerName", exitsCustomer.getFirstName() + " " + exitsCustomer.getLastName());
			response.put("paymentStatus", savePayment.getPaymentStatus());
			response.put("status", true);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Worng");
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> OrderAssignedNotify(CustomerMain exitsCustomer,String orderNumber) {
		Map<String, Object> response = new HashMap<>();
		String notify = "";
		int roundedTime = 0;
		double distanceKm = 0.0;
		try {
			Order getOrdersDetails = this.orderRepo.getOrderNumber(orderNumber);
			List<AgentMain> activeAgents = this.userdao.activeAgent();
			System.out.println("activeAgents size " + activeAgents.size());

			// List to keep track of agents within 5 km range
			List<AgentMain> agentsWithinRange = new ArrayList<>();

			// If there are active agents
			if (!activeAgents.isEmpty()) {
				for (AgentMain agent : activeAgents) {
					if (agent.isActiveAgent()) {
						// Check if the agent is within the 5 km range
						boolean isWithinRange = this.locationService.isWithinRange(getOrdersDetails.getLatitude(),
								getOrdersDetails.getLongtitude(), agent.getLatitude(), agent.getLongitude());

						if (isWithinRange) {
							double getEstimatetime = this.locationService.estimateArrivalTime(getOrdersDetails.getLatitude(), getOrdersDetails.getLongtitude(),
									agent.getLatitude(), agent.getLongitude());
							roundedTime = (int) Math.ceil(getEstimatetime);

							System.out.println(" get estimate time --- " + roundedTime);

							distanceKm = this.locationService.calculateDistance(getOrdersDetails.getLatitude(),getOrdersDetails.getLongtitude(), agent.getLatitude(), agent.getLongitude());
							System.out.println(" distance km -- " + distanceKm);

							// Add agent to the list if they are within the range
							agentsWithinRange.add(agent);
							System.out.println("Agent within range: " + agent.getFirstName());

							String message = "Reminder Alert!\n\n" + "Hey " + agent.getFirstName() + " "
									+ agent.getLastName() + " it's time to nurture some greens! 🌱\n"
									+ "You have a scheduled service today. Check your app for details and get ready to bring life to another garden! 🌿✨";

							// notification send firebase with help
							notify = sendNotificationToAgent(agent, null, "Service Alert: New Order", message,"Order",orderNumber);
							System.out.println(" notify----" + notify);
						}
					}
				}
			}
			// Prepare the response
			response.put("message", "Data received successfully");

			if (!agentsWithinRange.isEmpty()) {
				System.out.println("Total agents within 5 km range: " + agentsWithinRange.size());

//				Map<String, Object> CustomerDetails = new HashMap<String, Object>();
//				CustomerDetails.put("customerName", exitsCustomer.getFirstName() + " " + exitsCustomer.getLastName());
//				CustomerDetails.put("location", getOrdersDetails.getAddress());
//				CustomerDetails.put("latitudeCus", getOrdersDetails.getLatitude());
//				CustomerDetails.put("arrivalTime", roundedTime);
//				CustomerDetails.put("distanceKm", Utils.decimalFormat(distanceKm) + " KM");
//				CustomerDetails.put("longtitudeCus", getOrdersDetails.getLongtitude());

//				Map<String, Object> plansDetails = new HashMap<String, Object>();
//				plansDetails.put("planName", getOrdersDetails.getPlans().getPlansName());
//				plansDetails.put("planRs", getOrdersDetails.getPlans().getPlansRs());
//				plansDetails.put("fertilizer", getOrdersDetails.getOrderFertilizers());
				
				
				
				response.put("notify", notify);
				response.put("OrderNumber", getOrdersDetails.getOrderId());
				response.put("order_Status", getOrdersDetails.getOrderStatus());
				response.put("date", Utils.formatDateTime(getOrdersDetails.getCreatedAt()));
				response.put("Location", getOrdersDetails.getAddress());
				response.put("Latitude", getOrdersDetails.getLatitude());
				response.put("Longitude", getOrdersDetails.getLongtitude());
				response.put("Km", Utils.decimalFormat(distanceKm) + " KM");
				response.put("PlanName", getOrdersDetails.getPlans().getPlansName());
				response.put("PlanPrice", getOrdersDetails.getPlans().getPlansRs());
				response.put("workingTime",getOrdersDetails.getPlans().getTimeDuration());
				response.put("notificationKey", "Order");
			//	response.put("CustomerDetails", CustomerDetails);
			//	response.put("PlansDetails", plansDetails);
				response.put("status", true);
				
				
				 // Store upcoming orders without overwriting previous ones
	            synchronized (upComingOrdersStored) {
	                upComingOrdersStored.add(response);
	            }
			} else {
				System.out.println("No agents found within 5 km range.");
				response.put("status", false);
				response.put("agentsWithinRange", "No agents found within range");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Worng");
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> upComingOrders(AgentMain agentRecords, int pageNumber, int pageSize, String baseUrl) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        synchronized (upComingOrdersStored) {
	            List<Map<String, Object>> ordersList = new ArrayList<>(upComingOrdersStored); // Create a copy to avoid thread issues
	            // Sort by date in descending order (latest first)
	            ordersList.sort((o1, o2) -> {
	                try {
	                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a dd-MM-yyyy", Locale.ENGLISH);
	                    LocalDateTime d1 = LocalDateTime.parse(o1.get("date").toString(), formatter);
	                    LocalDateTime d2 = LocalDateTime.parse(o2.get("date").toString(), formatter);
	                    return d2.compareTo(d1); // Descending order
	                } catch (Exception e) {
	                    e.printStackTrace(); // Log for debugging
	                    return 0;
	                }
	            });

	            // Handle empty list
	            if (ordersList.isEmpty()) {
	                List<Map<String, Object>> emptyOrdersList = new ArrayList<>();
	                Map<String, Object> pagination = Utils.buildPaginationResponse(ordersList, baseUrl, pageNumber, pageSize, emptyOrdersList);
	                pagination.put("message", "You have no upcoming orders.");

	                response.put("success", true);
	                response.put("response", pagination);
	                return ResponseEntity.ok(response);
	            }

	            // Build paginated response
	            Map<String, Object> pagination = Utils.buildPaginationResponse(ordersList, baseUrl, pageNumber, pageSize, ordersList);
	            pagination.put("message", "Upcoming orders available.");

	            response.put("success", true);
	            response.put("response", pagination);
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // log exception
	        response.put("success", false);
	        response.put("message", "Something went wrong.");
	    }

	    return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> checkOrderAssigned(CustomerMain exitsCustomer, Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		String OrderNumber = (String) request.get("OrderNumber");
		try{
			Order getOrdersDetails = this.orderRepo.getOrderNumber(OrderNumber);

			// Prepare the response
			if (Objects.nonNull(getOrdersDetails.getAgentMain())) {
				double getEstimatetime = this.locationService.estimateArrivalTime(getOrdersDetails.getLatitude(),
						getOrdersDetails.getLongtitude(), getOrdersDetails.getAgentMain().getLatitude(),
						getOrdersDetails.getAgentMain().getLongitude());
				int roundedTime = (int) Math.ceil(getEstimatetime);

				System.out.println(" get estimate time --- " + roundedTime);

				// order assigned has been get all details
				Map<String, Object> agentDetails = new HashMap<String, Object>();
				agentDetails.put("agentName", "I'm " + getOrdersDetails.getAgentMain().getFirstName() + " "
						+ getOrdersDetails.getAgentMain().getLastName() + ", your gardner");
				agentDetails.put("agentMobileNo", getOrdersDetails.getAgentMain().getMobileNumber());
				agentDetails.put("otpCode", getOrdersDetails.getShareCode());
				agentDetails.put("arrivalTime", "Arriving in " + roundedTime + " minutes");

				Map<String, Object> CustomerDetails = new HashMap<String, Object>();
				CustomerDetails.put("customerName", exitsCustomer.getFirstName() + " " + exitsCustomer.getLastName());
				CustomerDetails.put("location", getOrdersDetails.getAddress());
				CustomerDetails.put("latitudeCus", getOrdersDetails.getLatitude());
				CustomerDetails.put("longtitudeCus", getOrdersDetails.getLongtitude());

//				Map<String, Object> plansDetails = new HashMap<String, Object>();
//				plansDetails.put("planName", getOrdersDetails.getPlans().getPlansName());
//				plansDetails.put("planRs", getOrdersDetails.getPlans().getPlansRs());
//				plansDetails.put("fertilizer", getOrdersDetails.getOrderFertilizers());

				response.put("OrderNumber", getOrdersDetails.getOrderId());
				response.put("order_Status", getOrdersDetails.getOrderStatus());
				response.put("date",Utils.formatDateTime(getOrdersDetails.getCreatedAt()));
				response.put("CustomerDetails", CustomerDetails);
				response.put("agentDetails", agentDetails);
//				response.put("PlansDetails", plansDetails);
				response.put("status", true);

			} else {
				response.put("message", "Please wait for this Order gardner assigning");
				response.put("status", true);
			}
			// response.put("agentsWithinRange", agentsWithinRange); // Send the list of
			// agents within range
		}catch(Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Worng");
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> accpetedOrder(AgentMain agentMain, Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		String OrderNumber = (String) request.get("OrderNumber");
		String bookingStatus = (String) request.get("BookingStatus");
		try {
			if (bookingStatus.equals("ACCEPT")) {
				Order getOrdersDetails = this.orderRepo.getOrderNumber(OrderNumber);
				
				// if order has been assigned for some one 
				
				if(getOrdersDetails.getOrderStatus().equals("ACCEPTED")) {
					synchronized (upComingOrdersStored) {
					    boolean removed = upComingOrdersStored.removeIf(order -> 
					        OrderNumber.equals(order.get("OrderNumber")) // Compare OrderNumber correctly
					    );

					    if (removed) {
					        System.out.println("Order " + OrderNumber + " removed from upComingOrdersStored.");
					    } else {
					        System.out.println("Order " + OrderNumber + " not found in upComingOrdersStored.");
					    }
					}
					response.put("message","This order has been expire");
					response.put("status", false);
					return ResponseEntity.ok(response);
				}
				
				getOrdersDetails.setOrderStatus("ACCEPTED");
				getOrdersDetails.setAgentMain(agentMain);
				double distanceKm = this.locationService.calculateDistance(getOrdersDetails.getLatitude(),getOrdersDetails.getLongtitude(), agentMain.getLatitude(), agentMain.getLongitude());
				getOrdersDetails.setKm(distanceKm);
				Random random = new Random();
				int code = 1000 + random.nextInt(9000); // Ensures a 4-digit number (1000-9999)
				getOrdersDetails.setShareCode(String.valueOf(code));
				Order saveOrders = this.orderRepo.save(getOrdersDetails);
				CustomerMain customerMain = this.customerDao.findbyPrimaryKey(saveOrders.getCustomerMain().getPrimarykey());
				// send notification to customer your order has been accpeted
			//	String notify = sendNotificationToAgent(null, customerMain, "You Order have been assigned",
				//		"Please check the app for details.", "OrderRecived",OrderNumber);
				
				Map<String, Object> orderDetails = new HashMap<String, Object>();
				orderDetails.put("OrderNumber", saveOrders.getOrderId());
				orderDetails.put("order_status", saveOrders.getOrderStatus());
				orderDetails.put("date", Utils.formatDateTime(saveOrders.getCreatedAt()));

				Map<String, Object> CustomerDetails = new HashMap<String, Object>();
				CustomerDetails.put("customerName", customerMain.getFirstName() + " " + customerMain.getLastName());
				CustomerDetails.put("address", getOrdersDetails.getAddress());
				CustomerDetails.put("latitudeCus", getOrdersDetails.getLatitude());
				CustomerDetails.put("longtitudeCus", getOrdersDetails.getLongtitude());
				CustomerDetails.put("mobileNUmber", customerMain.getMobileNumber());

				Map<String, Object> plansDetails = new HashMap<String, Object>();
				plansDetails.put("planName", getOrdersDetails.getPlans().getPlansName());
				plansDetails.put("planRs", getOrdersDetails.getPlans().getPlansRs());
				plansDetails.put("pots", getOrdersDetails.getPlans().getUptoPots());
				plansDetails.put("plansDuration", getOrdersDetails.getPlans().getTimeDuration());
				plansDetails.put("frequency", "1 times");
				 // Fertilizer details
//		        List<Map<String, Object>> fertilizerList = new ArrayList<>();
//		        for (OrderFertilizers orderfer : getOrdersDetails.getOrderFertilizers()) {
//		            Map<String, Object> fertilizerMap = new HashMap<>();
//		            fertilizerMap.put("fertilizerName", orderfer.getFertilizerName());
//		            fertilizerMap.put("earningMalliFertilizer", orderfer.getAmount()- marginEachFertilizer);
//		            fertilizerMap.put("quantity", orderfer.getKg());
//		            fertilizerList.add(fertilizerMap);
//		        }

		      //  response.put("fertilizerDetails", fertilizerList);
				response.put("OderDetails", orderDetails);
				response.put("customerDetails", CustomerDetails);
				response.put("PlansDetails", plansDetails);
			//	response.put("notify", notify);
				response.put("status", true);
				response.put("message", "Order has been Assigned ");
				
				synchronized (upComingOrdersStored) {
				    boolean removed = upComingOrdersStored.removeIf(order -> 
				        OrderNumber.equals(order.get("OrderNumber")) // Compare OrderNumber correctly
				    );

				    if (removed) {
				        System.out.println("Order " + OrderNumber + " removed from upComingOrdersStored.");
				    } else {
				        System.out.println("Order " + OrderNumber + " not found in upComingOrdersStored.");
				    }
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Worng");
		}
		return ResponseEntity.ok(response);
	}
	
	
	public ResponseEntity<Map<String, Object>> getListAccpetOrder(AgentMain agentRecords, int pageNumber, int pageSize, String baseUrl) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        int pageIndex = Math.max(pageNumber - 1, 0);
	        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending());
	        Page<Order> getOrdersDetails = orderRepo.getOrderAssignedListPaganation(agentRecords.getAgentIDPk(), pageable);

	        if (getOrdersDetails.isEmpty()) {
	            List<Map<String, Object>> emptyOrdersList = new ArrayList<>();
	            Map<String, Object> pagination = Utils.buildPaginationResponse(getOrdersDetails, baseUrl, pageSize, emptyOrdersList);
	            pagination.put("message", "You have no orders assigned."); 
	            response.put("success", true);
	            response.put("response", pagination);
	            return ResponseEntity.ok(response);
	        }
	        List<Map<String, Object>> ordersList = new ArrayList<>();
		    for (Order order : getOrdersDetails) {
		        Map<String, Object> orderDetails = new HashMap<>();
		        orderDetails.put("OrderNumber", order.getOrderId());
		        orderDetails.put("order_status", order.getOrderStatus());
		        orderDetails.put("Location", order.getAddress());
		        orderDetails.put("Latitude", order.getLatitude());
		        orderDetails.put("Longitude", order.getLongtitude());
		        orderDetails.put("workingTime",order.getPlans().getTimeDuration());
		        orderDetails.put("date", Utils.formatDateTime(order.getCreatedAt()));
		        orderDetails.put("Km", Utils.decimalFormat(order.getKm()) + " KM");
		        orderDetails.put("PlanName", order.getPlans().getPlansName());
		        orderDetails.put("PlanPrice", order.getPlans().getPlansRs());
		        orderDetails.put("status", true);
		        ordersList.add(orderDetails);
		    }
	        Map<String, Object> pagination = Utils.buildPaginationResponse(getOrdersDetails, baseUrl, pageSize, ordersList);
	        response.put("success", true);
	        response.put("response", pagination);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("success", false);
	        response.put("message", "Something went wrong.");
	    }
	    return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> viewDetailOrders(AgentMain agentMain, Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		String OrderNumber = (String) request.get("OrderNumber");
		try {
				Order getOrdersDetails = this.orderRepo.getOrderNumber(OrderNumber);
				
				Map<String, Object> orderDetails = new HashMap<String, Object>();
				orderDetails.put("OrderNumber", getOrdersDetails.getOrderId());
			    orderDetails.put("date", Utils.formatDateTime(getOrdersDetails.getCreatedAt()));
			    orderDetails.put("startTime", getOrdersDetails.getStartTime());
			    orderDetails.put("order_status", getOrdersDetails.getOrderStatus());


				Map<String, Object> CustomerDetails = new HashMap<String, Object>();
				CustomerDetails.put("customerName", getOrdersDetails.getCustomerMain().getFirstName() + " " + getOrdersDetails.getCustomerMain().getLastName());
				CustomerDetails.put("address", getOrdersDetails.getAddress());
				CustomerDetails.put("latitudeCus", getOrdersDetails.getLatitude());
				CustomerDetails.put("longtitudeCus", getOrdersDetails.getLongtitude());
				CustomerDetails.put("Km", Utils.decimalFormat(getOrdersDetails.getKm()) + " KM");
				CustomerDetails.put("mobileNUmber", getOrdersDetails.getCustomerMain().getMobileNumber());

				Map<String, Object> plansDetails = new HashMap<String, Object>();
				plansDetails.put("planName", getOrdersDetails.getPlans().getPlansName());
				plansDetails.put("planRs", getOrdersDetails.getPlans().getPlansRs());
				plansDetails.put("pots", getOrdersDetails.getPlans().getUptoPots());
				plansDetails.put("workingTime", getOrdersDetails.getPlans().getTimeDuration());
				plansDetails.put("frequency", "1 times");
				
				 // Fertilizer details
//		        List<Map<String, Object>> fertilizerList = new ArrayList<>();
//		        for (OrderFertilizers orderfer : getOrdersDetails.getOrderFertilizers()) {
//		            Map<String, Object> fertilizerMap = new HashMap<>();
//		            fertilizerMap.put("fertilizerName", orderfer.getFertilizerName());
//		            fertilizerMap.put("earningMalliFertilizer", orderfer.getAmount()- marginEachFertilizer);
//		            fertilizerMap.put("quantity", orderfer.getKg());
//		            fertilizerList.add(fertilizerMap);
//		        }

		   //     response.put("fertilizerDetails", fertilizerList);
				response.put("OderDetails", orderDetails);
				response.put("customerDetails", CustomerDetails);
				response.put("PlansDetails", plansDetails);
				response.put("status", true);
				
		}catch(Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Worng");
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> rejectedOrders(AgentMain agentMain, Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		String OrderNumber = (String) request.get("OrderNumber");
		String reason = (String) request.get("reason");
		String notify = "";
		int roundedTime = 0;
		double distanceKm = 0.0;
		try {
				Order getOrdersDetails = this.orderRepo.getOrderNumber(OrderNumber);
				
				
//				// if order has been assigned for some one 
//				
//				if(getOrdersDetails.getorder_status().equals("ACCEPTED")) {
//					synchronized (upComingOrdersStored) {
//					    boolean removed = upComingOrdersStored.removeIf(order -> 
//					        OrderNumber.equals(order.get("OrderNumber")) // Compare OrderNumber correctly
//					    );
//
//					    if (removed) {
//					        System.out.println("Order " + OrderNumber + " removed from upComingOrdersStored.");
//					    } else {
//					        System.out.println("Order " + OrderNumber + " not found in upComingOrdersStored.");
//					    }
//					}
//					response.put("message","This order assign to another Maali");
//					response.put("status", false);
//					return ResponseEntity.ok(response);
//				}
				
			//	AgentMain getAssignOrderAgent = getOrdersDetails.getAgentMain();
				
				getOrdersDetails.setOrderStatus("NOT_ACCPETED");
				getOrdersDetails.setAgentMain(null);

				Order saveOrders = this.orderRepo.save(getOrdersDetails);
				
				RejectedOrders rejectedOrders = new RejectedOrders();
				
				rejectedOrders.setOrderNumber(OrderNumber);
				rejectedOrders.setAgents(agentMain);
				rejectedOrders.setOrders(saveOrders);
				rejectedOrders.setReason(reason);
				rejectedOrders.setCreatedAt(LocalDateTime.now());
				rejectedOrders.setOrderStatus("CANCELED");
				this.rejectedOrdersRepo.save(rejectedOrders);
				
				
				// send notification to other same  orders 
				List<AgentMain> activeAgents = this.userdao.activeAgent();
				System.out.println("activeAgents size " + activeAgents.size());

				// List to keep track of agents within 5 km range
				List<AgentMain> agentsWithinRange = new ArrayList<>();

				// If there are active agents
				if (!activeAgents.isEmpty()) {
					for (AgentMain agent : activeAgents) {
						
						// Skip the assigned agent to prevent duplicate notifications
//					    if (agentMain != null && agent.getAgentIDPk() == agentMain.getAgentIDPk()) {
//				        System.out.println("Skipping notification for assigned agent: " + agent.getFirstName());
//					        continue;
//					    }
						if (agent.isActiveAgent()) {
							// Check if the agent is within the 5 km range
							boolean isWithinRange = this.locationService.isWithinRange(getOrdersDetails.getLatitude(),
									getOrdersDetails.getLongtitude(), agent.getLatitude(), agent.getLongitude());

							if (isWithinRange) {
								double getEstimatetime = this.locationService.estimateArrivalTime(getOrdersDetails.getLatitude(), getOrdersDetails.getLongtitude(),
										agent.getLatitude(), agent.getLongitude());
								roundedTime = (int) Math.ceil(getEstimatetime);

								System.out.println(" get estimate time --- " + roundedTime);

								distanceKm = this.locationService.calculateDistance(getOrdersDetails.getLatitude(),getOrdersDetails.getLongtitude(), agent.getLatitude(), agent.getLongitude());
								System.out.println(" distance km -- " + distanceKm);

								// Add agent to the list if they are within the range
								agentsWithinRange.add(agent);
								System.out.println("Agent within range: " + agent.getFirstName());

								String message = "Reminder Alert!\n\n" + "Hey " + agent.getFirstName() + " "
										+ agent.getLastName() + " it's time to nurture some greens! 🌱\n"
										+ "You have a scheduled service today. Check your app for details and get ready to bring life to another garden! 🌿✨";

								// notification send firebase with help
								notify = sendNotificationToAgent(agent, null, "Service Alert: New Order", message,"Order",OrderNumber);
								System.out.println(" notify----" + notify);
							}
						}
					}
				}
				// Prepare the response
				response.put("message", "Data received successfully");

				if (!agentsWithinRange.isEmpty()) {
					System.out.println("Total agents within 5 km range: " + agentsWithinRange.size());
					Map<String, Object> CustomerDetails = new HashMap<String, Object>();
					CustomerDetails.put("customerName", getOrdersDetails.getCustomerMain().getFirstName() + " " + getOrdersDetails.getCustomerMain().getLastName());
					CustomerDetails.put("location", getOrdersDetails.getAddress());
					CustomerDetails.put("latitudeCus", getOrdersDetails.getLatitude());
					CustomerDetails.put("arrivalTime", roundedTime);
					CustomerDetails.put("distanceKm", Utils.decimalFormat(distanceKm) + " KM");
					CustomerDetails.put("longtitudeCus", getOrdersDetails.getLongtitude());

					Map<String, Object> plansDetails = new HashMap<String, Object>();
					plansDetails.put("planName", getOrdersDetails.getPlans().getPlansName());
					plansDetails.put("planRs", getOrdersDetails.getPlans().getPlansRs());
				//	plansDetails.put("fertilizer", getOrdersDetails.getOrderFertilizers());
					
					response.put("notify", notify);
					response.put("notificationKey", "Order");
					response.put("OrderNumber", getOrdersDetails.getOrderId());
					response.put("order_status", getOrdersDetails.getOrderStatus());
					response.put("date", Utils.formatDateTime(getOrdersDetails.getCreatedAt()));
					response.put("Location", getOrdersDetails.getAddress());
					response.put("Latitude", getOrdersDetails.getLatitude());
					response.put("Longitude", getOrdersDetails.getLongtitude());
					response.put("Km", Utils.decimalFormat(distanceKm) + " KM");
					response.put("workingTime", getOrdersDetails.getPlans().getTimeDuration());
					response.put("PlanName", getOrdersDetails.getPlans().getPlansName());
					response.put("PlanPrice", getOrdersDetails.getPlans().getPlansRs());
					
//					response.put("notify", notify);
//					response.put("notificationKey", "OrderNotification");
//					response.put("OrderNumber", getOrdersDetails.getOrderId());
//					response.put("order_status", getOrdersDetails.getorder_status());
//					response.put("CustomerDetails", CustomerDetails);
//					response.put("PlansDetails", plansDetails);
					response.put("status", true);
					
					
					 // Store upcoming orders without overwriting previous ones
		            synchronized (upComingOrdersStored) {
		                upComingOrdersStored.add(response);
		            }
				} else {
					System.out.println("No agents found within 5 km range.");
					response.put("status", false);
					response.put("agentsWithinRange", "No agents found within range");
				}
	
		}catch(Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Worng");
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getRejectedOrderList(AgentMain agentRecords, int pageNumber, int pageSize, String baseUrl) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        int pageIndex = Math.max(pageNumber - 1, 0);
	        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending());
	        Page<RejectedOrders> getOrdersDetails = this.rejectedOrdersRepo.getRejectedOrderListPaganation(agentRecords.getAgentIDPk(), pageable);

	        if (getOrdersDetails.isEmpty()) {
	            List<Map<String, Object>> emptyOrdersList = new ArrayList<>();
	            Map<String, Object> pagination = Utils.buildPaginationResponse(getOrdersDetails, baseUrl, pageSize, emptyOrdersList);
	            pagination.put("message", "You have no orders rejecteds."); 
	            response.put("success", true);
	            response.put("response", pagination);
	            return ResponseEntity.ok(response);
	        }
	        List<Map<String, Object>> ordersList = new ArrayList<>();
		    for (RejectedOrders rejectedOrder : getOrdersDetails) {
		        Map<String, Object> orderDetails = new HashMap<>();
		        orderDetails.put("OrderNumber", rejectedOrder.getOrderNumber());
		        orderDetails.put("order_status", rejectedOrder.getOrderStatus());
		        orderDetails.put("date", Utils.formatDateTime(rejectedOrder.getCreatedAt()));
		        orderDetails.put("Location", rejectedOrder.getOrders().getAddress());
		        orderDetails.put("Latitude", rejectedOrder.getOrders().getLatitude());
		        orderDetails.put("Longitude", rejectedOrder.getOrders().getLongtitude());
		        orderDetails.put("Km", Utils.decimalFormat(rejectedOrder.getOrders().getKm()) + " KM");
		        orderDetails.put("PlanName", rejectedOrder.getOrders().getPlans().getPlansName());
		        orderDetails.put("PlanPrice", rejectedOrder.getOrders().getPlans().getPlansRs());
		        orderDetails.put("workingTime", rejectedOrder.getOrders().getPlans().getTimeDuration());
		        ordersList.add(orderDetails);
		    }
	        Map<String, Object> pagination = Utils.buildPaginationResponse(getOrdersDetails, baseUrl, pageSize, ordersList);
	        response.put("success", true);
	        response.put("response", pagination);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("success", false);
	        response.put("message", "Something went wrong.");
	    }
	    return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> wayToLocation(AgentMain agentMain, Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		String orderNumber = (String) request.get("OrderNumber");
		double latitude = (double) request.get("latitude");
		double longitude = (double) request.get("longitude");
		try {
			Order getOrdersDetails = this.orderRepo.getOrderNumber(orderNumber);
			if (getOrdersDetails == null) {
				response.put("status", false);
				response.put("message", "Order details not found");
				return ResponseEntity.ok(response);
			}
			double getEstimatetime = this.locationService.estimateArrivalTime(getOrdersDetails.getLatitude(),getOrdersDetails.getLongtitude(), latitude, longitude);
			
			int roundedTime = (int) Math.ceil(getEstimatetime);
			getOrdersDetails.setOrderStatus("START");
			Order save = this.orderRepo.save(getOrdersDetails);
			
			String message = "Reminder Alert!\n\n"
					+"Hello " + getOrdersDetails.getCustomerMain().getFirstName() + ",\n\n"
			        + "Your assigned malli is currently en route and is expected to arrive in approximately "
			        + roundedTime + " minutes.\n\n"
			        + "Please ensure access to the location is available. Thank you for choosing our service!";

				String notify = sendNotificationToAgent(null, getOrdersDetails.getCustomerMain(), "Way Location",message, "WayNotiy",orderNumber);
				
				response.put("order_Number", save.getOrderId());
				response.put("order_status", save.getOrderStatus());
				response.put("notify", notify);
				response.put("message", "Gardener has way to your location.");
				response.put("status", true);
			
		} catch(Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Worng");
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> reachedLocation(AgentMain agentMain, Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		String orderNumber = (String) request.get("OrderNumber");
		double latitude = (double) request.get("latitude");
		double longitude = (double) request.get("longitude");
		try {
			Order getOrdersDetails = this.orderRepo.getOrderNumber(orderNumber);
			if (getOrdersDetails == null) {
				response.put("status", false);
				response.put("message", "Order details not found");
				return ResponseEntity.ok(response);
			}
			double distanceKm = this.locationService.calculateDistance(getOrdersDetails.getLatitude(),getOrdersDetails.getLongtitude(), latitude, longitude);
			System.out.println("Distance km -- " + distanceKm);
			
			if (distanceKm <= reachedLocation) {  // 200 meter  reached.location.meter
				int distanceMeters = (int) Math.round(distanceKm * 1000);
			
				// save status reached location 
				getOrdersDetails.setOrderStatus("REACHED");
				Order save = this.orderRepo.save(getOrdersDetails);
				
				String notifyMsg = "Your assigned maali has arrived at your location "
		                 + "(approximately " + distanceMeters + " meters away). "
		                 + "Now you can start the work. Thank you for choosing our service.";
				
				String notify = sendNotificationToAgent(null, getOrdersDetails.getCustomerMain(), "Reached Location",
						notifyMsg, "ReachedNotify",orderNumber);
				
				response.put("notify", notify);
				response.put("order_Number", save.getOrderId());
				response.put("order_status", save.getOrderStatus());
				response.put("message", "Gardener has been reached the your location.");
				response.put("status", true);
			} else {
				response.put("message", "Move closer to the customer's location.");
				response.put("status", true);
			}
		} catch(Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Worng");
		}
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> OtpCodeShared(AgentMain agentMain, Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();

		String OrderNumber = (String) request.get("OrderNumber");
		String otpCode = (String) request.get("otpCode");
		try {
			if (otpCode == null || otpCode.trim().isEmpty()) {
				response.put("message", "Enter the Customer Code");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}
			Order getOrdersDetails = this.orderRepo.getOrderNumber(OrderNumber);
			if (getOrdersDetails == null) {
				response.put("message", "Order not found.");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}
			if (otpCode.equals(getOrdersDetails.getShareCode())) {
				// status changed
				LocalTime currentTime = LocalTime.now();

				getOrdersDetails.setOrderStatus("START_TIME");
				getOrdersDetails.setStartTime(currentTime);
				Order saveOrders = this.orderRepo.save(getOrdersDetails);
				response.put("order_Number", saveOrders.getOrderId());
				response.put("order_status", saveOrders.getOrderStatus());
				response.put("currentTime", saveOrders.getStartTime());
				response.put("message", "Otp Code has been matched , Now you can start the work");
				response.put("status", true);
			} else {
				response.put("message", "Please enter the correct Otp Code,Please try again it");
				response.put("status", false);
			}
		}catch(Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Worng");
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> endTimeWork(AgentMain agentMain, Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		String OrderNumber = (String) request.get("OrderNumber");
		try {
			
			Order getOrdersDetails = this.orderRepo.getOrderNumber(OrderNumber);
			if (getOrdersDetails == null) {
				response.put("message", "Order not found.");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}
				// status changed
				LocalTime currentTime = LocalTime.now();

				getOrdersDetails.setOrderStatus("END_TIME");
				getOrdersDetails.setEndTime(currentTime);
				Order saveOrders = this.orderRepo.save(getOrdersDetails);
				response.put("order_Number", saveOrders.getOrderId());
				response.put("order_status", saveOrders.getOrderStatus());
				response.put("message", "Now you can Upload the Photo what you work!");
				response.put("status", true);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something Went Worng");
		}
		return ResponseEntity.ok(response);
	}
	public ResponseEntity<Map<String, Object>> workPhotoUpload(AgentMain existingAgent, String OrderNumber,
			MultipartFile workCompletdPhoto1, MultipartFile workCompletdPhoto2) {
		Map<String, Object> response = new HashMap<>();
		if (workCompletdPhoto1.isEmpty()) {
			response.put("message", "Please upload the Picture what you work");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		if (workCompletdPhoto2.isEmpty()) {
			response.put("message", "Please upload the Picture what you work");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		if (OrderNumber == null || OrderNumber.trim().isEmpty()) {
			response.put("message", "Please Provide Order Number");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		try {

			System.out.println(" OrderNumber  --  " + OrderNumber);
			// upload image in server ORderID
			Order getOrdersDetails = this.orderRepo.getOrderNumber(OrderNumber);
			WorkCompletedPhoto workCompletedPhoto = new WorkCompletedPhoto();

			String subDirectoryFloderName = OrderNumber;
			String workCompletdPhoto1fileName = OrderNumber + "_" + System.currentTimeMillis() + "_"+ workCompletdPhoto1.getOriginalFilename();
			String workCompletdPhoto1ImagePath = Utils.saveImgFile(workCompletdPhoto1, uploadDir,subDirectoryFloderName, workCompletdPhoto1fileName);

			workCompletedPhoto.setWorkCompletdPhoto1(workCompletdPhoto1fileName);
			workCompletedPhoto.setWorkCompletdPhoto1Path(workCompletdPhoto1ImagePath);
			workCompletedPhoto.setWorkCompletdPhoto1_type(workCompletdPhoto1.getContentType());

			String workCompletdPhoto2Name = OrderNumber + "_" + System.currentTimeMillis() + "_"+ workCompletdPhoto2.getOriginalFilename();
			String workCompletdPhoto2ImagePath = Utils.saveImgFile(workCompletdPhoto2, uploadDir,subDirectoryFloderName, workCompletdPhoto2Name);

			workCompletedPhoto.setWorkCompletdPhoto2(workCompletdPhoto2Name);
			workCompletedPhoto.setWorkCompletdPhoto2Path(workCompletdPhoto2ImagePath);
			workCompletedPhoto.setWorkCompletdPhoto2_type(workCompletdPhoto2.getContentType());

			workCompletedPhoto.setOrderNumber(OrderNumber);
			workCompletedPhoto.setOrders(getOrdersDetails);
			this.workCompletedPhotoRepo.save(workCompletedPhoto);

			getOrdersDetails.setOrderStatus("COMPLETED");
			Order saveOrders = this.orderRepo.save(getOrdersDetails);
		//	double totalMalliFertilizer=0;
		//	double totalCompanyFertilizer=0;
//			// fertilizer earning 
//			for(OrderFertilizers orderFertilizers : getOrdersDetails.getOrderFertilizers()) {
//				double getMalliCost = orderFertilizers.getAmount()-marginEachFertilizer;
//				double getCompanyCost = orderFertilizers.getAmount()-getMalliCost;
//				orderFertilizers.setEarningMalliFertilizer(getMalliCost);
//				orderFertilizers.setCompanyEarningFertilizer(getCompanyCost);
//				orderFertilizers.setMargineachFertilizer(marginEachFertilizer);
//				OrderFertilizers save = this.orderFertilizersRepo.save(orderFertilizers);	
//				
//				totalMalliFertilizer += save.getEarningMalliFertilizer();
//				totalCompanyFertilizer += save.getCompanyEarningFertilizer();
//			}
			
			// agent amount rs  get account
			OrderEarning getOrderEarning = this.orderEarningRepo.getbyOrderNumber(OrderNumber);
			getOrderEarning.setAgentEarningRs(getOrderEarning.getPlansRs()-getOrderEarning.getCompanyEarningRs());
			getOrderEarning.setAgentMain(existingAgent);
			getOrderEarning.setEarningStatus("COMPLETED");
			//getOrderEarning.setCompanyEarningFertilizer(totalCompanyFertilizer);
		//	getOrderEarning.setEarningMalliFertilizer(totalMalliFertilizer);
			getOrderEarning.setCreatedAt(LocalDateTime.now());
			
			this.orderEarningRepo.save(getOrderEarning);
			
			response.put("order_status", saveOrders.getOrderStatus());
			response.put("message", "Order has been Compelted");
			response.put("status", true);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status", false);
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getCompletedOrder(AgentMain agentRecords, int pageNumber, int pageSize, String baseUrl) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        int pageIndex = Math.max(pageNumber - 1, 0);
	        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending());
	        Page<Order> getOrdersDetails = orderRepo.getOrderCompletedListPaganation(agentRecords.getAgentIDPk(), pageable);

	        if (getOrdersDetails.isEmpty()) {
	            List<Map<String, Object>> emptyOrdersList = new ArrayList<>();
	            Map<String, Object> pagination = Utils.buildPaginationResponse(getOrdersDetails, baseUrl, pageSize, emptyOrdersList);
	            pagination.put("message", "You have No Completed Order."); 
	            response.put("success", true);
	            response.put("response", pagination);
	            return ResponseEntity.ok(response);
	        }
	        List<Map<String, Object>> ordersList = new ArrayList<>();
		    for (Order order : getOrdersDetails) {
		        Map<String, Object> orderDetails = new HashMap<>();
		        orderDetails.put("OrderNumber", order.getOrderId());
		        orderDetails.put("order_status", order.getOrderStatus());
		        orderDetails.put("Location", order.getAddress());
		        orderDetails.put("Latitude", order.getLatitude());
		        orderDetails.put("Longitude", order.getLongtitude());
		        orderDetails.put("Km", Utils.decimalFormat(order.getKm()) + " KM");
		        orderDetails.put("date", Utils.formatDateTime(order.getCreatedAt()));
		        orderDetails.put("workingTime", order.getPlans().getTimeDuration());
		        orderDetails.put("PlanName", order.getPlans().getPlansName());
		        orderDetails.put("PlanPrice", order.getPlans().getPlansRs());
		        ordersList.add(orderDetails);
		    }
	        Map<String, Object> pagination = Utils.buildPaginationResponse(getOrdersDetails, baseUrl, pageSize, ordersList);
	        response.put("success", true);
	        response.put("response", pagination);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("success", false);
	        response.put("message", "Something went wrong.");
	    }
	    return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> ourServiceRating(CustomerMain exitsCustomer,
			Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		String OrderNumber = (String) request.get("OrderNumber");
		String comment = (String) request.get("comment");
		double rating = (double) request.get("rating");

		if (OrderNumber == null || OrderNumber.trim().isEmpty()) {
			response.put("message", "Please Provide Order Number");
			response.put("status", false);
			return ResponseEntity.ok(response);
		}
		try {
			Order getOrdersDetails = this.orderRepo.getOrderNumber(OrderNumber);
			GardnerRating gardnerRating = new GardnerRating();
			gardnerRating.setRating(rating);
			gardnerRating.setComment(comment);
			gardnerRating.setOrderNumber(OrderNumber);
			gardnerRating.setAgents(getOrdersDetails.getAgentMain());
			gardnerRating.setOrders(getOrdersDetails);
			this.gardnerRatingRepo.save(gardnerRating);
			response.put("message", "Thanks you giving rating");
			response.put("status", true);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Something Went Wrong");
			response.put("status", false);
		}
		return ResponseEntity.ok(response);
	}

	public String generateOrderId() {
		String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String oderFormat = "GAR" + currentDate + "000";
		// Fetch the latest order ID for today
		Integer lastSequenceNumber = orderRepo.getMaxSequenceOrderNumber();
		System.out.println(" Order  Number last digit -- " + lastSequenceNumber);
		int sequenceNumber = 1;
		String orderGenerated = "";
		if (lastSequenceNumber != null) {
			sequenceNumber = lastSequenceNumber + 1;
			orderGenerated = oderFormat + sequenceNumber;
		} else {
			orderGenerated = oderFormat + sequenceNumber;
		}
		return orderGenerated;
	}

	private String sendNotificationToAgent(AgentMain agent, CustomerMain customerMain, String title, String message,String notificationType,String OrderNumber) {
		String response = "";
		String fcmToken = null;

		try {
			if (agent != null && agent.getFcmTokenAgent() != null && !agent.getFcmTokenAgent().trim().isEmpty()) {
				fcmToken = agent.getFcmTokenAgent();
			}
			else if (customerMain != null && customerMain.getFirebasetokenCus() != null
					&& !customerMain.getFirebasetokenCus().trim().isEmpty()) {
				fcmToken = customerMain.getFirebasetokenCus();
			}
			else {
				return "FCM Token is missing for both Agent and Customer";
			}
			Message firebaseMessage = Message.builder().setToken(fcmToken)
					.setNotification(Notification.builder().setTitle(title).setBody(message).build())
					.putData("type", notificationType) // Add custom data
					.putData("OrderID", OrderNumber) // Add custom data
					.build();
			// Send the Notification
			response = FirebaseMessaging.getInstance().send(firebaseMessage);
			System.out.println("Successfully sent message: " + response);
		} catch (FirebaseMessagingException e) {
			System.err.println("Error sending notification: " + e.getMessage());
			e.printStackTrace();
			return "Notification sending failed";
		}
		return response;
	}
	
	public void calculateOrderEarning(PaymentRequest request,Order orders) {
		
		OrderEarning orderEarning = new OrderEarning();
		
		double serviceCharges = request.getOrderSummaryRequest().getServicesCharges();
		
		double grandAmount = request.getOrderSummaryRequest().getGrandTotal();
		
		double platformfees = request.getOrderSummaryRequest().getPlatformfees();
		
		double gstamount = request.getOrderSummaryRequest().getGstAmount();
		
		double coupanAmount = request.getOrderSummaryRequest().getCoupanAmount();
		
		double companyEarning = serviceCharges * marginServiceChargesPerOrderPercent/100;
		
		orderEarning.setCompanyEarningRs(companyEarning);
		orderEarning.setCouponAppliedRs(coupanAmount);
		orderEarning.setGrandTotalAmount(grandAmount);
		orderEarning.setGstAmountRs(gstamount);
		orderEarning.setGstPercentFees(gstRate);
		orderEarning.setMarginPercentPerOrder(marginServiceChargesPerOrderPercent);
		orderEarning.setOrders(orders);
		orderEarning.setOrderNumber(orders.getOrderId());
		orderEarning.setPlansRs(serviceCharges);
		orderEarning.setPlatformFess(platformfees);
		
		this.orderEarningRepo.save(orderEarning);
	
		
	}
	
	// coupan applied   customer
	public void appliedOffers(PaymentRequest request,Order orders,Offers offer,CustomerMain exitsCustomer) {
		OffersApplied existingAppliedOffer = offersAppliedRepo.getAppliedOffers(offer.getPrimarykey(),exitsCustomer.getPrimarykey());
			if (existingAppliedOffer == null) {
				OffersApplied appliedOffer = new OffersApplied();
				appliedOffer.setCustomerMain(exitsCustomer);
				appliedOffer.setOfferStatus("APPLY");
				appliedOffer.setAppTypeId("Customer");
				appliedOffer.setOffers(offer);
				offersAppliedRepo.save(appliedOffer);
			}		
	}

}
