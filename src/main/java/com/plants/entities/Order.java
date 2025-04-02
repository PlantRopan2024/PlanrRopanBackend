package com.plants.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primary_key;

	@Column(unique = true, nullable = false)
	private String orderId; // Unique Order ID
	private double servicesCharges;
	private double platformfees;
	private double gstAmount;
	private boolean couponApplied;
	private double coupanAmount;
	private double totalAmount;
	private String offerCode;
	private LocalDateTime createdAt;
	private String orderStatus; // PENDING, SUCCESS, FAILED
	private String address;
	private double latitude;
    private double longtitude;
    private double Km;
    private String shareCode;  //4 digit code
	
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_agent_main_id", nullable = true)
	private AgentMain agentMain;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_cust_main_id", nullable = true)
	private CustomerMain customerMain;
	
	@OneToMany(mappedBy = "orders")
    private List<OrderFertilizers> orderFertilizers = new ArrayList<>();
	
	@OneToMany(mappedBy = "orders")
    private List<WorkCompletedPhoto> workCompletedPhoto = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "fk_plan_id", nullable = false)
	private Plans plans;

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Order(int primary_key, String orderId, double servicesCharges, double platformfees, double gstAmount,
			boolean couponApplied, double coupanAmount, double totalAmount, String offerCode, LocalDateTime createdAt,
			String orderStatus, String address, double latitude, double longtitude, String shareCode, Payment payment,
			AgentMain agentMain, CustomerMain customerMain, List<OrderFertilizers> orderFertilizers,
			List<WorkCompletedPhoto> workCompletedPhoto, Plans plans, double Km) {
		super();
		this.primary_key = primary_key;
		this.orderId = orderId;
		this.servicesCharges = servicesCharges;
		this.platformfees = platformfees;
		this.gstAmount = gstAmount;
		this.couponApplied = couponApplied;
		this.coupanAmount = coupanAmount;
		this.totalAmount = totalAmount;
		this.offerCode = offerCode;
		this.createdAt = createdAt;
		this.orderStatus = orderStatus;
		this.address = address;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.shareCode = shareCode;
		this.payment = payment;
		this.agentMain = agentMain;
		this.customerMain = customerMain;
		this.orderFertilizers = orderFertilizers;
		this.workCompletedPhoto = workCompletedPhoto;
		this.plans = plans;
		this.Km=Km;
	}

	public int getPrimary_key() {
		return primary_key;
	}

	public void setPrimary_key(int primary_key) {
		this.primary_key = primary_key;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public double getServicesCharges() {
		return servicesCharges;
	}

	public void setServicesCharges(double servicesCharges) {
		this.servicesCharges = servicesCharges;
	}

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

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getOfferCode() {
		return offerCode;
	}
	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public List<OrderFertilizers> getOrderFertilizers() {
		return orderFertilizers;
	}

	public void setOrderFertilizers(List<OrderFertilizers> orderFertilizers) {
		this.orderFertilizers = orderFertilizers;
	}

	public Plans getPlans() {
		return plans;
	}

	public void setPlans(Plans plans) {
		this.plans = plans;
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
	public String getShareCode() {
		return shareCode;
	}
	public void setShareCode(String shareCode) {
		this.shareCode = shareCode;
	}
	public List<WorkCompletedPhoto> getWorkCompletedPhoto() {
		return workCompletedPhoto;
	}
	public void setWorkCompletedPhoto(List<WorkCompletedPhoto> workCompletedPhoto) {
		this.workCompletedPhoto = workCompletedPhoto;
	}
	public double getKm() {
		return Km;
	}
	public void setKm(double km) {
		Km = km;
	}
}
