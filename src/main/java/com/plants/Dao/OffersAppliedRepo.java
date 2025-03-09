package com.plants.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.OffersApplied;

public interface OffersAppliedRepo extends JpaRepository<OffersApplied,Integer>{
	
	@Query("SELECT e FROM OffersApplied e Where e.offers.primaryKey = :offerPk AND e.customerMain.primarykey = :cusstPk ")
	List<OffersApplied> getAppliedlistOffers(@Param("offerPk") int offerPk,@Param("custPk") int custPk);
	
	@Query("SELECT COUNT(e) FROM OffersApplied e WHERE e.offers.primaryKey = :offerPk AND e.customerMain.primarykey = :custPk AND e.offerStatus = 'USED'")
	long countAppliedOffers(@Param("offerPk") int offerPk, @Param("custPk") int custPk);


}
