package com.plants.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FeedBackPlantRopan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primaryKey;
	
	private String name;
	private String email;
	private String phone;
	
	@Column(columnDefinition = "TEXT")
	private String message;

	public FeedBackPlantRopan() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FeedBackPlantRopan(int primaryKey, String name, String email, String phone, String message) {
		super();
		this.primaryKey = primaryKey;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.message = message;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
