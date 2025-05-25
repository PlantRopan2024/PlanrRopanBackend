package com.plants.entities;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlansDto {

	private String plansName;
    private String planPacks;
    private String plansRs;
    private String timeDuration;
    
    @JsonProperty("UptoPots") 
    private String uptoPots;
    private String planType;
	private String servicesName;
	private String serviceNamePk;
    @JsonProperty("planKey") 
	private String planKey;
    private List<IncludingServiceDto> includingServicesList; // Including services
    private MultipartFile imageFile; // Image file for the plan

    // Getters and Setters
    public String getPlansName() {
		return plansName;
	}

	public String getPlanKey() {
		return planKey;
	}

	public void setPlanKey(String planKey) {
		this.planKey = planKey;
	}

	public void setPlansName(String plansName) {
		this.plansName = plansName;
	}

	public String getPlanPacks() {
		return planPacks;
	}

	public void setPlanPacks(String planPacks) {
		this.planPacks = planPacks;
	}

	public String getPlansRs() {
		return plansRs;
	}

	public void setPlansRs(String plansRs) {
		this.plansRs = plansRs;
	}

	public String getUptoPots() {
		return uptoPots;
	}

	public void setUptoPots(String uptoPots) {
		this.uptoPots = uptoPots;
	}

	public String getTimeDuration() {
		return timeDuration;
	}

	public void setTimeDuration(String timeDuration) {
		this.timeDuration = timeDuration;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getServicesName() {
		return servicesName;
	}

	public void setServicesName(String servicesName) {
		this.servicesName = servicesName;
	}

	public String getServiceNamePk() {
		return serviceNamePk;
	}

	public void setServiceNamePk(String serviceNamePk) {
		this.serviceNamePk = serviceNamePk;
	}

	public List<IncludingServiceDto> getIncludingServicesList() {
		return includingServicesList;
	}

	public void setIncludingServicesList(List<IncludingServiceDto> includingServicesList) {
		this.includingServicesList = includingServicesList;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
	
	

    @Override
	public String toString() {
		return "PlansDto [plansName=" + plansName + ", planPacks=" + planPacks + ", plansRs=" + plansRs
				+ ", timeDuration=" + timeDuration + ", uptoPots=" + uptoPots + ", planType=" + planType
				+ ", servicesName=" + servicesName + ", serviceNamePk=" + serviceNamePk + ", planKey=" + planKey
				+ ", includingServicesList=" + includingServicesList + ", imageFile=" + imageFile + "]";
	}



	public static class IncludingServiceDto {
        public String getHeaderName() {
			return headerName;
		}
		public void setHeaderName(String headerName) {
			this.headerName = headerName;
		}
		public Integer getPrimaryKey() {
			return primaryKey;
		}
		public void setPrimaryKey(Integer primaryKey) {
			this.primaryKey = primaryKey;
		}
		private String headerName;
        private Integer primaryKey;
		@Override
		public String toString() {
			return "IncludingServiceDto [headerName=" + headerName + ", primaryKey=" + primaryKey + "]";
		}

        // Getters and Setters
    }
}
