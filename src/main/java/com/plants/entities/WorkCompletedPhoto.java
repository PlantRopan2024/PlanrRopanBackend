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
import jakarta.persistence.Table;

@Entity
@Table(name = "work_completed")
public class WorkCompletedPhoto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primary_key;
	
	private String workCompletdPhoto1;
	private String workCompletdPhoto1_type;    
	@Column(columnDefinition = "TEXT")
	private String workCompletdPhoto1Path;
	
	private String workCompletdPhoto2;
	private String workCompletdPhoto2_type;
	@Column(columnDefinition = "TEXT")
	private String workCompletdPhoto2Path;
	
	private String OrderNumber;
	
	private LocalDateTime completedAt;

	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_order_id", nullable = true)
	private Order orders;

	public WorkCompletedPhoto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WorkCompletedPhoto(int primary_key, String workCompletdPhoto1, String workCompletdPhoto1_type,
			String workCompletdPhoto1Path, String workCompletdPhoto2, String workCompletdPhoto2_type,
			String workCompletdPhoto2Path, String orderNumber, Order orders,LocalDateTime completedAt) {
		super();
		this.primary_key = primary_key;
		this.workCompletdPhoto1 = workCompletdPhoto1;
		this.workCompletdPhoto1_type = workCompletdPhoto1_type;
		this.workCompletdPhoto1Path = workCompletdPhoto1Path;
		this.workCompletdPhoto2 = workCompletdPhoto2;
		this.workCompletdPhoto2_type = workCompletdPhoto2_type;
		this.workCompletdPhoto2Path = workCompletdPhoto2Path;
		OrderNumber = orderNumber;
		this.orders = orders;
		this.completedAt = completedAt;
	}

	public int getPrimary_key() {
		return primary_key;
	}

	public void setPrimary_key(int primary_key) {
		this.primary_key = primary_key;
	}

	public String getWorkCompletdPhoto1() {
		return workCompletdPhoto1;
	}

	public void setWorkCompletdPhoto1(String workCompletdPhoto1) {
		this.workCompletdPhoto1 = workCompletdPhoto1;
	}

	public String getWorkCompletdPhoto1_type() {
		return workCompletdPhoto1_type;
	}

	public void setWorkCompletdPhoto1_type(String workCompletdPhoto1_type) {
		this.workCompletdPhoto1_type = workCompletdPhoto1_type;
	}

	public String getWorkCompletdPhoto1Path() {
		return workCompletdPhoto1Path;
	}

	public void setWorkCompletdPhoto1Path(String workCompletdPhoto1Path) {
		this.workCompletdPhoto1Path = workCompletdPhoto1Path;
	}

	public String getWorkCompletdPhoto2() {
		return workCompletdPhoto2;
	}

	public void setWorkCompletdPhoto2(String workCompletdPhoto2) {
		this.workCompletdPhoto2 = workCompletdPhoto2;
	}

	public String getWorkCompletdPhoto2_type() {
		return workCompletdPhoto2_type;
	}

	public void setWorkCompletdPhoto2_type(String workCompletdPhoto2_type) {
		this.workCompletdPhoto2_type = workCompletdPhoto2_type;
	}

	public String getWorkCompletdPhoto2Path() {
		return workCompletdPhoto2Path;
	}

	public void setWorkCompletdPhoto2Path(String workCompletdPhoto2Path) {
		this.workCompletdPhoto2Path = workCompletdPhoto2Path;
	}

	public String getOrderNumber() {
		return OrderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		OrderNumber = orderNumber;
	}

	public Order getOrders() {
		return orders;
	}

	public void setOrders(Order orders) {
		this.orders = orders;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}
	
	
}
