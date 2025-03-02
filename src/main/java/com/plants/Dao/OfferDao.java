package com.plants.Dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.plants.entities.Offers;
@Repository
public interface OfferDao extends JpaRepository<Offers, Integer> {
	
	@Query("select e FROM Offers e where e.isNewActive = true and e.TypeID = 'Agent'")
	public List<Offers> getAllActiveOfferAgentMobApi();
	
	@Query("select e FROM Offers e where e.isNewActive = true and e.TypeID = 'Customer'")
	public List<Offers> getAllActiveOfferCusMobApi();
	
	@Query("select e FROM Offers e where e.TypeID = 'Agent'")
	public List<Offers> getAllActiveOfferAgent();
	
	@Query("select e FROM Offers e where e.TypeID = 'Customer'")
	public List<Offers> getAllActiveOfferCus();
	
	
	@Query("select e FROM Offers e where e.isNewActive = true")
	public List<Offers> getListActiveOffer();
	
	@Query("SELECT e FROM Offers e where e.primaryKey = :pk")
	public Offers findById(@Param("pk") String pk);

}
