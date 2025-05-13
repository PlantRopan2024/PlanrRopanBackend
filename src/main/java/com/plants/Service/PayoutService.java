package com.plants.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
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
import com.plants.config.Utils;
import com.plants.entities.AgentMain;
import com.plants.entities.DailyRollupOrder;
import com.plants.entities.LoginHours;
import com.plants.entities.OrderEarning;
import com.plants.entities.OrderFertilizers;

@Service
public class PayoutService {

	@Autowired
	private OrderEarningRepo orderEarningRepo;
	
	@Autowired
	LoginHoursRepo loginHoursRepo;
	
	public void insertDataOrderDaily() {
	    try {
	        LocalDate localDate = LocalDate.now(); 

	        // Fetch completed order earnings for the agent on that date
	      //  List<OrderEarning> getTodayOrderComing = orderEarningRepo.getdailyOrderCurrentDate(localDate);

	    //    DailyOrder dailyOrder = new DailyOrder();
	        
	       

	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }
	}



	public void insertDataOrderWeekly() {
		
	}


	public void insertDataOrderMonthly() {
		
	}
	
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
	        long totalWorkingMinutes = 0;


	        long hr = 0;
	        long minutes = 0;


	        for (OrderEarning orderEarning : getOrderEarning) {
	            if ("COMPLETED".equalsIgnoreCase(orderEarning.getEarningStatus())) {
	                completedOrders++;
	            }

	            agentPerOrderEarningRs += orderEarning.getAgentEarningRs();
	         // Calculate duration between start and end time
                LocalTime startTime = orderEarning.getOrders().getStartTime();
                LocalTime endTime = orderEarning.getOrders().getEndTime();

                if (startTime != null && endTime != null && endTime.isAfter(startTime)) {
                    Duration duration = Duration.between(startTime, endTime);
                    totalWorkingMinutes += duration.toMinutes();
                }	            
//	            if (orderEarning.getOrders() != null && orderEarning.getOrders().getOrderFertilizers() != null) {
//	                for (OrderFertilizers fert : orderEarning.getOrders().getOrderFertilizers()) {
//	                    agentFertilizer += fert.getEarningMalliFertilizer();
//	                }
//	            }

	           
	        }
	        
	        long totalWorkingHours = totalWorkingMinutes / 60;
	        long remainingMinutes = totalWorkingMinutes % 60;
	        String formattedWorkingTime = String.format("%02d:%02d", totalWorkingHours, remainingMinutes);

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
	        response.put("orderEarningsPerDay", Utils.decimalFormat(agentPerOrderEarningRs));
	        response.put("gardenersTips", totalTips);
	        response.put("todayDate", Utils.formatDate(localDate));
	        response.put("workingHours", formattedWorkingTime);
	        response.put("agentOrderEarningsTotal", Utils.decimalFormat(agentTotalEarningRs));
	        //response.put("fertilizerEarning", agentFertilizer);
	        response.put("loginHours", totalLoginhr);
	        response.put("status", true);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("message", "Something Went Wrong");
	        response.put("status", false);
	    }
	    return ResponseEntity.ok(response);
	}


	public ResponseEntity<Map<String, Object>> weeklyOrderEarning(AgentMain existingAgent, String date) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate inputDate = LocalDate.parse(date, formatter);

	        System.out.println(" inputDate  of date " + inputDate);

	        // Get start (Monday) and end (Sunday) of the week
	        LocalDate startOfWeek = inputDate.with(DayOfWeek.MONDAY);
	        
	        System.out.println(" start of week " + startOfWeek);
	        LocalDate endOfWeek = inputDate.with(DayOfWeek.SUNDAY);
	        
	        System.out.println(" endOfWeek of week " + endOfWeek);


	        // Fetch weekly order earnings
	        List<OrderEarning> getOrderEarning = orderEarningRepo.getWeeklyOrderEarn(existingAgent.getAgentIDPk(), startOfWeek, endOfWeek);

	        double agentTotalEarningRs = 0.0;
	        double agentPerOrderEarningRs = 0.0;
	     //   double agentFertilizer = 0.0;
	        double totalIncentives = 0.0;
	        double totalTips = 0.0;
	        int completedOrders = 0;
	        long totalWorkingMinutes = 0;
	        long hr = 0;
	        long minutes = 0;

	        for (OrderEarning orderEarning : getOrderEarning) {
	            if ("COMPLETED".equalsIgnoreCase(orderEarning.getEarningStatus())) {
	                completedOrders++;
	            }

	            agentPerOrderEarningRs += orderEarning.getAgentEarningRs();
	            
	            LocalTime startTime = orderEarning.getOrders().getStartTime();
                LocalTime endTime = orderEarning.getOrders().getEndTime();

                if (startTime != null && endTime != null && endTime.isAfter(startTime)) {
                    Duration duration = Duration.between(startTime, endTime);
                    totalWorkingMinutes += duration.toMinutes();
                }	
//	            if (orderEarning.getOrders() != null && orderEarning.getOrders().getOrderFertilizers() != null) {
//	                for (OrderFertilizers fert : orderEarning.getOrders().getOrderFertilizers()) {
//	                    agentFertilizer += fert.getEarningMalliFertilizer();
//	                }
//	            }
	        }
	        
	        long totalWorkingHours = totalWorkingMinutes / 60;
	        long remainingMinutes = totalWorkingMinutes % 60;
	        String formattedWorkingTime = String.format("%02d:%02d", totalWorkingHours, remainingMinutes);

	        // Total earnings
	        agentTotalEarningRs = agentPerOrderEarningRs  + totalTips + totalIncentives;

	        // Get weekly login hours
	        List<LoginHours> getLoginHours = loginHoursRepo.getLoginHoursBetweenDates(existingAgent.getAgentIDPk(), startOfWeek, endOfWeek);
	        for (LoginHours loginhr : getLoginHours) {
	            hr += loginhr.getHr();
	            minutes += loginhr.getMinutes();
	        }

	        // Format login hours
	        String totalLoginhr = String.format("%02d:%02d", hr, minutes);

	        // Final response
	        response.put("completedOrder", completedOrders);
	        response.put("incentivestotal", totalIncentives);
	        response.put("orderEarningsPerDay", Utils.decimalFormat(agentPerOrderEarningRs));
	        response.put("gardenersTips", totalTips);
	        response.put("agentOrderEarningsTotal", Utils.decimalFormat(agentTotalEarningRs));
	    //    response.put("fertilizerEarning", agentFertilizer);
	        response.put("startOfWeek", Utils.formatDate(startOfWeek));
	        response.put("endOfWeek", Utils.formatDate(endOfWeek));
	        response.put("workingHours", formattedWorkingTime);
	        response.put("loginHours", totalLoginhr);
	        response.put("status", true);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("message", "Something Went Wrong");
	        response.put("status", false);
	    }
	    return ResponseEntity.ok(response);
	}

	public ResponseEntity<Map<String, Object>> monthlyOrderEarning(AgentMain existingAgent, String date) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate inputDate = LocalDate.parse(date, formatter);
	        
	        System.out.println(" input date " + inputDate);

	        LocalDate startOfMonth = inputDate.withDayOfMonth(1);
	        
	        System.out.println(" startOfMonth   date " + startOfMonth);

	        LocalDate endOfMonth = inputDate.withDayOfMonth(inputDate.lengthOfMonth());
	        
	        System.out.println(" endOfMonthdate " + endOfMonth);


	        List<OrderEarning> orderEarnings = orderEarningRepo.getMonthlyOrderEarn(existingAgent.getAgentIDPk(), startOfMonth, endOfMonth);

	        double agentTotalEarningRs = 0.0;
	        double agentPerOrderEarningRs = 0.0;
	        double agentFertilizer = 0.0;
	        double totalIncentives = 0.0;
	        double totalTips = 0.0;
	        int completedOrders = 0;
	        long totalWorkingMinutes = 0;
	        long hr = 0;
	        long minutes = 0;

	        for (OrderEarning orderEarning : orderEarnings) {
	            if ("COMPLETED".equalsIgnoreCase(orderEarning.getEarningStatus())) {
	                completedOrders++;
	            }

	            agentPerOrderEarningRs += orderEarning.getAgentEarningRs();
	            
	            LocalTime startTime = orderEarning.getOrders().getStartTime();
                LocalTime endTime = orderEarning.getOrders().getEndTime();

                if (startTime != null && endTime != null && endTime.isAfter(startTime)) {
                    Duration duration = Duration.between(startTime, endTime);
                    totalWorkingMinutes += duration.toMinutes();
                }	
//	            if (orderEarning.getOrders() != null && orderEarning.getOrders().getOrderFertilizers() != null) {
//	                for (OrderFertilizers fert : orderEarning.getOrders().getOrderFertilizers()) {
//	                    agentFertilizer += fert.getEarningMalliFertilizer();
//	                }
//	            }
	        }
	        
	        long totalWorkingHours = totalWorkingMinutes / 60;
	        long remainingMinutes = totalWorkingMinutes % 60;
	        String formattedWorkingTime = String.format("%02d:%02d", totalWorkingHours, remainingMinutes);

	        
	        agentTotalEarningRs = agentPerOrderEarningRs + totalTips + totalIncentives;

	        // Fetch login hours in the month
	        List<LoginHours> getLoginHours = loginHoursRepo.getLoginHoursBetweenDates(existingAgent.getAgentIDPk(), startOfMonth, endOfMonth);
	        for (LoginHours loginhr : getLoginHours) {
	            hr += loginhr.getHr();
	            minutes += loginhr.getMinutes();
	        }

	        String totalLoginhr = String.format("%02d:%02d", hr, minutes);

	        response.put("completedOrder", completedOrders);
	        response.put("incentivestotal", totalIncentives);
	        response.put("orderEarningsPerDay", Utils.decimalFormat(agentPerOrderEarningRs));
	        response.put("gardenersTips", totalTips);
	        response.put("agentOrderEarningsTotal", Utils.decimalFormat(agentTotalEarningRs));
	        response.put("fertilizerEarning", agentFertilizer);
	        response.put("startOfMonth", Utils.formatDate(startOfMonth));
	        response.put("endOfMonth", Utils.formatDate(endOfMonth));
	        response.put("workingHours", formattedWorkingTime);
	        response.put("loginHours", totalLoginhr);
	        response.put("status", true);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("message", "Something Went Wrong");
	        response.put("status", false);
	    }
	    return ResponseEntity.ok(response);
	}


}
