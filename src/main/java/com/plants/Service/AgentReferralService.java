package com.plants.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.plants.entities.AgentMain;
import com.plants.entities.AgentReferral;
import com.plants.entities.WalletHistory;

@Service
public class AgentReferralService {
	
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
	        response.put("status", "error");
	        response.put("message", "Referral Code is Empty!");
	        return ResponseEntity.ok(response);
	    }
	    try {
	        if (agentReferralRepository.getUseReferralCode(referralCode, existingAgent.getAgentIDPk()) != null) {
	            response.put("status", "warning");
	            response.put("message", "You have already used this code.");
	            return ResponseEntity.ok(response);
	        }

	        // Get the agent associated with this referral code
	        System.out.println(" referral code  -- " + referralCode);
	        AgentMain agentMainReferral = this.agentMainRepo.getReferralCodeAgent(referralCode.trim());
	        if (agentMainReferral == null) {
	            response.put("status", "warning");
	            response.put("message", "Referral Code is Invalid!");
	            return ResponseEntity.ok(response);
	        }

	        // Save referral record
	        AgentReferral agentReferral = new AgentReferral();
	        agentReferral.setReferralCode(referralCode);
	        agentReferral.setReferrerAgentId(agentMainReferral.getAgentIDPk());
	        agentReferral.setReferredAgentId(existingAgent.getAgentIDPk());
	        agentReferral.setCreatedAt(LocalDateTime.now());
	        agentReferral = agentReferralRepository.save(agentReferral);

	        // Save Wallet Transaction
			/*
			 * WalletHistory walletHistory = new WalletHistory();
			 * walletHistory.setAmount(50.00);
			 * walletHistory.setDescription("Refer Cashback Received");
			 * walletHistory.setTransactionType("REWARD");
			 * walletHistory.setAgentMain(agentMainReferral);
			 * walletHistory.setCreatedAt(LocalDateTime.now());
			 * walletHistory.setAgentReferral(agentReferral);
			 * walletHistoryRepo.save(walletHistory);
			 */

	        // Save Notification
	        String message = "Dear Gardener,\n\n" +
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

	        response.put("status", "success");
	        response.put("message", "Referral Saved!");
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", "error");
	        response.put("message", "An unexpected error occurred.");
	    }
	    
	    return ResponseEntity.ok(response);
	}

	
	public ResponseEntity<Map<String, Object>> walletHistoryAgent(AgentMain existingAgent) {
	    Map<String, Object> response = new HashMap<>();
	    double totalAmount = 0.0; // Variable to store total amount
	    List<Map<String, Object>> historyList = new ArrayList<>();
	    try {
	        for (WalletHistory walletHistory : existingAgent.getReferralHistory()) {
	            Map<String, Object> historyEntry = new HashMap<>();
	            historyEntry.put("id", walletHistory.getId());
	            historyEntry.put("description", walletHistory.getDescription());
	            historyEntry.put("amount", walletHistory.getAmount());
	            historyEntry.put("transactionType", walletHistory.getTransactionType());
	            historyEntry.put("createdAt", walletHistory.getCreatedAt());
	            // Add to total amount
	            totalAmount += walletHistory.getAmount();
	            historyList.add(historyEntry);
	        }
	        if (historyList.isEmpty()) {
	            response.put("status", "warning");
	            response.put("message", "No Wallet found");
	        }else {
	        	response.put("referralHistory", historyList);
		        response.put("totalWalletBalance", totalAmount);
		        response.put("status", "success");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", "error");
	        response.put("message", "Failed to fetch wallet history");
	    }
	    return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Map<String, Object>> getNotificationHistoryAgent(AgentMain existingAgent) {
	    Map<String, Object> response = new HashMap<>();
	    List<Map<String, Object>> notifyList = new ArrayList<>();
	    try {
	        for (com.plants.entities.Notification notificationHistory : existingAgent.getNotification()) {
	            Map<String, Object> notifyhist = new HashMap<>();
	            notifyhist.put("id", notificationHistory.getId());
	            notifyhist.put("message", notificationHistory.getMessage());
	            notifyhist.put("title", notificationHistory.getTitle());
	            notifyhist.put("date", notificationHistory.getCreatedAt());
	            notifyhist.put("isRead", notificationHistory.isRead());
	            notifyList.add(notifyhist);
	        }
	        if (notifyList.isEmpty()) {
	            response.put("status", "warning");
	            response.put("message", "No Notifications found");
	        }else {
	            response.put("notifyHistory", notifyList);
	            response.put("status", "success");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", "error");
	        response.put("message", "Failed to fetch Notification history");
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
