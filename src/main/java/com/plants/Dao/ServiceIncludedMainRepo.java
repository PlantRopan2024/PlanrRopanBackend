package com.plants.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.ServiceIncludedMain;

public interface ServiceIncludedMainRepo extends JpaRepository<ServiceIncludedMain, Integer>{
	
	
	@Query("select e FROM ServiceIncludedMain e ")
	public List<ServiceIncludedMain> getallViewIncludingService();

	@Query("select e FROM ServiceIncludedMain e Where e.isActive = true ")
	public List<ServiceIncludedMain> getactiveIncludingServiceMain();
	
	@Query("select e FROM ServiceIncludedMain e Where e.primaryKey = :primaryKey ")
	public ServiceIncludedMain getPkIncludingServiceMain(@Param("primaryKey") String primaryKey);
	
	@Query("select e FROM ServiceIncludedMain e Where e.primaryKey = :primaryKey ")
	public ServiceIncludedMain getPkIncludingServiceMain(@Param("primaryKey") int primaryKey);
	
}
