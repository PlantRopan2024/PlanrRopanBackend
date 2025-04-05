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
public class OrderEarning {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int primaryKey;
	
	private double grandTotalAmount;
	private double plansRs;
	private double marginPercentPerOrder;
	private double companyEarningRs;
	private double agentEarningRs;
	private double platformFess;
	private double gstPercentFees;
	private double gstAmountRs;
	private double couponAppliedRs;
	
	private String earningStatus;
	
	private LocalDateTime createdAt;
	
	@Column(unique = true, nullable = false)
	private String orderNumber;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_order_id", nullable = false)
	private Order orders;
	
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_agent_main_id", nullable = true)
	private AgentMain agentMain;


	public OrderEarning() {
		super();
		// TODO Auto-generated constructor stub
	}


	public OrderEarning(int primaryKey, double grandTotalAmount, double plansRs, double marginPercentPerOrder,
			double companyEarningRs, double agentEarningRs, double platformFess, double gstPercentFees,
			double gstAmountRs, double couponAppliedRs, String orderNumber, Order orders, AgentMain agentMain,String earningStatus,LocalDateTime createdAt) {
		super();
		this.primaryKey = primaryKey;
		this.grandTotalAmount = grandTotalAmount;
		this.plansRs = plansRs;
		this.marginPercentPerOrder = marginPercentPerOrder;
		this.companyEarningRs = companyEarningRs;
		this.agentEarningRs = agentEarningRs;
		this.platformFess = platformFess;
		this.gstPercentFees = gstPercentFees;
		this.gstAmountRs = gstAmountRs;
		this.couponAppliedRs = couponAppliedRs;
		this.orderNumber = orderNumber;
		this.orders = orders;
		this.agentMain = agentMain;
		this.earningStatus=earningStatus;
		this.createdAt=createdAt;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public double getGrandTotalAmount() {
		return grandTotalAmount;
	}

	public void setGrandTotalAmount(double grandTotalAmount) {
		this.grandTotalAmount = grandTotalAmount;
	}

	public double getPlansRs() {
		return plansRs;
	}

	public void setPlansRs(double plansRs) {
		this.plansRs = plansRs;
	}

	public double getMarginPercentPerOrder() {
		return marginPercentPerOrder;
	}

	public void setMarginPercentPerOrder(double marginPercentPerOrder) {
		this.marginPercentPerOrder = marginPercentPerOrder;
	}

	public double getCompanyEarningRs() {
		return companyEarningRs;
	}

	public void setCompanyEarningRs(double companyEarningRs) {
		this.companyEarningRs = companyEarningRs;
	}

	public double getAgentEarningRs() {
		return agentEarningRs;
	}

	public void setAgentEarningRs(double agentEarningRs) {
		this.agentEarningRs = agentEarningRs;
	}

	public double getPlatformFess() {
		return platformFess;
	}


	public void setPlatformFess(double platformFess) {
		this.platformFess = platformFess;
	}

	public double getGstPercentFees() {
		return gstPercentFees;
	}

	public void setGstPercentFees(double gstPercentFees) {
		this.gstPercentFees = gstPercentFees;
	}

	public double getGstAmountRs() {
		return gstAmountRs;
	}

	public void setGstAmountRs(double gstAmountRs) {
		this.gstAmountRs = gstAmountRs;
	}

	public double getCouponAppliedRs() {
		return couponAppliedRs;
	}

	public void setCouponAppliedRs(double couponAppliedRs) {
		this.couponAppliedRs = couponAppliedRs;
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

	public AgentMain getAgentMain() {
		return agentMain;
	}
	public void setAgentMain(AgentMain agentMain) {
		this.agentMain = agentMain;
	}
	public String getEarningStatus() {
		return earningStatus;
	}

	public void setEarningStatus(String earningStatus) {
		this.earningStatus = earningStatus;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}	
	
}
