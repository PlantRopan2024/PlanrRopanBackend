package com.plants.entities;

import java.util.List;

public class BookingRequest {
	
	private String planId;
	private List<FertilizerRequest> fertilizers;
	public BookingRequest(String planId, List<FertilizerRequest> fertilizers) {
		super();
		this.planId = planId;
		this.fertilizers = fertilizers;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public List<FertilizerRequest> getFertilizers() {
		return fertilizers;
	}
	public void setFertilizers(List<FertilizerRequest> fertilizers) {
		this.fertilizers = fertilizers;
	}
	@Override
	public String toString() {
		return "BookingRequest [planId=" + planId + ", fertilizers=" + fertilizers + "]";
	}
}
