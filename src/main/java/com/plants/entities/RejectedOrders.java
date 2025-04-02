package com.plants.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class RejectedOrders {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primaryKey;
	
	private String reason;
	private String orderStatus;
	private String orderNumber;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_order_id", nullable = true)
	private Order orders;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_agent_main_id", nullable = true)
	private AgentMain agents;

	public RejectedOrders() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RejectedOrders(int primaryKey, String reason, String orderStatus, String orderNumber, Order orders,
			AgentMain agents) {
		super();
		this.primaryKey = primaryKey;
		this.reason = reason;
		this.orderStatus = orderStatus;
		this.orderNumber = orderNumber;
		this.orders = orders;
		this.agents = agents;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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
