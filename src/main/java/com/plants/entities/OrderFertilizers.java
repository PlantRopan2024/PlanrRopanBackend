package com.plants.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders_fertilizer")
public class OrderFertilizers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int primaryKey;
	
	private String fertilizerName;
	private double amount;
	private double Kg;
	
	@Column(name = "margin_each_fertilizer", columnDefinition = "DOUBLE PRECISION DEFAULT 0")
	private double margineachFertilizer;
	
	@Column(name = "earning_malli_fertilizer", columnDefinition = "DOUBLE PRECISION DEFAULT 0")
	private double earningMalliFertilizer;
	
	@Column(name = "company_earning_fertilizer", columnDefinition = "DOUBLE PRECISION DEFAULT 0")
	private double companyEarningFertilizer;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_order_id")
	private Order orders;

	public OrderFertilizers() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderFertilizers(int primaryKey, String fertilizerName, double amount, double kg,
			double margineachFertilizer, double earningMalliFertilizer, double companyEarningFertilizer, Order orders) {
		super();
		this.primaryKey = primaryKey;
		this.fertilizerName = fertilizerName;
		this.amount = amount;
		Kg = kg;
		this.margineachFertilizer = margineachFertilizer;
		this.earningMalliFertilizer = earningMalliFertilizer;
		this.companyEarningFertilizer = companyEarningFertilizer;
		this.orders = orders;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getKg() {
		return Kg;
	}

	public void setKg(double kg) {
		Kg = kg;
	}

	public Order getOrders() {
		return orders;
	}

	public void setOrders(Order orders) {
		this.orders = orders;
	}

	public double getMargineachFertilizer() {
		return margineachFertilizer;
	}

	public void setMargineachFertilizer(double margineachFertilizer) {
		this.margineachFertilizer = margineachFertilizer;
	}

	public double getEarningMalliFertilizer() {
		return earningMalliFertilizer;
	}

	public void setEarningMalliFertilizer(double earningMalliFertilizer) {
		this.earningMalliFertilizer = earningMalliFertilizer;
	}

	public double getCompanyEarningFertilizer() {
		return companyEarningFertilizer;
	}

	public void setCompanyEarningFertilizer(double companyEarningFertilizer) {
		this.companyEarningFertilizer = companyEarningFertilizer;
	}
	
}
