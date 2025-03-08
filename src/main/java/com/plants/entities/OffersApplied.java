package com.plants.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OffersApplied {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int primaryKey;
	
	private String offerStatus;
	private String appTypeId;   // Agent  , Customer
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_offers_id",nullable = false)
	private Offers offers;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_agent_main_id",nullable = true)
	private AgentMain agentMain;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_customer_main_id",nullable = true)
	private CustomerMain customerMain;
	
	public OffersApplied() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OffersApplied(int primaryKey, String offerStatus, String appTypeId, Offers offers, AgentMain agentMain,
			CustomerMain customerMain) {
		super();
		this.primaryKey = primaryKey;
		this.offerStatus = offerStatus;
		this.appTypeId = appTypeId;
		this.offers = offers;
		this.agentMain = agentMain;
		this.customerMain = customerMain;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getOfferStatus() {
		return offerStatus;
	}

	public void setOfferStatus(String offerStatus) {
		this.offerStatus = offerStatus;
	}

	public String getAppTypeId() {
		return appTypeId;
	}

	public void setAppTypeId(String appTypeId) {
		this.appTypeId = appTypeId;
	}

	public Offers getOffers() {
		return offers;
	}

	public void setOffers(Offers offers) {
		this.offers = offers;
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

	@Override
	public String toString() {
		return "OffersApplied [primaryKey=" + primaryKey + ", offerStatus=" + offerStatus + ", appTypeId=" + appTypeId
				+ ", offers=" + offers + "]";
	}
	
	
}
