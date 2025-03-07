package com.plants.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.plants.Dao.AgentMainRepo;
import com.plants.Dao.AgentReferralRepository;
import com.plants.Dao.NotificationRepo;
import com.plants.Dao.WalletHistoryRepo;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;
import com.plants.entities.AgentReferral;
import com.plants.entities.WalletHistory;

@Service
public class AgentReferralService {
	
	@Value("${pagination.page}")
	private int page;
	
	@Value("${pagination.size}")
	private int size;
	
	@Autowired
    private AgentReferralRepository agentReferralRepository;
	
	@Autowired
	private AgentMainRepo agentMainRepo;
	
	@Autowired
	private WalletHistoryRepo walletHistoryRepo;
	
	@Autowired
	private NotificationRepo notificationRepo;

	public ResponseEntity<Map<String, Object>> assignReferralCode(AgentMain existingAgent, Map<String, String> request) {
	    Map<String, Object> response = new HashMap<>();
	    String referralCode = request.get("referralCode");

	    if (referralCode.isEmpty() || referralCode == null) {
	        response.put("status", true);
	        response.put("message", "Referral Code is Empty!");
	        return ResponseEntity.ok(response);
	    }
	    try {
	        if (agentReferralRepository.getUseReferralCode(referralCode, existingAgent.getAgentIDPk()) != null) {
	            response.put("status", true);
	            response.put("message", "You have already used this code.");
	            return ResponseEntity.ok(response);
	        }

	        AgentMain agentMainReferral = this.agentMainRepo.getReferralCodeAgent(referralCode.trim());
	        if (agentMainReferral == null) {
	            response.put("status", true);
	            response.put("message", "Referral Code is Invalid!");
	            return ResponseEntity.ok(response);
	        }

	        AgentReferral agentReferral = new AgentReferral();
	        agentReferral.setReferralCode(referralCode);
	        agentReferral.setReferrerAgentId(agentMainReferral.getAgentIDPk());
	        agentReferral.setReferredAgentId(existingAgent.getAgentIDPk());
	        agentReferral.setCreatedAt(LocalDateTime.now());
	        agentReferral = agentReferralRepository.save(agentReferral);

	        // Save Wallet Transaction
			
//			  WalletHistory walletHistory = new WalletHistory();
//			  walletHistory.setAmount(50.00);
//			  walletHistory.setDescription("Refer Cashback Received");
//			  walletHistory.setTransactionType("REWARD");
//			  walletHistory.setAgentMain(agentMainReferral);
//			  walletHistory.setCreatedAt(LocalDateTime.now());
//			  walletHistory.setAgentReferral(agentReferral);
//			  walletHistoryRepo.save(walletHistory);
			 

	        // Save Notification
	        String message = "Dear "+existingAgent.getFirstName()+" "+existingAgent.getLastName()+",\n\n" +
	                "Your referral code has been successfully applied! ₹50 will be credited to your account after the first service is completed with Plant Ropan.\n\n" +
	                "Thank you for spreading the greenery!\n\n" +
	                "Happy Gardening,\n" +
	                "Team Plant Ropan";
	        
	        try {
	            sendNotificationToAgent(agentMainReferral, "Referral Reward Earned 🎉", message);
	        } catch (Exception e) {
	            e.printStackTrace();
	            response.put("notification_status", "failed");
	        }

	        com.plants.entities.Notification notification = new com.plants.entities.Notification();
	        notification.setMessage(message);
	        notification.setTitle("Referral Reward Earned 🎉");
	        notification.setTypeIId("Agent");
	        notification.setAgentMain(agentMainReferral);
	        notificationRepo.save(notification);

	        response.put("status", true);
	        response.put("message", "Your referral has been successfully submitted!");
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", false);
	        response.put("message", "Something Went Wrong!");
	    }
	    
	    return ResponseEntity.ok(response);
	}

	
	public ResponseEntity<Map<String, Object>> walletHistoryAgent(AgentMain existingAgent) {
	    Map<String, Object> response = new HashMap<>();
	    double totalAmount = 0.0;
	    List<Map<String, Object>> historyList = new ArrayList<>();

	    try {
	        List<WalletHistory> referralHistory = existingAgent.getReferralHistory();
	        
	        if (referralHistory == null || referralHistory.isEmpty()) {
	            response.put("status", true);
	            response.put("message", "No Wallet found");
	            response.put("totalWalletBalance", totalAmount);
	            response.put("referralHistory", historyList);
	            response.put("currentPage", 0);
	            response.put("totalPages", 0);
	            response.put("totalElements", 0);
	            return ResponseEntity.ok(response);
	        }

	        for (WalletHistory walletHistory : referralHistory) {
	            Map<String, Object> historyEntry = new HashMap<>();
	            historyEntry.put("id", walletHistory.getId());
	            historyEntry.put("description", walletHistory.getDescription());
	            historyEntry.put("amount", walletHistory.getAmount());
	            historyEntry.put("transactionType", walletHistory.getTransactionType());
	            historyEntry.put("createdAt", walletHistory.getCreatedAt());
	            
	            totalAmount += walletHistory.getAmount();
	            historyList.add(historyEntry);
	        }

	        // Implement pagination
	        int totalElements = referralHistory.size();
	        Map<String, Object> pagination = Utils.paganationInApi(size, page, totalElements);
	        int startIndex = (int) pagination.get("startIndex");
	        int endIndex = (int) pagination.get("endIndex");
	        int totalPages = (int) pagination.get("totalPages");
	        int currentPage = (int) pagination.get("currentPage");

	        List<Map<String, Object>> paginatedList = historyList.subList(startIndex, Math.min(endIndex, totalElements));

	        response.put("status", true);
	        response.put("message", "Wallet found");
	        response.put("totalWalletBalance", totalAmount);
	        response.put("referralHistory", paginatedList);
	        response.put("currentPage", currentPage);
	        response.put("totalPages", totalPages);
	        response.put("totalElements", totalElements);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", false);
	        response.put("message", "Something Went Wrong!");
	    }
	    return ResponseEntity.ok(response);
	}

	
	public ResponseEntity<Map<String, Object>> getNotificationHistoryAgent(AgentMain existingAgent) {
	    Map<String, Object> response = new HashMap<>();
	    List<Map<String, Object>> notifyList = new ArrayList<>();
	    try {
	        List<com.plants.entities.Notification> allNotifications = existingAgent.getNotification()
	                .stream()
	                .filter(notification -> "Agent".equals(notification.getTypeIId()))
	                .collect(Collectors.toList());
	        int totalElements = allNotifications.size();
	        Map<String, Object> pagination = Utils.paganationInApi(size, page, totalElements);
	        int startIndex = (int) pagination.get("startIndex");
	        int endIndex = (int) pagination.get("endIndex");
	        int totalPages = (int) pagination.get("totalPages");
	        int currentPage = (int) pagination.get("currentPage");
	        
	        if (startIndex >= totalElements) {
	            response.put("status", true);
	            response.put("message", "No Notifications found");
	            response.put("notifyHistory", Collections.emptyList());
	            response.put("currentPage", currentPage);
	            response.put("totalPages", totalPages);
	            response.put("totalElements", totalElements);
	            return ResponseEntity.ok(response);
	        }
	        List<com.plants.entities.Notification> paginatedList = allNotifications.subList(startIndex, endIndex);
	        for (com.plants.entities.Notification notification : paginatedList) {
	            Map<String, Object> notifyHist = new HashMap<>();
	            notifyHist.put("id", notification.getId());
	            notifyHist.put("message", notification.getMessage());
	            notifyHist.put("title", notification.getTitle());
	            notifyHist.put("date", notification.getCreatedAt());
	            notifyHist.put("isRead", notification.isRead());
	            notifyList.add(notifyHist);
	        }
	        response.put("notifyHistory", notifyList);
	        response.put("message", "Notifications found");
	        response.put("status", true);
	        response.put("currentPage", currentPage);
	        response.put("totalPages", totalPages);
	        response.put("totalElements", totalElements);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", false);
	        response.put("message", "Something Went Wrong!");
	    }
	    return ResponseEntity.ok(response);
	}

	public String sendNotificationToAgent(AgentMain agent, String title, String message) {
	    String response = "";
	    try {
	    	if (agent.getFcmTokenAgent() == null || agent.getFcmTokenAgent().isEmpty()) {
	            return "FCM Token is missing";
	        }
	        // Create the message to send
	        Message firebaseMessage = Message.builder()
	            .setToken(agent.getFcmTokenAgent())  // Use the agent's FCM token here
	            .setNotification(Notification.builder()
	                .setTitle(title)
	                .setBody(message)
	                .build())
	            .putData("agentId", String.valueOf(agent.getAgentIDPk())) // Add additional data if needed
	            .putData("action", "ACCEPT_REJECT") // Add action to distinguish the type of notification
	            .build();

	        // Send the message via Firebase
	        response = FirebaseMessaging.getInstance().send(firebaseMessage);
	        System.out.println("Successfully sent message: " + response);
	    } catch (FirebaseMessagingException e) {
	        e.printStackTrace();
	    }
	    return response;
	}
}
