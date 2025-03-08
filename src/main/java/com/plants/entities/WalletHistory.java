package com.plants.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class WalletHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "description")
    private String description;
	
	private String appTypeId;
	
	@Column(name = "amount", nullable = false)
    private Double amount;
	
	@Column(name = "transaction_type", nullable = false)
    private String transactionType; // "REWARD" or "DEDUCTION"

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_agent_Referral_id",nullable = true)
	private AgentReferral agentReferral;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_Cus_Referral_id",nullable = true)
	private CusReferral cusReferral;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_agent_main_id",nullable = true)
	private AgentMain agentMain;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_customer_main_id",nullable = true)
	private CustomerMain customerMain;
	
	public WalletHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WalletHistory(int id, String description, Double amount, String transactionType, LocalDateTime createdAt,
			AgentReferral agentReferral, AgentMain agentMain,String appTypeId,CustomerMain customerMain) {
		super();
		this.id = id;
		this.description = description;
		this.amount = amount;
		this.transactionType = transactionType;
		this.createdAt = createdAt;
		this.agentReferral = agentReferral;
		this.agentMain = agentMain;
		this.appTypeId = appTypeId;
		this.customerMain= customerMain;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public AgentReferral getAgentReferral() {
		return agentReferral;
	}

	public void setAgentReferral(AgentReferral agentReferral) {
		this.agentReferral = agentReferral;
	}
	

	public AgentMain getAgentMain() {
		return agentMain;
	}

	public void setAgentMain(AgentMain agentMain) {
		this.agentMain = agentMain;
	}
	

	public String getAppTypeId() {
		return appTypeId;
	}

	public void setAppTypeId(String appTypeId) {
		this.appTypeId = appTypeId;
	}

	public CusReferral getCusReferral() {
		return cusReferral;
	}

	public void setCusReferral(CusReferral cusReferral) {
		this.cusReferral = cusReferral;
	}

	public CustomerMain getCustomerMain() {
		return customerMain;
	}

	public void setCustomerMain(CustomerMain customerMain) {
		this.customerMain = customerMain;
	}

	@Override
	public String toString() {
		return "WalletHistory [id=" + id + ", description=" + description + ", amount=" + amount + ", transactionType="
				+ transactionType + ", createdAt=" + createdAt + ", agentReferral=" + agentReferral + "]";
	}
	
}
