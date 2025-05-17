package com.plants.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.plants.Dao.DailyRollupOrderRepo;
import com.plants.Dao.HourlyRollupOrderRepo;
import com.plants.Dao.OrderRepo;
import com.plants.config.Utils;
import com.plants.entities.DailyRollupOrder;
import com.plants.entities.HourlyRollupOrder;
import com.plants.entities.Order;
import com.plants.entities.OrderEarning;

import jakarta.transaction.Transactional;

@Service
public class RollupService {

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private DailyRollupOrderRepo dailyRollupOrderRepo;

	@Autowired
	private HourlyRollupOrderRepo hourlyRollupOrderRepo;

	@Transactional
	public ResponseEntity<Map<String, Object>> hourlyDataRollup() {
		Map<String, Object> response = new HashMap<>();

		try {
			double totalAmount = 0;
			double plansRs = 0;
			double companyEarn = 0;
			double agentEarn = 0;
			double platformFees = 0;
			double couponRs = 0;
			int orderCount = 0;

			LocalDateTime now =Utils.getCurrentDateTimeInIST(LocalDateTime.now());
			System.out.println(" now  curretn date " + now);

			LocalDateTime endHours = now.withMinute(0).withSecond(0).withNano(0);
			LocalDateTime startHours = endHours.minusHours(1);

			System.out.println(" start hours " + endHours);
			System.out.println(" end hours " + startHours);
			
			
			List<Order> listOrder = orderRepo.getOrderDateWise(startHours, endHours);
			orderCount = listOrder.size();
			System.out.println("Orders in " + orderCount);

			for (Order order : listOrder) {
				for (OrderEarning orderEarning : order.getOrderEarnings()) {
					totalAmount += orderEarning.getGrandTotalAmount();
					plansRs += orderEarning.getPlansRs();
					companyEarn += orderEarning.getCompanyEarningRs();
					agentEarn += orderEarning.getAgentEarningRs();
					platformFees += orderEarning.getPlatformFess();
					couponRs += orderEarning.getCouponAppliedRs();
				}
			}

			HourlyRollupOrder hourlyRollup = new HourlyRollupOrder();
			hourlyRollup.setAgentEarningRs(agentEarn);
			hourlyRollup.setCompanyEarningRs(companyEarn);
			hourlyRollup.setCouponAppliedRs(couponRs);
			hourlyRollup.setGrandTotalAmount(totalAmount);
			hourlyRollup.setPlansRs(plansRs);
			hourlyRollup.setPlatformFess(platformFees);
			hourlyRollup.setCreatedAt(Utils.getCurrentDateTimeInIST(LocalDateTime.now()));
			hourlyRollup.setTotalOrder(orderCount);
			hourlyRollup.setRollupAt(Utils.getCurrentDateInIST());
			hourlyRollup.setStartHours(startHours);
			hourlyRollup.setEndHours(endHours);

			this.hourlyRollupOrderRepo.save(hourlyRollup);

			response.put("status", true);
			response.put("message", "Hourly rollup completed successfully.");
			response.put("ordersRolledUp", orderCount);
			response.put("totalAmount", totalAmount);
			response.put("agentEarning", agentEarn);
			response.put("companyEarning", companyEarn);
			response.put("platformFees", platformFees);
			response.put("couponApplied", couponRs);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something went wrong during hourly rollup.");
		}
		return ResponseEntity.ok(response);
	}
	
	
	@Transactional
	public ResponseEntity<Map<String, Object>> dailyRollupData() {
		Map<String, Object> response = new HashMap<>();

		try {
			double totalAmount = 0;
			double plansRs = 0;
			double companyEarn = 0;
			double agentEarn = 0;
			double platformFees = 0;
			double couponRs = 0;
			int orderCount = 0;
			
			LocalDate today = Utils.getCurrentDateInIST();
			System.out.println(" now  today date " + today);

			LocalDateTime startOfDay = today.atStartOfDay(); // 00:00:00
			LocalDateTime endOfDay = startOfDay.plusDays(1); // next day's 00:00:00

			System.out.println("Start of day: " + startOfDay);
			System.out.println("End of day: " + endOfDay);

			
			List<HourlyRollupOrder> liHourlyRollupOrders = this.hourlyRollupOrderRepo.getHourlyRollup(today);
			
			System.out.println("  hourly Orders in " + liHourlyRollupOrders.size());

			
				for (HourlyRollupOrder orderEarning : liHourlyRollupOrders) {
					totalAmount += orderEarning.getGrandTotalAmount();
					plansRs += orderEarning.getPlansRs();
					companyEarn += orderEarning.getCompanyEarningRs();
					agentEarn += orderEarning.getAgentEarningRs();
					platformFees += orderEarning.getPlatformFess();
					couponRs += orderEarning.getCouponAppliedRs();
				}
			

			DailyRollupOrder dailyRollupOrder = new DailyRollupOrder();
			dailyRollupOrder.setAgentEarningRs(agentEarn);
			dailyRollupOrder.setCompanyEarningRs(companyEarn);
			dailyRollupOrder.setCouponAppliedRs(couponRs);
			dailyRollupOrder.setGrandTotalAmount(totalAmount);
			dailyRollupOrder.setPlansRs(plansRs);
			dailyRollupOrder.setPlatformFess(platformFees);
			dailyRollupOrder.setCreatedAt(Utils.getCurrentDateTimeInIST(LocalDateTime.now()));
			dailyRollupOrder.setTotalOrder(orderCount);
			dailyRollupOrder.setRollupAt(Utils.getCurrentDateInIST());
			dailyRollupOrder.setStartOfDay(startOfDay);
			dailyRollupOrder.setEndOfDay(endOfDay);

			this.dailyRollupOrderRepo.save(dailyRollupOrder);

			response.put("status", true);
			response.put("message", "Hourly rollup completed successfully.");
			response.put("ordersRolledUp", orderCount);
			response.put("totalAmount", totalAmount);
			response.put("agentEarning", agentEarn);
			response.put("companyEarning", companyEarn);
			response.put("platformFees", platformFees);
			response.put("couponApplied", couponRs);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", false);
			response.put("message", "Something went wrong during hourly rollup.");
		}

		return ResponseEntity.ok(response);
	}
	
	
}
