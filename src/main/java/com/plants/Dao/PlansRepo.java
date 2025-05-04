package com.plants.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.Plans;

public interface PlansRepo extends JpaRepository<Plans, Integer>{
	
	@Query("select e FROM Plans e WHERE e.primaryKey = :id")
	public Plans getPlansId(@Param("id") String id);
	
	@Query("select e FROM Plans e WHERE e.primaryKey = :id")
	public Plans getPlansByPk(@Param("id") int id);
}
