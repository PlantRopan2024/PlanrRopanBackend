package com.plants.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.HourlyRollupOrder;

public interface HourlyRollupOrderRepo extends JpaRepository<HourlyRollupOrder, Integer>{

	
	@Query("SELECT o FROM HourlyRollupOrder o WHERE o.rollupAt = :rollupAt")
	List<HourlyRollupOrder> getHourlyRollup(@Param("rollupAt") LocalDate rollupAt);
	
}
