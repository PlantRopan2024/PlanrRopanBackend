package com.plants.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_order_id")
	private Order orders;

	public OrderFertilizers() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderFertilizers(int primaryKey, String fertilizerName, double amount, double kg, Order orders) {
		super();
		this.primaryKey = primaryKey;
		this.fertilizerName = fertilizerName;
		this.amount = amount;
		Kg = kg;
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
}
