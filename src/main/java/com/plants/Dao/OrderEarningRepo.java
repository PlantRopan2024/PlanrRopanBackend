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
	
	
	@Query("SELECT e FROM OrderEarning e WHERE  FUNCTION('DATE', e.createdAt) = :date AND e.earningStatus = 'COMPLETED'")
	List<OrderEarning> getdailyOrderCurrentDate(@Param("date") LocalDate date);
	
	@Query("SELECT e FROM OrderEarning e WHERE e.agentMain.AgentIDPk = :agentId AND FUNCTION('DATE', e.createdAt) BETWEEN :startDate AND :endDate AND e.earningStatus = 'COMPLETED'")
	List<OrderEarning> getWeeklyOrderEarn(@Param("agentId") int agentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query("SELECT e FROM OrderEarning e WHERE e.agentMain.AgentIDPk = :agentId AND FUNCTION('DATE', e.createdAt) BETWEEN :startDate AND :endDate AND e.earningStatus = 'COMPLETED'")
	List<OrderEarning> getMonthlyOrderEarn(@Param("agentId") int agentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
