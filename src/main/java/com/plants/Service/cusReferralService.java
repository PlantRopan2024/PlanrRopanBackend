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
			  String message = "Dear Plant Lover,\n\n" +
		                 "A big thank you for referring [Referred Person’s Name] to Plant Ropan!\n\n" +
		                 "As a token of our appreciation, ₹50 will be credited to your wallet after their first service is completed with us.\n\n" +
		                 //"We truly appreciate your support in making the world greener!\n\n" +
		                 "Thank you for helping us spread the greener!\n\n" +
		                 "Happy Gardening,\n" +
		                 "Team Plant Ropan";
	        try {
	            sendNotificationToCust(cusMainReferral, "Referral Reward Earned 🎉", message);
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
	
	
	public ResponseEntity<Map<String, Object>> getNotificationHistoryCus(CustomerMain existingCustomer, int pageNumber,
			int pageSize, String baseUrl) {
		Map<String, Object> response = new HashMap<>();
		try {
			int pageIndex = Math.max(pageNumber - 1, 0);
			Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending());
	        Page<com.plants.entities.Notification> notificationPage = notificationRepo.findByCustomerMainAndTypeIId(existingCustomer, "Customer", pageable);
	       
			if (notificationPage.isEmpty()) {
				List<Map<String, Object>> emptyOrdersList = new ArrayList<>();
				Map<String, Object> pagination = Utils.buildPaginationResponse(notificationPage, baseUrl, pageSize,
						emptyOrdersList);
				pagination.put("message", "No Notifications found");
				response.put("success", true);
				response.put("response", pagination);
				return ResponseEntity.ok(response);
			}

			List<Map<String, Object>> notifyList = new ArrayList<>();
			for (com.plants.entities.Notification notification : notificationPage) {
				Map<String, Object> notifyHist = new HashMap<>();
				notifyHist.put("id", notification.getId());
				notifyHist.put("message", notification.getMessage());
				notifyHist.put("title", notification.getTitle());
				notifyHist.put("date", notification.getCreatedAt());
				notifyHist.put("isRead", notification.isRead());
				notifyList.add(notifyHist);
			}
			Map<String, Object> pagination = Utils.buildPaginationResponse(notificationPage, baseUrl, pageSize,
					notifyList);
			response.put("success", true);
			response.put("response", pagination);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Something went wrong.");
		}
		return ResponseEntity.ok(response);
	}

	public String sendNotificationToCust(CustomerMain customerMain, String title, String message) {
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
