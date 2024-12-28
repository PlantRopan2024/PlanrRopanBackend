package com.plants.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plants.entities.ImageData;

public interface StorageRepository extends JpaRepository<ImageData,Long> {


    Optional<ImageData> findByName(String fileName);
}