package com.plants.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.Role;

public interface RoleRepo extends JpaRepository<Role, Integer>{
	
	@Query("SELECT r FROM Role r Where r.roleName = :roleName")
	Role getRoleName(@Param("roleName") String roleName);
	
	@Query("SELECT r FROM Role r Where r.primarykey = :rolePk")
	Role getRolePk(@Param("rolePk") String rolePk);
	
	@Query("SELECT r FROM Role r Where r.primarykey = :rolePk")
	Role getRolePk(@Param("rolePk") int rolePk);
	
	@Query("SELECT r FROM Role r")
	List<Role> getAllRole();
	
	@Query("SELECT r FROM Role r Where r.isActive = true")
	List<Role> getActiveRole();
}
