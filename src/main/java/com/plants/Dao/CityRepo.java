package com.plants.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.City;

public interface CityRepo extends JpaRepository<City, Integer>{
	
	@Query("SELECT c FROM City c")
	List<City> getAllCity();
	
	@Query("SELECT c FROM City c where c.cityName = :cityName")
	City getCityName(@Param("cityName") String cityName);
	
	@Query("SELECT c FROM City c where c.primaryKey = :primaryKey")
	City getCityPk(@Param("primaryKey") String primaryKey);
	
	@Query("SELECT c FROM City c where c.state.primaryKey = :primaryKey and c.isActive=true")
	List<City> getCityStatePk(@Param("primaryKey") int primaryKey);
}
