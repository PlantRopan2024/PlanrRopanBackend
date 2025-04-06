package com.plants.entities;

import java.util.List;

public class OrderSummaryRequest {
	
	private double servicesCharges;
	//private List<FertilizerRequest> fertilizers;
	private double platformfees;
	private double gstAmount;
	private boolean couponApplied;
	private double coupanAmount;
	private double grandTotal;
	public double getServicesCharges() {
		return servicesCharges;
	}
	public void setServicesCharges(double servicesCharges) {
		this.servicesCharges = servicesCharges;
	}
//	public List<FertilizerRequest> getFertilizers() {
//		return fertilizers;
//	}
//	public void setFertilizers(List<FertilizerRequest> fertilizers) {
//		this.fertilizers = fertilizers;
//	}
	public double getPlatformfees() {
		return platformfees;
	}
	public void setPlatformfees(double platformfees) {
		this.platformfees = platformfees;
	}
	public double getGstAmount() {
		return gstAmount;
	}
	public void setGstAmount(double gstAmount) {
		this.gstAmount = gstAmount;
	}
	public boolean isCouponApplied() {
		return couponApplied;
	}
	public void setCouponApplied(boolean couponApplied) {
		this.couponApplied = couponApplied;
	}
	public double getCoupanAmount() {
		return coupanAmount;
	}
	public void setCoupanAmount(double coupanAmount) {
		this.coupanAmount = coupanAmount;
	}
	public double getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}
	
	
	
}
