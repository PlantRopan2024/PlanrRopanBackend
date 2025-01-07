package com.plants.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plants.entities.ImageData;

import jakarta.transaction.Transactional;

public interface StorageRepository extends JpaRepository<ImageData,Long> {

	@Transactional
    Optional<ImageData> findByName(String fileName);
}