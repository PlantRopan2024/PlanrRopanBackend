package com.plants.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class AgentReferral {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "referrer_agent_id", nullable = false)
	private int referrerAgentId; // ID of the agent who referred

	@Column(name = "referred_agent_id")
	private int referredAgentId; // ID of the referred agent

	@Column(name = "referral_code", nullable = false, length = 20)
	private String referralCode;

	@Column(name = "status", nullable = true, length = 20)
	private String status; // e.g., "PENDING", "APPROVED", "REJECTED"

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@OneToMany(mappedBy = "agentReferral")
    private List<WalletHistory> referralHistory = new ArrayList<>();
	
	public AgentReferral() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AgentReferral(int id, int referrerAgentId, int referredAgentId, String referralCode, String status,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.referrerAgentId = referrerAgentId;
		this.referredAgentId = referredAgentId;
		this.referralCode = referralCode;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReferrerAgentId() {
		return referrerAgentId;
	}

	public void setReferrerAgentId(int referrerAgentId) {
		this.referrerAgentId = referrerAgentId;
	}

	public int getReferredAgentId() {
		return referredAgentId;
	}

	public void setReferredAgentId(int referredAgentId) {
		this.referredAgentId = referredAgentId;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "AgentRefferal [id=" + id + ", referrerAgentId=" + referrerAgentId + ", referredAgentId="
				+ referredAgentId + ", referralCode=" + referralCode + ", status=" + status + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
	
	
}
