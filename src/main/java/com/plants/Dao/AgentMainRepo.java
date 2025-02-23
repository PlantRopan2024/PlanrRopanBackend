package com.plants.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.AgentMain;

public interface AgentMainRepo extends JpaRepository<AgentMain, Integer>{
	
	
	@Query("SELECT e FROM AgentMain e WHERE LOWER(e.agentReferralCode) = LOWER(:referralCode)")
	AgentMain getReferralCodeAgent(@Param("referralCode") String referralCode);

}
