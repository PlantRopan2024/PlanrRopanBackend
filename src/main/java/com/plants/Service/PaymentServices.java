package com.plants.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.plants.Dao.CustomerDao;
import com.plants.Dao.OrderFertilizersRepo;
import com.plants.Dao.OrderRepo;
import com.plants.Dao.PaymentRepo;
import com.plants.Dao.userDao;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;
import com.plants.entities.CustomerMain;
import com.plants.entities.FertilizerRequest;
import com.plants.entities.Order;
import com.plants.entities.OrderFertilizers;
import com.plants.entities.OrderSummaryRequest;
import com.plants.entities.Payment;
import com.plants.entities.PaymentRequest;
import com.plants.entities.Plans;

@Service
public class PaymentServices {

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

	public ResponseEntity<Map<String, Object>> paymentInitiate(CustomerMain exitsCustomer, PaymentRequest request) {
		Map<String, Object> response = new HashMap<>();
		// String merchantVpa = "merchant@upi"; // Replace with actual UPI ID
		// String merchantName = "Your Business";

		// we get plans form Database

		Plans selectedPlan = this.customerDao.getPlansId(request.getPlanId());
		if (selectedPlan == null) {
			response.put("status", false);
			response.put("message", "Plans are not avaialbe");
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
		orders.setOrderId(generateOrderId());
		orders.setTotalAmount(orderSummaryRequest.getGrandTotal());
		orders.setCoupanAmount(orderSummaryRequest.getCoupanAmount());
		orders.setCouponApplied(orderSummaryRequest.isCouponApplied());
		orders.setGstAmount(orderSummaryRequest.getGstAmount());
		orders.setOfferId(request.getOfferId()); // save offerid customer choose
		orders.setPlans(selectedPlan); // save plans book customer choose
		orders.setPlatformfees(orderSummaryRequest.getPlatformfees());
		orders.setServicesCharges(orders.getServicesCharges());
		orders.setCreatedAt(LocalDateTime.now());
		orders.setAddress(request.getAddress());
		orders.setLatitude(request.getLatitude());
		orders.setLongtitude(request.getLongtitude());
		orders.setOrderStatus("NOT_ASSIGNED");

		Order saveOrders = this.orderRepo.save(orders);

		// fertilizers save
		for (FertilizerRequest fertilizerRequest : request.getOrderSummaryRequest().getFertilizers()) {
			OrderFertilizers ordersFertilizers = new OrderFertilizers();
			ordersFertilizers.setFertilizerName(fertilizerRequest.getName());
			ordersFertilizers.setAmount(fertilizerRequest.getPrice());
			ordersFertilizers.setKg(fertilizerRequest.getQuantity());
			ordersFertilizers.setOrders(saveOrders);
			this.orderFertilizersRepo.save(ordersFertilizers);
		}

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

		response.put("amount", saveOrders.getTotalAmount());
		response.put("OrderId", saveOrders.getOrderId());
		response.put("date", saveOrders.getCreatedAt());
		response.put("paymentMethod", savePayment.getPaymentMethod());
		response.put("customerName", exitsCustomer.getFirstName()+ " " + exitsCustomer.getLastName());
		response.put("paymentStatus", savePayment.getPaymentStatus());
		response.put("status", true);

		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> OrderAssigned(CustomerMain exitsCustomer, Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();

		String OrderNumber = (String) request.get("OrderNumber");
		String notify = "";
		int roundedTime = 0;
		double distanceKm =0.0;
		Order getOrdersDetails = this.orderRepo.getOrderNumber(OrderNumber);
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

						double getEstimatetime = this.locationService.estimateArrivalTime(
								getOrdersDetails.getLatitude(), getOrdersDetails.getLongtitude(), agent.getLatitude(),
								agent.getLongitude());
						roundedTime = (int) Math.ceil(getEstimatetime);

						System.out.println(" get estimate time --- " + roundedTime);
						
						distanceKm = this.locationService.calculateDistance(getOrdersDetails.getLatitude(),
								getOrdersDetails.getLongtitude(), agent.getLatitude(), agent.getLongitude());
						System.out.println(" distance km -- "+ distanceKm);
						
						// Add agent to the list if they are within the range
						agentsWithinRange.add(agent);
						System.out.println("Agent within range: " + agent.getFirstName());

						// notification send firebase with help
						notify = sendNotificationToAgent(agent,null ,"You have a new order nearby",
								"Please check the app for details.","OrderNotification");
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
			CustomerDetails.put("customerName", exitsCustomer.getFirstName()+ " " + exitsCustomer.getLastName());
			CustomerDetails.put("location", getOrdersDetails.getAddress());
			CustomerDetails.put("latitudeCus", getOrdersDetails.getLatitude());
			CustomerDetails.put("arrivalTime", roundedTime);
			CustomerDetails.put("distanceKm", Utils.decimalFormat(distanceKm)+" KM");
			CustomerDetails.put("longtitudeCus", getOrdersDetails.getLongtitude());

			Map<String, Object> plansDetails = new HashMap<String, Object>();
			plansDetails.put("planName", getOrdersDetails.getPlans().getPlansName());
			plansDetails.put("planRs", getOrdersDetails.getPlans().getPlansRs());
			plansDetails.put("fertilizer", getOrdersDetails.getOrderFertilizers());

			response.put("notify", notify);
			response.put("notificationKey", "OrderNotification");
			response.put("OrderNumber", getOrdersDetails.getOrderId());
			response.put("CustomerDetails", CustomerDetails);
			response.put("PlansDetails", plansDetails);
			response.put("status", true);

			// response.put("agentsWithinRange", agentsWithinRange); // Send the list of
			// agents within range
		} else {
			System.out.println("No agents found within 5 km range.");
			response.put("status", false);
			response.put("agentsWithinRange", "No agents found within range");
		}
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> accpetOrRejctOrder(AgentMain agentMain, Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		
		String OrderNumber = (String) request.get("OrderNumber");
		String bookingStatus = (String) request.get("BookingStatus");
		
		if(bookingStatus.equals("ACCEPT")) {
			Order getOrdersDetails = this.orderRepo.getOrderNumber(OrderNumber);
			getOrdersDetails.setOrderStatus("ASSIGNED");
			getOrdersDetails.setAgentMain(agentMain);
			
			Order saveOrders = this.orderRepo.save(getOrdersDetails);
			CustomerMain customerMain = this.customerDao.findbyPrimaryKey(saveOrders.getCustomerMain().getPrimarykey());
			// send notification to customer your order has been accpeted
			String notify = sendNotificationToAgent(null,customerMain ,"You Order have been assigned",
					"Please check the app for details.","OrderRecived");
			
			Map<String, Object> orderDetails = new HashMap<String, Object>();
			orderDetails.put("OrderNumber", saveOrders.getOrderId());
			
			Map<String, Object> CustomerDetails = new HashMap<String, Object>();
			CustomerDetails.put("customerName", customerMain.getFirstName()+ " " + customerMain.getLastName());
			CustomerDetails.put("address", getOrdersDetails.getAddress());
			CustomerDetails.put("latitudeCus", getOrdersDetails.getLatitude());
			CustomerDetails.put("longtitudeCus", getOrdersDetails.getLongtitude());
			CustomerDetails.put("mobileNUmber", customerMain.getMobileNumber());
			
			Map<String, Object> plansDetails = new HashMap<String, Object>();
			plansDetails.put("planName", getOrdersDetails.getPlans().getPlansName());
			plansDetails.put("planRs", getOrdersDetails.getPlans().getPlansRs());
			plansDetails.put("pots", getOrdersDetails.getPlans().getUptoPots());
			plansDetails.put("plansDuration", getOrdersDetails.getPlans().getTimeDuration());
			plansDetails.put("frequency","1 times");
			plansDetails.put("fertilizer", getOrdersDetails.getOrderFertilizers());
			
			response.put("OderDetails", orderDetails);
			response.put("customerDetails", CustomerDetails);
			response.put("PlansDetails", plansDetails);
			response.put("notify", notify);
			response.put("status", true);
			response.put("message", "Order has been Assigned ");
		}
		
		if(bookingStatus.equals("REJECT")) {
			
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

	private String sendNotificationToAgent(AgentMain agent,CustomerMain customerMain, String title, String message ,String notificationType) {
		String response = "";
		try {
			String fcmToken = null;
			if(Objects.isNull(agent)) {
				 fcmToken = agent.getFcmTokenAgent();
				if (fcmToken == null || fcmToken.trim().isEmpty()) {
					return "Agent FCM Token is missing";
				}
			}
			if(Objects.isNull(customerMain)) {
				 fcmToken = customerMain.getFirebasetokenCus();
				if (fcmToken == null || fcmToken.trim().isEmpty()) {
					return "Customer FCM Token is missing";
				}
			}
			
			Message firebaseMessage = Message.builder().setToken(fcmToken)
					.setNotification(Notification.builder().setTitle(title).setBody(message).build())
				//	.putData("agentId", String.valueOf(agent.getAgentIDPk()))
				//	.putData("action", "ACCEPT_REJECT") // Action
					.putData("type", notificationType)  // type
					.build();

			response = FirebaseMessaging.getInstance().send(firebaseMessage);
			System.out.println("Successfully sent message: " + response);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
	        return "Notification sending failed";

		}
		return response;
	}

}
