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
	public List<CustomerMain>findMobileNumberList(@Param("mobileNumber")String mobileNumber);
	
	@Query("select e FROM CustomerMain e where e.mobileNumber=:mobileNumber")
	public CustomerMain findMobileNumber(@Param("mobileNumber")String mobileNumber);
	
	@Query("select e FROM CustomerMain e where e.primarykey=:primarykey")
	public CustomerMain findbyPrimaryKey(@Param("primarykey")int primarykey);
	
	@Query("select e FROM Plans e where e.planPacks=:planPacks")
	public ArrayList<Plans> getMonthVSDailyfetch(@Param("planPacks")String planPacks);
	
	@Query("SELECT e FROM CustomerMain e WHERE LOWER(e.cusReferralCode) = LOWER(:cusReferralCode)")
	CustomerMain getReferralCodeCus(@Param("cusReferralCode") String cusReferralCode);
	
	@Query("select e FROM Plans e")
	public List<Plans> getallPlans();
	
	@Query("select e FROM Plans e WHERE e.isActive=true and e.servicesName.primaryKey = :id")
	public List<Plans> getPlansListId(@Param("id") int id);
	
	@Query("select e FROM Plans e WHERE e.primaryKey = :id")
	public Plans getPlansId(@Param("id") String id);
	
	@Query("select e FROM Plans e WHERE e.primaryKey = :id")
	public Plans getPlansByPk(@Param("id") int id);
	

}
