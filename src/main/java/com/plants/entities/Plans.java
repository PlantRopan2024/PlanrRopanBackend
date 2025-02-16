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
	private String workDone;
	private String varietiesTrimmed;
	private boolean isActive;
	private String reviewsCount;
	private String ratingStar;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "FK_SERVICE_NAME_ID")
	private serviceName servicesName;
		
	@OneToMany(mappedBy = "plans")
    private List<Fertilizer> fertilizers = new ArrayList<>();
	
	@OneToMany(mappedBy = "plans")
    private List<Reviews> reviews = new ArrayList<>();
	    
	public Plans() {
		super();
	}

	public Plans(int primaryKey, String plansName, String plansRs, String timeDuration, String uptoPots,
			String includingServicesName, String planType, String planPacks, String workDone, String varietiesTrimmed,
			boolean isActive, serviceName servicesName,String reviewsCount,String ratingStar, List<Fertilizer> fertilizers, List<Reviews> reviews) {
		super();
		this.primaryKey = primaryKey;
		this.plansName = plansName;
		this.plansRs = plansRs;
		this.timeDuration = timeDuration;
		UptoPots = uptoPots;
		this.includingServicesName = includingServicesName;
		this.planType = planType;
		this.planPacks = planPacks;
		this.workDone = workDone;
		this.varietiesTrimmed = varietiesTrimmed;
		this.isActive = isActive;
		this.servicesName = servicesName;
		this.fertilizers = fertilizers;
		this.reviews = reviews;
		this.reviewsCount=reviewsCount;
		this.ratingStar=ratingStar;
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

	public String getWorkDone() {
		return workDone;
	}

	public void setWorkDone(String workDone) {
		this.workDone = workDone;
	}

	public String getVarietiesTrimmed() {
		return varietiesTrimmed;
	}

	public void setVarietiesTrimmed(String varietiesTrimmed) {
		this.varietiesTrimmed = varietiesTrimmed;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public serviceName getServicesName() {
		return servicesName;
	}

	public void setServicesName(serviceName servicesName) {
		this.servicesName = servicesName;
	}

	public List<Fertilizer> getFertilizers() {
		return fertilizers;
	}

	public void setFertilizers(List<Fertilizer> fertilizers) {
		this.fertilizers = fertilizers;
	}

	public List<Reviews> getReviews() {
		return reviews;
	}

	public void setReviews(List<Reviews> reviews) {
		this.reviews = reviews;
	}
	

	public String getReviewsCount() {
		return reviewsCount;
	}

	public void setReviewsCount(String reviewsCount) {
		this.reviewsCount = reviewsCount;
	}

	public String getRatingStar() {
		return ratingStar;
	}

	public void setRatingStar(String ratingStar) {
		this.ratingStar = ratingStar;
	}

	@Override
	public String toString() {
		return "Plans [primaryKey=" + primaryKey + ", plansName=" + plansName + ", plansRs=" + plansRs
				+ ", timeDuration=" + timeDuration + ", UptoPots=" + UptoPots + ", includingServicesName="
				+ includingServicesName + ", planType=" + planType + ", planPacks=" + planPacks + ", workDone="
				+ workDone + ", varietiesTrimmed=" + varietiesTrimmed + ", isActive=" + isActive + ", reviewsCount="
				+ reviewsCount + ", ratingStar=" + ratingStar + ", servicesName=" + servicesName + ", fertilizers="
				+ fertilizers + ", reviews=" + reviews + "]";
	}
}
