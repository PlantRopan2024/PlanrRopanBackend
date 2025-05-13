package com.plants.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "hourly_rollup_order")
public class HourlyRollupOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primary_key;
	
	private double grandTotalAmount;
	private double plansRs;
	private double companyEarningRs;
	private double agentEarningRs;
	private double platformFess;
	private double couponAppliedRs;
	private double companyFertilizerAmount;
	private double agentFertilizerAmount;
	private double totalOrder;
	
	private LocalDateTime startHours;
	private LocalDateTime endHours;

	
	private LocalDateTime createdAt;
	private LocalDate rollupAt;
	
	public HourlyRollupOrder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HourlyRollupOrder(int primary_key, double grandTotalAmount, double plansRs, double companyEarningRs,
			double agentEarningRs, double platformFess, double couponAppliedRs, double companyFertilizerAmount,
			double agentFertilizerAmount, double totalOrder, LocalDateTime createdAt, LocalDate rollupAt,LocalDateTime startHours,LocalDateTime endHours) {
		super();
		this.primary_key = primary_key;
		this.grandTotalAmount = grandTotalAmount;
		this.plansRs = plansRs;
		this.companyEarningRs = companyEarningRs;
		this.agentEarningRs = agentEarningRs;
		this.platformFess = platformFess;
		this.couponAppliedRs = couponAppliedRs;
		this.companyFertilizerAmount = companyFertilizerAmount;
		this.agentFertilizerAmount = agentFertilizerAmount;
		this.totalOrder = totalOrder;
		this.createdAt = createdAt;
		this.rollupAt = rollupAt;
		this.startHours=startHours;
		this.endHours= endHours;
	}

	public int getPrimary_key() {
		return primary_key;
	}

	public void setPrimary_key(int primary_key) {
		this.primary_key = primary_key;
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

	public double getCouponAppliedRs() {
		return couponAppliedRs;
	}

	public void setCouponAppliedRs(double couponAppliedRs) {
		this.couponAppliedRs = couponAppliedRs;
	}

	public double getCompanyFertilizerAmount() {
		return companyFertilizerAmount;
	}

	public void setCompanyFertilizerAmount(double companyFertilizerAmount) {
		this.companyFertilizerAmount = companyFertilizerAmount;
	}

	public double getAgentFertilizerAmount() {
		return agentFertilizerAmount;
	}

	public void setAgentFertilizerAmount(double agentFertilizerAmount) {
		this.agentFertilizerAmount = agentFertilizerAmount;
	}

	public double getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(double totalOrder) {
		this.totalOrder = totalOrder;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getRollupAt() {
		return rollupAt;
	}

	public void setRollupAt(LocalDate rollupAt) {
		this.rollupAt = rollupAt;
	}

	public LocalDateTime getStartHours() {
		return startHours;
	}

	public void setStartHours(LocalDateTime startHours) {
		this.startHours = startHours;
	}

	public LocalDateTime getEndHours() {
		return endHours;
	}

	public void setEndHours(LocalDateTime endHours) {
		this.endHours = endHours;
	}
	
	
}
