package com.plants.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "SERVICE_NAME")
public class serviceName { // Renamed to PascalCase

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int primaryKey;

    private String isActive;

    private String name;

    @OneToMany(mappedBy = "serviceName")
    private List<Plans> plans = new ArrayList<>();

    // Getters and Setters
    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String isActive() {
        return isActive;
    }

    public void setActive(String isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Plans> getPlans() {
        return plans;
    }

    public void setPlans(List<Plans> plans) {
        this.plans = plans;
    }
}
