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
    
    private String serviceImage;
	@Column(columnDefinition = "TEXT")
	private String serviceImagePath;
	private String serviceImage_type;   

    @OneToMany(mappedBy = "servicesName")
    private List<Plans> plans = new ArrayList<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false, updatable = true)
	private LocalDateTime updatedAt;

	public serviceName() {
		super();
	}

	public serviceName(int primaryKey, boolean isActive, String name, String serviceImage, String serviceImagePath,
			String serviceImage_type, List<Plans> plans, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.primaryKey = primaryKey;
		this.isActive = isActive;
		this.name = name;
		this.serviceImage = serviceImage;
		this.serviceImagePath = serviceImagePath;
		this.serviceImage_type = serviceImage_type;
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

	public String getServiceImage() {
		return serviceImage;
	}

	public void setServiceImage(String serviceImage) {
		this.serviceImage = serviceImage;
	}

	public String getServiceImagePath() {
		return serviceImagePath;
	}

	public void setServiceImagePath(String serviceImagePath) {
		this.serviceImagePath = serviceImagePath;
	}

	public String getServiceImage_type() {
		return serviceImage_type;
	}

	public void setServiceImage_type(String serviceImage_type) {
		this.serviceImage_type = serviceImage_type;
	}
}
