package com.plants.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "including_Service")
public class IncludingService {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int primaryKey;
	
	private String headerName;
	private String nameDetails;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_plans_id")
	private Plans plans;

	public IncludingService() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IncludingService(int primaryKey, String headerName, String nameDetails, Plans plans) {
		super();
		this.primaryKey = primaryKey;
		this.headerName = headerName;
		this.nameDetails = nameDetails;
		this.plans = plans;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
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

	public Plans getPlans() {
		return plans;
	}

	public void setPlans(Plans plans) {
		this.plans = plans;
	}
	
	
}
