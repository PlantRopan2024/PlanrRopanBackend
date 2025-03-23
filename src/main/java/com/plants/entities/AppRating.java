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
@Table(name = "app_Rating")
public class AppRating {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int primaryKey;
	
    private int rating;   
	
    @ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_agent_main_id" ,nullable = true)
	private AgentMain agentMain;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_cust_main_id" ,nullable = true)
	private CustomerMain customerMain;

	public AppRating() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppRating(int primaryKey, int rating, AgentMain agentMain, CustomerMain customerMain) {
		super();
		this.primaryKey = primaryKey;
		this.rating = rating;
		this.agentMain = agentMain;
		this.customerMain = customerMain;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public AgentMain getAgentMain() {
		return agentMain;
	}

	public void setAgentMain(AgentMain agentMain) {
		this.agentMain = agentMain;
	}

	public CustomerMain getCustomerMain() {
		return customerMain;
	}

	public void setCustomerMain(CustomerMain customerMain) {
		this.customerMain = customerMain;
	}
	
	
}
