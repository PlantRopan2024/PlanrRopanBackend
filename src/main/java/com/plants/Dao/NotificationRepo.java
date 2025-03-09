package com.plants.Dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.plants.entities.AgentMain;
import com.plants.entities.CustomerMain;

public interface NotificationRepo extends JpaRepository<com.plants.entities.Notification, Integer>{
	
	
    Page<com.plants.entities.Notification> findByAgentMainAndTypeIId(AgentMain agentMain, String typeIId, Pageable pageable);
    
    Page<com.plants.entities.Notification> findByCustomerMainAndTypeIId(CustomerMain customerMain, String typeIId, Pageable pageable);

}
