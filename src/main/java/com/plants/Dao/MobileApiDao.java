package com.plants.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.plants.entities.AgentMain;

public interface MobileApiDao extends CrudRepository<AgentMain, Integer>{
	

	@Query("select e FROM AgentMain e WHERE e.mobileNumber = :mobileNumber")
	public AgentMain findMobileNumber(@Param("mobileNumber") String mobileNumber );
	
	
	@Query("select e FROM AgentMain e WHERE e.mobileNumber = :mobileNumber")
	public List<AgentMain> findMobileNumberValidateToken(@Param("mobileNumber") String mobileNumber );
}
