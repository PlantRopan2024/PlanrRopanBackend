package com.plants.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.Order;

public interface OrderRepo extends JpaRepository<Order, Integer>{
	
	@Query("SELECT o FROM Order o WHERE o.orderId = :OrderNumber")
	Order getOrderNumber(@Param("OrderNumber") String OrderNumber);
	
	@Query("SELECT MAX(o.orderId) FROM Order o")
	long getMaxOrderId();
	
	@Query("SELECT MAX(CAST(SUBSTRING(o.orderId, 14) AS int)) FROM Order o ")
	Integer getMaxSequenceOrderNumber();

}
