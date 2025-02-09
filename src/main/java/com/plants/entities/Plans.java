package com.plants.entities;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Plans {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primaryKey;
	private String plansName;
	private String plansRs;
	private String timeDuration;
	private String UptoPots;
	private String includingServicesName;
	private String planType;
	private String planPacks;
	private boolean isActive;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "FK_SERVICE_NAME_ID")
	private serviceName serviceName;
		
	@OneToMany(mappedBy = "plans")
    private List<Fertilizer> fertilizers = new ArrayList<>();
	    
	public Plans() {
		super();
	}

	public Plans(int primaryKey, String plansName, String plansRs, String timeDuration, String uptoPots,
			String includingServicesName, String planType, String planPacks, boolean isActive,
			com.plants.entities.serviceName serviceName, List<Fertilizer> fertilizers) {
		super();
		this.primaryKey = primaryKey;
		this.plansName = plansName;
		this.plansRs = plansRs;
		this.timeDuration = timeDuration;
		UptoPots = uptoPots;
		this.includingServicesName = includingServicesName;
		this.planType = planType;
		this.planPacks = planPacks;
		this.isActive = isActive;
		this.serviceName = serviceName;
		this.fertilizers = fertilizers;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getPlansName() {
		return plansName;
	}

	public void setPlansName(String plansName) {
		this.plansName = plansName;
	}

	public String getPlansRs() {
		return plansRs;
	}

	public void setPlansRs(String plansRs) {
		this.plansRs = plansRs;
	}

	public String getTimeDuration() {
		return timeDuration;
	}

	public void setTimeDuration(String timeDuration) {
		this.timeDuration = timeDuration;
	}

	public String getUptoPots() {
		return UptoPots;
	}

	public void setUptoPots(String uptoPots) {
		UptoPots = uptoPots;
	}

	public String getIncludingServicesName() {
		return includingServicesName;
	}

	public void setIncludingServicesName(String includingServicesName) {
		this.includingServicesName = includingServicesName;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getPlanPacks() {
		return planPacks;
	}

	public void setPlanPacks(String planPacks) {
		this.planPacks = planPacks;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public serviceName getServiceName() {
		return serviceName;
	}

	public void setServiceName(serviceName serviceName) {
		this.serviceName = serviceName;
	}

	public List<Fertilizer> getFettilizer() {
		return fertilizers;
	}

	public void setFettilizer(List<Fertilizer> fertilizers) {
		this.fertilizers = fertilizers;
	}

	@Override
	public String toString() {
		return "Plans [primaryKey=" + primaryKey + ", plansName=" + plansName + ", plansRs=" + plansRs
				+ ", timeDuration=" + timeDuration + ", UptoPots=" + UptoPots + ", includingServicesName="
				+ includingServicesName + ", planType=" + planType + ", planPacks=" + planPacks + ", isActive="
				+ isActive + ", serviceName=" + serviceName + ", fettilizer=" + fertilizers + "]";
	}

}
