package com.plants.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "SERVICE_NAME")
public class serviceName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int primaryKey;

    private boolean isActive;

    private String name;

    @OneToMany(mappedBy = "servicesName")
    private List<Plans> plans = new ArrayList<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false, updatable = true)
	private LocalDateTime updatedAt;

	public serviceName() {
		super();
	}

	public serviceName(int primaryKey, boolean isActive, String name, List<Plans> plans, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super();
		this.primaryKey = primaryKey;
		this.isActive = isActive;
		this.name = name;
		this.plans = plans;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Plans> getPlans() {
		return plans;
	}

	public void setPlans(List<Plans> plans) {
		this.plans = plans;
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
