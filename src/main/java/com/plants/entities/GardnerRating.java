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
@Table(name = "gardner_rating")
public class GardnerRating {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primary_key;
	
	private double rating;
	private String comment;
	
	private String orderNumber;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_order_id", nullable = true)
	private Order orders;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_agent_main_id", nullable = true)
	private AgentMain agents;

	public GardnerRating() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GardnerRating(int primary_key, double rating, String comment, String orderNumber, Order orders,
			AgentMain agents) {
		super();
		this.primary_key = primary_key;
		this.rating = rating;
		this.comment = comment;
		this.orderNumber = orderNumber;
		this.orders = orders;
		this.agents = agents;
	}

	public int getPrimary_key() {
		return primary_key;
	}

	public void setPrimary_key(int primary_key) {
		this.primary_key = primary_key;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Order getOrders() {
		return orders;
	}

	public void setOrders(Order orders) {
		this.orders = orders;
	}

	public AgentMain getAgents() {
		return agents;
	}

	public void setAgents(AgentMain agents) {
		this.agents = agents;
	}
	
	
}
