package com.plants.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.State;

public interface StateRepo extends JpaRepository<State, Integer>{
	
	@Query("SELECT s FROM State s")
	List<State> getAllState();
	
	@Query("SELECT s FROM State s Where s.isActive=true")
	List<State> getAllStateActive();
	
	@Query("SELECT s FROM State s where s.stateName = :stateName")
	State getStateName(@Param("stateName") String StateName);
	
	@Query("SELECT s FROM State s where s.primaryKey = :primaryKey")
	State getStatePk(@Param("primaryKey") String primaryKey);
	
	@Query("SELECT s FROM State s where s.primaryKey = :primaryKey")
	State getStatePk(@Param("primaryKey") int primaryKey);
}
