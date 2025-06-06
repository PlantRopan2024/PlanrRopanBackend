package com.plants.Dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.plants.entities.AgentMain;
import com.plants.entities.Plans;
import com.plants.entities.user;

import jakarta.transaction.Transactional;


public interface userDao extends CrudRepository<user, Integer>{
	
	@Query("select e FROM user e WHERE e.username = :username AND e.password = :password AND e.isActive = true")
	public user finduserNameAndPassword(@Param("username") String username , @Param("password") String password);
	
	@Query("select e FROM user e")
	public List<user> getUserData();
	
	@Query("SELECT u from user u Where u.primarykey = :userPk")
	public user getUserPk(@Param("userPk") String userPk);
	
	public AgentMain save(AgentMain agentMain);
	
	public Plans save(Plans plans);
	
	@Query("select e FROM AgentMain e WHERE e.emailId = :emailId OR e.mobileNumber = :mobileNumber")
	public AgentMain findEmailAndMobileAg(@Param("emailId") String emailId , @Param("mobileNumber") String mobileNumber);
	
	@Transactional
	@Query("select e FROM AgentMain e WHERE e.AgentIDPk = :AgentIDPk ")
	public AgentMain findAgentID(@Param("AgentIDPk") String AgentIDPk);

	@Transactional
	@Query("select e FROM AgentMain e WHERE e.AgentIDPk = :AgentIDPk ")
	public AgentMain findAgentID(@Param("AgentIDPk") int AgentIDPk);
	
	@Modifying
	@Transactional
    @Query("UPDATE AgentMain a SET a.selfieImg = :selfieImg WHERE a.AgentIDPk = :AgentIDPk")
    public void updateSelfiImage(@Param("selfieImg") String selfieImg , @Param("AgentIDPk") String AgentIDPk);
	
	
	@Modifying
	@Transactional
    @Query("UPDATE AgentMain a SET a.state = :state ,a.city = :city, a.address = :address, a.pincode = :pincode, a.latitude = :latitude,a.longitude = :longitude  WHERE a.AgentIDPk = :AgentIDPk")
    public void updateAddress(@Param("state") String state ,@Param("city") String city ,@Param("address") String address ,@Param("pincode") String pincode ,@Param("latitude") String latitude ,@Param("longitude") String longitude , @Param("AgentIDPk") String AgentIDPk);
	
	
	
	@Modifying
	@Transactional
    @Query("UPDATE AgentMain a SET  a.latitude = :latitude,a.longitude = :longitude  WHERE a.AgentIDPk = :AgentIDPk")
    public void updateliveLocation(@Param("latitude") String latitude ,@Param("longitude") String longitude , @Param("AgentIDPk") String AgentIDPk);
	
	
	@Modifying
	@Transactional
    @Query("UPDATE AgentMain a SET  a.isActiveAgent = :isActiveAgent  WHERE a.AgentIDPk = :AgentIDPk")
    public void UpdateagentActive(@Param("isActiveAgent") boolean isActiveAgent , @Param("AgentIDPk") int AgentIDPk);
	
	
	@Modifying
	@Transactional
    @Query("UPDATE AgentMain a SET a.aadharImgFrontSide = :aadharImgFrontSide , a.aadhaarNumber =:aadhaarNumber WHERE a.AgentIDPk = :AgentIDPk")
    public void updateaddharImage(@Param("aadharImgFrontSide") String aadharImgFrontSide,@Param("aadhaarNumber") String aadhaarNumber , @Param("AgentIDPk") String AgentIDPk);
	
	
	@Modifying
	@Transactional
    @Query("UPDATE AgentMain a SET a.accHolderName = :accHolderName ,a.accNumber = :accNumber, a.bankName = :bankName, a.ifscCode = :ifscCode  WHERE a.AgentIDPk = :AgentIDPk")
    public void updateBankAccount(@Param("accHolderName") String accHolderName ,@Param("accNumber") String accNumber ,@Param("bankName") String bankName ,@Param("ifscCode") String ifscCode , @Param("AgentIDPk") String AgentIDPk);
	
	@Modifying
	@Transactional
    @Query("UPDATE AgentMain a SET a.bankPassBookImage = :bankPassBookImage  WHERE a.AgentIDPk = :AgentIDPk")
    public void updateBankPassBookImg(@Param("bankPassBookImage") String bankPassBookImage, @Param("AgentIDPk") String AgentIDPk);
	
	@Transactional
	@Query("select e FROM AgentMain e where e.agentApproved = false")
	public ArrayList<AgentMain> getpendingVerif();
	
	@Transactional
	@Query("select e FROM AgentMain e where e.agentApproved = true")
	public ArrayList<AgentMain> getApprovedAgent();
	
	@Query("select e FROM AgentMain e where e.isActiveAgent = true")
	public ArrayList<AgentMain> activeAgent();
	
	@Modifying
	@Transactional
    @Query("UPDATE AgentMain a SET  a.agentApproved = :agentApproved  WHERE a.AgentIDPk = :AgentIDPk")
    public void updateAgentApproved(@Param("agentApproved") boolean agentApproved , @Param("AgentIDPk") int AgentIDPk);
	
	@Query("SELECT MAX(CAST(SUBSTRING(a.agentId, 16) AS int)) FROM AgentMain a ")
	Integer getMaxSequenceAgentId();
	
}
