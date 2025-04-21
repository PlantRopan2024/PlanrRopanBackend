package com.plants.Dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.RejectedOrders;

public interface RejectedOrdersRepo extends JpaRepository<RejectedOrders, Integer> {
	
	 @Query("SELECT o FROM RejectedOrders o WHERE o.agents.AgentIDPk = :agentPk and o.orderStatus = 'CANCELED'")
	 Page<RejectedOrders> getRejectedOrderListPaganation(@Param("agentPk") int agentPk, Pageable pageable);
	 
	 @Query("SELECT o FROM RejectedOrders o WHERE o.orderStatus = 'CANCELED'")
	 Page<RejectedOrders> getRejectedOrderListWebPaganation(Pageable pageable);
}
