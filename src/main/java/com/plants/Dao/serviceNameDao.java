package com.plants.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.plants.entities.serviceName;

public interface serviceNameDao extends JpaRepository<serviceName, Integer>{
	
	@Query("select e FROM serviceName e" )
	//@Query("SELECT e FROM serviceName e JOIN e.plans p WHERE e.isActive = 'true' AND p.isActive = 'true'")

	public List<serviceName> getallPlans();
	
	

}
