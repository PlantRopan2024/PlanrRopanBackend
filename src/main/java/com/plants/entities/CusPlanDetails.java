package com.plants.entities;

public class CusPlanDetails {
	
	    private int primaryKey;
	    private String plansRs;
	    private String timeDuration;
	    private String servicesName;
	    private String planType;
	    private String planPacks;
	    private String isActive;
	    private String uptoPots;
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
		public String getUptoPots() {
			return uptoPots;
		}
		public void setUptoPots(String uptoPots) {
			this.uptoPots = uptoPots;
		}
		@Override
		public String toString() {
			return "CusPlanDetails [primaryKey=" + primaryKey + ", plansRs=" + plansRs + ", timeDuration="
					+ timeDuration + ", servicesName=" + servicesName + ", planType=" + planType + ", planPacks="
					+ planPacks + ", isActive=" + isActive + ", uptoPots=" + uptoPots + "]";
		}
	    
	    
}
