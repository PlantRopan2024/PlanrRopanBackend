package com.plants.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CusRequestWrapper {
	
	@JsonProperty("cusPersonalDetail")
	private List<CusPersonalDetail> cusPersonalDetail;
	
	@JsonProperty("plans")
    private List<CusPlanDetails> plans;
	  
	  
	public List<CusPersonalDetail> getCusPersonalDetail() {
		return cusPersonalDetail;
	}
	public void setCusPersonalDetail(List<CusPersonalDetail> cusPersonalDetail) {
		this.cusPersonalDetail = cusPersonalDetail;
	}
	public List<CusPlanDetails> getPlans() {
		return plans;
	}
	public void setPlans(List<CusPlanDetails> plans) {
		this.plans = plans;
	}
    
    
    
}
