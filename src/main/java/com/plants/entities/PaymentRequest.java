package com.plants.entities;

public class PaymentRequest {
	
	private OrderSummaryRequest orderSummaryRequest;
	private String OfferCode;
	private String planId;
	private String address;
    private String paymentMethod;  // UPI / Card / Net Banking
    private String paymentPlatform; // google pay , paytm, phone pe
    private double latitude;
    private double longtitude;
    
	public OrderSummaryRequest getOrderSummaryRequest() {
		return orderSummaryRequest;
	}
	public void setOrderSummaryRequest(OrderSummaryRequest orderSummaryRequest) {
		this.orderSummaryRequest = orderSummaryRequest;
	}
	
	public String getOfferCode() {
		return OfferCode;
	}
	public void setOfferCode(String offerCode) {
		OfferCode = offerCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getPaymentPlatform() {
		return paymentPlatform;
	}
	public void setPaymentPlatform(String paymentPlatform) {
		this.paymentPlatform = paymentPlatform;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	
}
