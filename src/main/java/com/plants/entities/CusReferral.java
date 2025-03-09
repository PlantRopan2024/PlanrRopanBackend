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
public class CusReferral {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "referrer_cust_id", nullable = false)
	private int referrerCustId; // ID of the agent who referred

	@Column(name = "referred_cust_id")
	private int referredCustId; // ID of the referred agent

	@Column(name = "referral_code", nullable = false, length = 20)
	private String referralCode;

	@Column(name = "status", nullable = true, length = 20)
	private String status; // e.g., "PENDING", "APPROVED", "REJECTED"

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@OneToMany(mappedBy = "cusReferral")
    private List<WalletHistory> referralHistory = new ArrayList<>();
	
	public CusReferral() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CusReferral(int id, int referrerCustId, int referredCustId, String referralCode, String status,
			LocalDateTime createdAt, LocalDateTime updatedAt, List<WalletHistory> referralHistory) {
		super();
		this.id = id;
		this.referrerCustId = referrerCustId;
		this.referredCustId = referredCustId;
		this.referralCode = referralCode;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.referralHistory = referralHistory;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReferrerCustId() {
		return referrerCustId;
	}

	public void setReferrerCustId(int referrerCustId) {
		this.referrerCustId = referrerCustId;
	}

	public int getReferredCustId() {
		return referredCustId;
	}

	public void setReferredCustId(int referredCustId) {
		this.referredCustId = referredCustId;
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

	public List<WalletHistory> getReferralHistory() {
		return referralHistory;
	}

	public void setReferralHistory(List<WalletHistory> referralHistory) {
		this.referralHistory = referralHistory;
	}
	
	
}
