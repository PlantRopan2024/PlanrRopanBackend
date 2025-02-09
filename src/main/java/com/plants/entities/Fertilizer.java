package com.plants.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fertilizer")
public class Fertilizer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int primaryKey;
	
	private String fertilizerName;
	private String amount;
	
	@ManyToOne
	@JoinColumn(name = "fk_plans_id")
	private Plans plans;

	public Fertilizer(int primaryKey, String fertilizerName, String amount, Plans plans) {
		super();
		this.primaryKey = primaryKey;
		this.fertilizerName = fertilizerName;
		this.amount = amount;
		this.plans = plans;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getFertilizerName() {
		return fertilizerName;
	}

	public void setFertilizerName(String fertilizerName) {
		this.fertilizerName = fertilizerName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Plans getPlans() {
		return plans;
	}

	public void setPlans(Plans plans) {
		this.plans = plans;
	}

	@Override
	public String toString() {
		return "Fertilizer [primaryKey=" + primaryKey + ", fertilizerName=" + fertilizerName + ", amount=" + amount
				+ ", plans=" + plans + "]";
	}
}
