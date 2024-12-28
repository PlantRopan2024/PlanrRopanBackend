package com.plants.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plants.entities.ImageUpload;

public interface ImageUploadRepo extends JpaRepository<ImageUpload, Integer>{

}
