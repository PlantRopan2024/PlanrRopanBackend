package com.plants.entities;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlansDto {

    private int servicesName;
    private String plansName;
    private String plansRs;
    private String timeDuration;

    @JsonProperty("UptoPots") 
    private String uptoPots;

    private String includingServicesName;
    private String planType;
    private String planPacks;

    @JsonProperty("isActive")
    private boolean active;

    private List<FertilizerDTO> fertilizers;

    // Getters and Setters
    public int getServicesName() {
        return servicesName;
    }

    public void setServicesName(int servicesName) {
        this.servicesName = servicesName;
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
        return uptoPots;
    }

    public void setUptoPots(String uptoPots) {
        this.uptoPots = uptoPots;
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
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<FertilizerDTO> getFertilizers() {
        return fertilizers;
    }

    public void setFertilizers(List<FertilizerDTO> fertilizers) {
        this.fertilizers = fertilizers;
    }

    @Override
    public String toString() {
        return "PlansDto{" +
                "servicesName=" + servicesName +
                ", plansName='" + plansName + '\'' +
                ", plansRs='" + plansRs + '\'' +
                ", timeDuration='" + timeDuration + '\'' +
                ", uptoPots='" + uptoPots + '\'' +
                ", includingServicesName='" + includingServicesName + '\'' +
                ", planType='" + planType + '\'' +
                ", planPacks='" + planPacks + '\'' +
                ", active=" + active +
                ", fertilizers=" + fertilizers +
                '}';
    }
}
