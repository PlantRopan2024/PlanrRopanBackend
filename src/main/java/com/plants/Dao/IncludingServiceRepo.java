package com.plants.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.IncludingService;

public interface IncludingServiceRepo extends JpaRepository<IncludingService, Integer>{
	
	@Query("SELECT e FROM IncludingService e Where e.plans.primaryKey = :planPk")
	List<IncludingService> getDataIncluding(@Param("planPk") String planPk);
}
