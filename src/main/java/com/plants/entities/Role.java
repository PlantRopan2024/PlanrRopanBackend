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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "role")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primarykey;
	
	private String roleName;
	
	private boolean isActive;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	    
	@Column(name = "updated_at", nullable = false, updatable = true)
	private LocalDateTime updatedAt;
	
	@OneToMany(mappedBy = "role")
    private List<SubRole> subRole = new ArrayList<>();

	public Role() {
		super();
	}

	public Role(int primarykey, String roleName, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt,
			List<SubRole> subRole) {
		super();
		this.primarykey = primarykey;
		this.roleName = roleName;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.subRole = subRole;
	}
	
	public int getPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(int primarykey) {
		this.primarykey = primarykey;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	public List<SubRole> getSubRole() {
		return subRole;
	}

	public void setSubRole(List<SubRole> subRole) {
		this.subRole = subRole;
	}
	
}
