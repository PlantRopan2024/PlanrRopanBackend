package com.plants.Dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.plants.entities.Offers;
@Repository
public interface OfferDao extends JpaRepository<Offers, Integer> {
	
	@Query("select e FROM Offers e where e.isNewActive = true")
	public ArrayList<Offers> getAllActiveOffer();

}
