package com.plants.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.CustomerMain;
import com.plants.entities.Order;

public interface OrderRepo extends JpaRepository<Order, Integer>{
	
	@Query("SELECT o FROM Order o WHERE o.orderId = :OrderNumber")
	Order getOrderNumber(@Param("OrderNumber") String OrderNumber);
	
//	@Query("SELECT o FROM Order o WHERE o.orderId = :OrderNumber AND o.orderStatus = 'NOT_ACCPETED' AND DATE(o.createdAt) = CURRENT_DATE")
//	Order getOrderNumberWithStatusAndCurrentDate(@Param("OrderNumber") String OrderNumber);
	
	@Query("SELECT o FROM Order o WHERE o.agentMain.AgentIDPk = :agentPk and o.orderStatus = 'ACCEPTED'")
	List<Order> getOrderAssignedList(@Param("agentPk") int agentPk);
	
	    
	@Query("SELECT o FROM Order o WHERE o.agentMain.AgentIDPk = :agentPk AND o.orderStatus IN ('ACCEPTED','START','REACHED','START_TIME','END_TIME')")
	Page<Order> getOrderAssignedListPaganation(@Param("agentPk") int agentPk, Pageable pageable);
	
	@Query("SELECT o FROM Order o WHERE o.orderStatus IN ('ACCEPTED','START','REACHED','START_TIME','END_TIME')")
	Page<Order> getOrderAssignedListWebPaganation(Pageable pageable);
	
	@Query("SELECT o FROM Order o WHERE o.customerMain.primarykey = :cusPk AND o.orderStatus IN ('NOT_ACCPETED','ACCEPTED','START','REACHED','START_TIME','END_TIME')")
	Page<Order> geActiveOrderCustomer(@Param("cusPk") int cusPk, Pageable pageable);

    
    @Query("SELECT o FROM Order o WHERE o.agentMain.AgentIDPk = :agentPk and o.orderStatus = 'COMPLETED'")
    Page<Order> getOrderCompletedListPaganation(@Param("agentPk") int agentPk, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.orderStatus = 'COMPLETED'")
    Page<Order> getOrderCompletedListWebPaganation(Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.customerMain.primarykey = :cusPk and o.orderStatus = 'COMPLETED'")
    Page<Order> getCompletedOrderCus(@Param("cusPk") int cusPk, Pageable pageable);
//	@Query("SELECT o FROM Order o WHERE o.agentMain.AgentIDPk = :agentPk and o.orderStatus = 'ACCEPTED'")
//	List<Order> getOrderAssignedListPaganation(@Param("agentPk") int agentPk);
	
	@Query("SELECT MAX(o.orderId) FROM Order o")
	long getMaxOrderId();
	
	@Query("SELECT MAX(CAST(SUBSTRING(o.orderId, 14) AS int)) FROM Order o ")
	Integer getMaxSequenceOrderNumber();

}
