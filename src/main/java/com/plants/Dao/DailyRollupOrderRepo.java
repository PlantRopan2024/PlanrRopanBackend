package com.plants.Dao;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.DailyRollupOrder;

public interface DailyRollupOrderRepo extends JpaRepository<DailyRollupOrder, Integer>{
	
	@Query("SELECT o FROM DailyRollupOrder o WHERE o.rollupAt >= :rollupAt")
	DailyRollupOrder rollupDate(@Param("rollupAt") LocalDateTime rollupAt);
	
	
}
