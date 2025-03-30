package com.plants.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primary_key;
	
	@OneToOne
	@JsonIgnore
    @JoinColumn(name = "order_id", referencedColumnName = "primary_key", nullable = false)
    private Order order;
	private LocalDateTime createdAt;
	private String paymentMethod;   // UPI
	private String paymentStatus; // PENDING, SUCCESS, FAILED
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_cust_main_id", nullable = false)
	private CustomerMain customerMain;
	
	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Payment(int primary_key, Order order, LocalDateTime createdAt, String paymentMethod, String paymentStatus,
			CustomerMain customerMain) {
		super();
		this.primary_key = primary_key;
		this.order = order;
		this.createdAt = createdAt;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.customerMain = customerMain;
	}

	public int getPrimary_key() {
		return primary_key;
	}

	public void setPrimary_key(int primary_key) {
		this.primary_key = primary_key;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public CustomerMain getCustomerMain() {
		return customerMain;
	}

	public void setCustomerMain(CustomerMain customerMain) {
		this.customerMain = customerMain;
	}
	
	
}
