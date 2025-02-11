package com.plants.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plants.entities.Fertilizer;

public interface FertilizerRepo extends JpaRepository<Fertilizer, Integer>{

}
