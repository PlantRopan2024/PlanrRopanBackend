package com.plants.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.plants.Dao.LoginHoursRepo;
import com.plants.Dao.OrderEarningRepo;
import com.plants.entities.AgentMain;
import com.plants.entities.LoginHours;
import com.plants.entities.OrderEarning;
import com.plants.entities.OrderFertilizers;

@Service
public class PayoutService {

	@Autowired
	private OrderEarningRepo orderEarningRepo;
	
	@Autowired
	LoginHoursRepo loginHoursRepo;
	
	
	public ResponseEntity<Map<String, Object>> dailyOrderEarning(AgentMain existingAgent, String date) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate localDate = LocalDate.parse(date, formatter);

	        // Fetch completed order earnings for the agent on that date
	        List<OrderEarning> getOrderEarning = orderEarningRepo.getdailyOrderEarn(existingAgent.getAgentIDPk(), localDate);

	        double agentTotalEarningRs = 0.0;
	        double agentPerOrderEarningRs = 0.0;
	        double agentFertilizer = 0.0;
	        double totalIncentives = 0.0;
	        double totalTips = 0.0;
	        int completedOrders = 0;
	        long hr = 0;
	        long minutes = 0;

	        // Prepare individual order data
	        List<Map<String, Object>> orderDetailsList = new ArrayList<>();

	        for (OrderEarning orderEarning : getOrderEarning) {
	            if ("COMPLETED".equalsIgnoreCase(orderEarning.getEarningStatus())) {
	                completedOrders++;
	            }

	            agentPerOrderEarningRs += orderEarning.getAgentEarningRs();

	            double fertilizerEarning = 0.0;
	            if (orderEarning.getOrders() != null && orderEarning.getOrders().getOrderFertilizers() != null) {
	                for (OrderFertilizers fert : orderEarning.getOrders().getOrderFertilizers()) {
	                    fertilizerEarning += fert.getEarningMalliFertilizer();
	                    agentFertilizer += fert.getEarningMalliFertilizer();
	                }
	            }

	            // Add individual order entry
	            Map<String, Object> orderMap = new HashMap<>();
	            orderMap.put("orderNumber", orderEarning.getOrders().getOrderId());
	            orderMap.put("agentEarningRs", orderEarning.getAgentEarningRs());
	            orderMap.put("fertilizerEarning", fertilizerEarning);
	            orderDetailsList.add(orderMap);
	        }

	        // Total earnings = per order + tips + incentives
	        agentTotalEarningRs = agentPerOrderEarningRs + totalTips + totalIncentives;

	        // Login hours calculation
	        List<LoginHours> getLoginHours = this.loginHoursRepo.getCountActiveLogin(existingAgent.getAgentIDPk(), localDate);
	        for (LoginHours loginhr : getLoginHours) {
	            hr += loginhr.getHr();
	            minutes += loginhr.getMinutes();
	        }

	        // Format login time correctly
	        String totalLoginhr = String.format("%02d:%02d", hr, minutes);

	        // Prepare response map
	        response.put("completedOrder", completedOrders);
	        response.put("incentivestotal", totalIncentives);
	        response.put("orderEarningsPerDay", agentPerOrderEarningRs);
	        response.put("gardenersTips", totalTips);
	        response.put("agentOrderEarningsTotal", agentTotalEarningRs);
	        response.put("fertilizerEarning", agentFertilizer);
	        response.put("loginHours", totalLoginhr);
	        response.put("orderDetails", orderDetailsList); // Include order list
	        response.put("status", true);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("message", "Something Went Wrong");
	        response.put("status", false);
	    }
	    return ResponseEntity.ok(response);
	}


}
