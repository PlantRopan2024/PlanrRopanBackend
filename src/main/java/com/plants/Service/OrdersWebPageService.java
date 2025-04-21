package com.plants.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.plants.Dao.OrderRepo;
import com.plants.Dao.RejectedOrdersRepo;
import com.plants.config.Utils;
import com.plants.entities.AgentMain;
import com.plants.entities.Order;
import com.plants.entities.RejectedOrders;

@Service
public class OrdersWebPageService {
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired 
	private RejectedOrdersRepo rejectedOrdersRepo;
	
	public ResponseEntity<Map<String, Object>> getListAccpetOrder(int pageNumber, int pageSize, String baseUrl) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        int pageIndex = Math.max(pageNumber - 1, 0);
	        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending());
	        Page<Order> getOrdersDetails = orderRepo.getOrderAssignedListWebPaganation(pageable);

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
	
	public ResponseEntity<Map<String, Object>> getCompletedOrder(int pageNumber, int pageSize, String baseUrl) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        int pageIndex = Math.max(pageNumber - 1, 0);
	        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending());
	        Page<Order> getOrdersDetails = orderRepo.getOrderCompletedListWebPaganation( pageable);

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
	
	public ResponseEntity<Map<String, Object>> getRejectedOrderList(int pageNumber, int pageSize, String baseUrl) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        int pageIndex = Math.max(pageNumber - 1, 0);
	        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending());
	        Page<RejectedOrders> getOrdersDetails = this.rejectedOrdersRepo.getRejectedOrderListWebPaganation(pageable);

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
}
