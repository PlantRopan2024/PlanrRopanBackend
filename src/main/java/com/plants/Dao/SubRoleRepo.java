package com.plants.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.SubRole;

public interface SubRoleRepo extends JpaRepository<SubRole, Integer>{
	
	
	@Query("SELECT r FROM SubRole r Where r.subRoleName = :subRoleName")
	SubRole getSubRoleName(@Param("subRoleName") String subRoleName);
	
	@Query("SELECT r FROM SubRole r Where r.primarykey = :subRolepK")
	SubRole getSubRolePk(@Param("subRolepK") int subRolepK);
	
	@Query("SELECT r FROM SubRole r Where r.primarykey = :subRolepK")
	SubRole getSubRolePk(@Param("subRolepK") String subRolepK);
}
