package com.plants.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String title;
	private String message;
	private String typeIId;  // agentApp and userApp
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_agent_main_id" ,nullable = true)
	private AgentMain agentMain;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_cust_main_id" ,nullable = true)
	private CustomerMain customerMain;
	
	private boolean isRead = false; // Default to unread
	private LocalDateTime createdAt = LocalDateTime.now();
	public Notification() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Notification(int id, String title, String message, String typeIId, AgentMain agentMain, boolean isRead,
			LocalDateTime createdAt,CustomerMain customerMain) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
		this.typeIId = typeIId;
		this.agentMain = agentMain;
		this.isRead = isRead;
		this.createdAt = createdAt;
		this.customerMain= customerMain;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTypeIId() {
		return typeIId;
	}
	public void setTypeIId(String typeIId) {
		this.typeIId = typeIId;
	}
	public AgentMain getAgentMain() {
		return agentMain;
	}
	public void setAgentMain(AgentMain agentMain) {
		this.agentMain = agentMain;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public CustomerMain getCustomerMain() {
		return customerMain;
	}
	public void setCustomerMain(CustomerMain customerMain) {
		this.customerMain = customerMain;
	}
	@Override
	public String toString() {
		return "Notification [id=" + id + ", title=" + title + ", message=" + message + ", typeIId=" + typeIId
				+ ", isRead=" + isRead + ", createdAt=" + createdAt + "]";
	}
	
	
}
