package com.plants.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "service_included_main")
public class ServiceIncludedMain {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int primaryKey;
	private boolean isActive;
	private String headerName;
	private String nameDetails;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	    
	@Column(name = "updated_at", nullable = false, updatable = true)
	private LocalDateTime updatedAt;
	
	public ServiceIncludedMain() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServiceIncludedMain(int primaryKey, boolean isActive, String headerName, String nameDetails,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.primaryKey = primaryKey;
		this.isActive = isActive;
		this.headerName = headerName;
		this.nameDetails = nameDetails;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public String getNameDetails() {
		return nameDetails;
	}

	public void setNameDetails(String nameDetails) {
		this.nameDetails = nameDetails;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	
	
}
