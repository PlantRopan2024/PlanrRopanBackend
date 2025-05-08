package com.plants.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "sub_role")
public class SubRole {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primarykey;
	
	private String subRoleName;
	
	private boolean isActive;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_role_id" ,nullable = true)
	private Role role;
	
	@OneToMany(mappedBy = "subRole")
    private List<user> userList = new ArrayList<>();
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	    
	@Column(name = "updated_at", nullable = false, updatable = true)
	private LocalDateTime updatedAt;

	public SubRole() {
		super();
	}

	public SubRole(int primarykey, String subRoleName, boolean isActive, Role role, List<user> userList,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.primarykey = primarykey;
		this.subRoleName = subRoleName;
		this.isActive = isActive;
		this.role = role;
		this.userList = userList;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(int primarykey) {
		this.primarykey = primarykey;
	}

	public String getSubRoleName() {
		return subRoleName;
	}

	public void setSubRoleName(String subRoleName) {
		this.subRoleName = subRoleName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<user> getUserList() {
		return userList;
	}

	public void setUserList(List<user> userList) {
		this.userList = userList;
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
