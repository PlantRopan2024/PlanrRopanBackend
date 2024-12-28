package com.plants.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ImageUpload {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primaryKey;
	
	private String fileName;
	
	private boolean isActive;
	
	public ImageUpload(int primaryKey, String fileName, boolean isActive) {
		super();
		this.primaryKey = primaryKey;
		this.fileName = fileName;
		this.isActive = isActive;
	}

	public ImageUpload() {
		// TODO Auto-generated constructor stub
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "ImageUpload [primaryKey=" + primaryKey + ", fileName=" + fileName + ", isActive=" + isActive + "]";
	}
	
	
}
