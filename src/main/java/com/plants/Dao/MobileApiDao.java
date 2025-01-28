package com.plants.Dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.plants.entities.AgentMain;

public interface MobileApiDao extends JpaRepository<AgentMain, Integer>{
	
	@Transactional
	@Query("select e FROM AgentMain e WHERE e.mobileNumber = :mobileNumber")
	public AgentMain findMobileNumber(@Param("mobileNumber") String mobileNumber );
	
	@Transactional
	@Query("select e FROM AgentMain e WHERE e.mobileNumber = :mobileNumber")
	public AgentMain findMobileNumberValidateToken(@Param("mobileNumber") String mobileNumber );
}
