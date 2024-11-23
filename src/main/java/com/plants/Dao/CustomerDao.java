package com.plants.Dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.plants.entities.AgentMain;
import com.plants.entities.CustomerMain;
import com.plants.entities.Plans;

import java.util.ArrayList;
import java.util.List;


public interface CustomerDao extends CrudRepository<CustomerMain, Integer> {
	
	
	@Query("select e FROM CustomerMain e WHERE e.emailId = :emailId OR e.mobileNumber = :mobileNumber")
	public CustomerMain findEmailAndMobileCus(@Param("emailId") String emailId , @Param("mobileNumber") String mobileNumber);
	
	@Query("select e FROM CustomerMain e where e.mobileNumber=:mobileNumber")
	public List<CustomerMain>findMobileNumber(@Param("mobileNumber")String mobileNumber);
	
	@Query("select e FROM Plans e where e.planPacks=:planPacks")
	public ArrayList<Plans> getMonthVSDailyfetch(@Param("planPacks")String planPacks);
	
	
	@Query("select e FROM Plans e")
	public List<Plans> getallPlans();
	

}
