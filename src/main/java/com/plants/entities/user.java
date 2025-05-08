package com.plants.entities;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "usr" , uniqueConstraints = {@UniqueConstraint(columnNames = {"username" , "email"})})
public class user {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primarykey;
	
	@Column(unique = true)
	private String username;
	private String employeename;
	private String mobileNumber;
	private boolean isActive;
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private LocalDateTime createdAt;
	private LocalDateTime modifiedOn;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_sub_role_id" ,nullable = true)
	private SubRole subRole;
	public user() {
		super();
	}

	public user(int primarykey, String username, String employeename, boolean isActive, String email, String password,
		 LocalDateTime createdAt, LocalDateTime modifiedOn, SubRole subRole,String mobileNumber) {
		super();
		this.primarykey = primarykey;
		this.username = username;
		this.employeename = employeename;
		this.isActive = isActive;
		this.email = email;
		this.password = password;
		this.createdAt = createdAt;
		this.modifiedOn = modifiedOn;
		this.subRole = subRole;
		this.mobileNumber=mobileNumber;
	}

	public int getPrimarykey() {
		return primarykey;
	}
	public void setPrimarykey(int primarykey) {
		this.primarykey = primarykey;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmployeename() {
		return employeename;
	}
	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public SubRole getSubRole() {
		return subRole;
	}

	public void setSubRole(SubRole subRole) {
		this.subRole = subRole;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	@Override
	public String toString() {
		return "user [primarykey=" + primarykey + ", username=" + username + ", employeename=" + employeename
				+ ", email=" + email + ", password=" + password + ", createdAt=" + createdAt
				+ ", modifiedOn=" + modifiedOn + "]";
	}
	
	
	
}
