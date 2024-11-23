package com.plants.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Plans {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primaryKey;
	private String plansRs;
	private String timeDuration;
	private String UptoPots;
	private String servicesName;
	private String planType;
	private String planPacks;
	private String isActive;
	public Plans() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Plans(int primaryKey, String plansRs, String timeDuration,String planPacks, String uptoPots, String servicesName,
			String planType, String isActive) {
		super();
		this.primaryKey = primaryKey;
		this.plansRs = plansRs;
		this.timeDuration = timeDuration;
		UptoPots = uptoPots;
		this.servicesName = servicesName;
		this.planType = planType;
		this.planPacks =planPacks;
		this.isActive = isActive;
	}
	public int getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
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
	public String getServicesName() {
		return servicesName;
	}
	public void setServicesName(String servicesName) {
		this.servicesName = servicesName;
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
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "Plans [primaryKey=" + primaryKey + ", plansRs=" + plansRs + ", timeDuration=" + timeDuration
				+ ", UptoPots=" + UptoPots + ", servicesName=" + servicesName + ", planType=" + planType
				+ ", planPacks=" + planPacks + ", isActive=" + isActive + "]";
	}
	
	
}
