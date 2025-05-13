package com.plants.entities;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
	
	private boolean couponApplied;
	
	private String offerCode;
	private LocalDateTime createdAt;
	private LocalTime startTime;
	private LocalTime endTime;;

	private String orderStatus; // PENDING, SUCCESS, FAILED
	private String address;
	private double latitude;
    private double longtitude;
    private double Km;
    private String shareCode;  //4 digit code
    
	private LocalDateTime orderCompletedDate;

	
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
    private List<OrderEarning> orderEarnings = new ArrayList<>();
	
	@OneToMany(mappedBy = "orders")
    private List<WorkCompletedPhoto> workCompletedPhoto = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "fk_plan_id", nullable = false)
	private Plans plans;

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public Order(int primary_key, String orderId, boolean couponApplied, String offerCode, LocalDateTime createdAt,
			LocalTime startTime, LocalTime endTime, String orderStatus, String address, double latitude,
			double longtitude, double km, String shareCode, Payment payment, AgentMain agentMain,
			CustomerMain customerMain, List<OrderFertilizers> orderFertilizers,
			List<WorkCompletedPhoto> workCompletedPhoto,List<OrderEarning> orderEarnings, Plans plans,LocalDateTime orderCompletedDate) {
		super();
		this.primary_key = primary_key;
		this.orderId = orderId;
		this.couponApplied = couponApplied;
		this.offerCode = offerCode;
		this.createdAt = createdAt;
		this.startTime = startTime;
		this.endTime = endTime;
		this.orderStatus = orderStatus;
		this.address = address;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.Km = km;
		this.shareCode = shareCode;
		this.payment = payment;
		this.agentMain = agentMain;
		this.customerMain = customerMain;
		this.orderFertilizers = orderFertilizers;
		this.workCompletedPhoto = workCompletedPhoto;
		this.orderEarnings = orderEarnings;
		this.plans = plans;
		this.orderCompletedDate= orderCompletedDate;
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

	
	public boolean isCouponApplied() {
		return couponApplied;
	}

	public void setCouponApplied(boolean couponApplied) {
		this.couponApplied = couponApplied;
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
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	public List<OrderEarning> getOrderEarnings() {
		return orderEarnings;
	}
	public void setOrderEarnings(List<OrderEarning> orderEarnings) {
		this.orderEarnings = orderEarnings;
	}

	public LocalDateTime getOrderCompletedDate() {
		return orderCompletedDate;
	}
	public void setOrderCompletedDate(LocalDateTime orderCompletedDate) {
		this.orderCompletedDate = orderCompletedDate;
	}
}
