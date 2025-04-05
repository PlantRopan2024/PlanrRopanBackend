package com.plants.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.OrderEarning;

public interface OrderEarningRepo extends JpaRepository<OrderEarning, Integer>{
	
	@Query("SELECT e FROM OrderEarning e where e.orderNumber = :orderNumber")
	OrderEarning getbyOrderNumber(@Param("orderNumber") String orderNumber);
	
	@Query("SELECT e FROM OrderEarning e WHERE e.agentMain.AgentIDPk = :agentId AND FUNCTION('DATE', e.createdAt) = :date AND e.earningStatus = 'COMPLETED'")
	List<OrderEarning> getdailyOrderEarn(@Param("agentId") int agentId, @Param("date") LocalDate date);

}
