package com.plants.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.plants.Dao.CusReferralDao;
import com.plants.Dao.CustomerDao;
import com.plants.Dao.NotificationRepo;
import com.plants.Dao.WalletHistoryRepo;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;
import com.plants.entities.AgentReferral;
import com.plants.entities.CusReferral;
import com.plants.entities.CustomerMain;
import com.plants.entities.WalletHistory;

@Service
public class cusReferralService {
	
	@Value("${pagination.page}")
	private int page;
	
	@Value("${pagination.size}")
	private int size;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private CusReferralDao cusReferralDao;
	
	@Autowired
	private WalletHistoryRepo walletHistoryRepo;
	
	@Autowired
	private NotificationRepo notificationRepo;
	
	public ResponseEntity<Map<String, Object>> assignReferralCustomer(CustomerMain exitsCustomer, Map<String, String> request) {
	    Map<String, Object> response = new HashMap<>();
	    String referralCode = request.get("referralCode");

	    if (referralCode.isEmpty() || referralCode == null) {
	        response.put("status", true);
	        response.put("message", "Referral Code is Empty!");
	        return ResponseEntity.ok(response);
	    }
	    try {
	        if (this.cusReferralDao.getUseReferralCodeCus(referralCode, exitsCustomer.getPrimarykey()) != null) {
	            response.put("status", true);
	            response.put("message", "You have already used this code.");
	            return ResponseEntity.ok(response);
	        }

	        System.out.println(" referral code  -- " + referralCode);
	        CustomerMain cusMainReferral = this.customerDao.getReferralCodeCus(referralCode.trim());
	        if (cusMainReferral == null) {
	            response.put("status", true);
	            response.put("message", "Referral Code is Invalid!");
	            return ResponseEntity.ok(response);
	        }

	        // Save referral record
	        CusReferral cusReferral = new CusReferral();
	        cusReferral.setReferralCode(referralCode);
	        cusReferral.setReferrerCustId(cusMainReferral.getPrimarykey());
	        cusReferral.setReferredCustId(exitsCustomer.getPrimarykey());
	        cusReferral.setCreatedAt(LocalDateTime.now());
	        cusReferral = cusReferralDao.save(cusReferral);

	        // Save Wallet Transaction
			  WalletHistory walletHistory = new WalletHistory();
			  walletHistory.setAmount(50.00);
			  walletHistory.setDescription("Refer Cashback Received");
			  walletHistory.setTransactionType("REWARD");
			  walletHistory.setCustomerMain(cusMainReferral);
			  walletHistory.setCreatedAt(LocalDateTime.now());
			  walletHistory.setCusReferral(cusReferral);
			  walletHistoryRepo.save(walletHistory);
			 

	        // Save Notification
	        String message = "Dear Customer,\n\n" +
	                "Your referral code has been successfully applied! ₹50 will be credited to your account after the first service is completed with Plant Ropan.\n\n" +
	                "Thank you for spreading the greenery!\n\n" +
	                "Happy Gardening,\n" +
	                "Team Plant Ropan";
	        
	        try {
	            sendNotificationToAgent(cusMainReferral, "Referral Reward Earned 🎉", message);
	        } catch (Exception e) {
	            e.printStackTrace();
	            response.put("notification_status", "failed");
	        }

	        com.plants.entities.Notification notification = new com.plants.entities.Notification();
	        notification.setMessage(message);
	        notification.setTitle("Referral Reward Earned 🎉");
	        notification.setTypeIId("Customer");
	        notification.setCustomerMain(cusMainReferral);
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
	
	public ResponseEntity<Map<String, Object>> walletHistoryCust(CustomerMain existingCust, int page, int size) {
	    Map<String, Object> response = new HashMap<>();
	    double totalAmount = 0.0;
	    List<Map<String, Object>> historyList = new ArrayList<>();
	    try {
	        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
	        Page<WalletHistory> walletPage = walletHistoryRepo.findByCustomerMain(existingCust, pageable);
	        if (walletPage.isEmpty()) {
	            response.put("status", true);
	            response.put("message", "No Wallet found");
	            response.put("totalWalletBalance", totalAmount);
	            response.put("referralHistory", historyList);
	            response.put("currentPage", 0);
	            response.put("totalPages", 0);
	            response.put("totalElements", 0);
	            return ResponseEntity.ok(response);
	        }
	        for (WalletHistory walletHistory : walletPage.getContent()) {
	            Map<String, Object> historyEntry = new HashMap<>();
	            historyEntry.put("id", walletHistory.getId());
	            historyEntry.put("description", walletHistory.getDescription());
	            historyEntry.put("amount", walletHistory.getAmount());
	            historyEntry.put("transactionType", walletHistory.getTransactionType());
	            historyEntry.put("createdAt", walletHistory.getCreatedAt());
	            totalAmount += walletHistory.getAmount();
	            historyList.add(historyEntry);
	        }
	        response.put("status", true);
	        response.put("message", "Wallet found");
	        response.put("totalWalletBalance", totalAmount);
	        response.put("referralHistory", historyList);
	        response.put("currentPage", walletPage.getNumber());
	        response.put("totalPages", walletPage.getTotalPages());
	        response.put("totalElements", walletPage.getTotalElements());

	        // **Next Page URL**
	        if (walletPage.hasNext()) {
	            String nextPageUrl = "/cusReferral/getWalletHistoryCust?pageNumber=" + (walletPage.getNumber() + 1) + "&pageSize=" + size;
	            response.put("nextPageUrl", nextPageUrl);
	        } else {
	            response.put("nextPageUrl", null);
	        }

	        // **Previous Page URL**
	        if (walletPage.hasPrevious()) {
	            String prevPageUrl = "/cusReferral/getWalletHistoryCust?pageNumber=" + (walletPage.getNumber() - 1) + "&pageSize=" + size;
	            response.put("prevPageUrl", prevPageUrl);
	        } else {
	            response.put("prevPageUrl", null);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", false);
	        response.put("message", "Something went wrong!");
	    }
	    return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> getNotificationHistoryCus(CustomerMain existingCustomer, int page, int size) {
	    Map<String, Object> response = new HashMap<>();
	    List<Map<String, Object>> notifyList = new ArrayList<>();
	    try {
	        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
	        Page<com.plants.entities.Notification> notificationPage = notificationRepo.findByCustomerMainAndTypeIId(existingCustomer, "Customer", pageable);

	        for (com.plants.entities.Notification notification : notificationPage.getContent()) {
	            Map<String, Object> notifyHist = new HashMap<>();
	            notifyHist.put("id", notification.getId());
	            notifyHist.put("message", notification.getMessage());
	            notifyHist.put("title", notification.getTitle());
	            notifyHist.put("date", notification.getCreatedAt());
	            notifyHist.put("isRead", notification.isRead());
	            notifyList.add(notifyHist);
	        }

	        response.put("notifyHistory", notifyList);
	        response.put("message", notificationPage.isEmpty() ? "No Notifications found" : "Notifications found");
	        response.put("status", !notificationPage.isEmpty());
	        response.put("currentPage", notificationPage.getNumber());
	        response.put("totalPages", notificationPage.getTotalPages());
	        response.put("totalElements", notificationPage.getTotalElements());

	        // Next Page URL
	        if (notificationPage.hasNext()) {
	            String nextPageUrl = "/cusReferral/getNotificationHistoryCus?pageNumber=" + (notificationPage.getNumber() + 1) + "&pageSize=" + size;
	            response.put("nextPageUrl", nextPageUrl);
	        } else {
	            response.put("nextPageUrl", null);
	        }

	        // Previous Page URL
	        if (notificationPage.hasPrevious()) {
	            String prevPageUrl = "/cusReferral/getNotificationHistoryCus?pageNumber=" + (notificationPage.getNumber() - 1) + "&pageSize=" + size;
	            response.put("prevPageUrl", prevPageUrl);
	        } else {
	            response.put("prevPageUrl", null);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", false);
	        response.put("message", "Something went wrong!");
	    }

	    return ResponseEntity.ok(response);
	}

	public String sendNotificationToAgent(CustomerMain customerMain, String title, String message) {
	    String response = "";
	    try {
	    	if (customerMain.getFirebasetokenCus() == null || customerMain.getFirebasetokenCus().isEmpty()) {
	            return "FCM Token is missing";
	        }
	        Message firebaseMessage = Message.builder()
	            .setToken(customerMain.getFirebasetokenCus()) 
	            .setNotification(Notification.builder()
	                .setTitle(title)
	                .setBody(message)
	                .build())
	            .build();
	        response = FirebaseMessaging.getInstance().send(firebaseMessage);
	        System.out.println("Successfully sent message: " + response);
	    } catch (FirebaseMessagingException e) {
	        e.printStackTrace();
	    }
	    return response;
	}
}
