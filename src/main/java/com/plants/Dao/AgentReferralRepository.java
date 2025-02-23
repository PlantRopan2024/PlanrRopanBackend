package com.plants.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.AgentReferral;

public interface AgentReferralRepository extends JpaRepository<AgentReferral, Integer>{
	
	@Query("SELECT e FROM AgentReferral e WHERE e.referralCode = :referralCode and e.referredAgentId = :referredAgentId")
	public AgentReferral getUseReferralCode(@Param("referralCode") String referralCode , @Param("referredAgentId") int referredAgentId);
}
