package com.plants.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.LoginHours;

public interface LoginHoursRepo extends JpaRepository<LoginHours, Integer>{
	
	@Query("SELECT lh FROM LoginHours lh WHERE lh.agentMain.AgentIDPk = :agentID AND lh.logoutTime IS NULL ORDER BY lh.loginTime DESC LIMIT 1")
	LoginHours getLatestActiveLogin(@Param("agentID") int agentID);
	
	
	@Query("SELECT lh FROM LoginHours lh WHERE lh.agentMain.AgentIDPk = :agentID AND FUNCTION('DATE', lh.createdAt) = :date AND lh.logoutTime IS NOT NULL")
	List<LoginHours> getCountActiveLogin(@Param("agentID") int agentID, @Param("date") LocalDate date);

}
