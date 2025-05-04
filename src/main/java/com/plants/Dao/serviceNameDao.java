package com.plants.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.serviceName;

public interface serviceNameDao extends JpaRepository<serviceName, Integer>{
	
	@Query("select e FROM serviceName e" )
	public List<serviceName> getallPlans();
	
	@Query("select e FROM serviceName e WHERE e.isActive = true")
	public List<serviceName> getallService();
	
	@Query("select e FROM serviceName e WHERE e.primaryKey = :id")
	public serviceName getServiceId(@Param("id") int id);
	
	@Query("select e FROM serviceName e WHERE e.primaryKey = :id")
	public serviceName getServicePk(@Param("id") String id);
	
	@Query("select e FROM serviceName e ")
	public List<serviceName> getallViewService();
}
