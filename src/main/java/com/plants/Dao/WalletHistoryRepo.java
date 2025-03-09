package com.plants.Dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.plants.entities.AgentMain;
import com.plants.entities.CustomerMain;
import com.plants.entities.WalletHistory;

public interface WalletHistoryRepo extends JpaRepository<WalletHistory, Integer>{

	Page<WalletHistory> findByAgentMain(AgentMain agentMain, Pageable pageable);
	
	Page<WalletHistory> findByCustomerMain(CustomerMain customerMain, Pageable pageable);
	
}
