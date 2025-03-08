package com.plants.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Customer_Main" , uniqueConstraints = {@UniqueConstraint(columnNames = {"mobileNumber"})})
public class CustomerMain {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primarykey;
	private String firstName;
	private String lastName;
	private String emailId;
	@Column(unique = true)
	private String mobileNumber;
	private String address;
	private String city;
	private double latitude;
	private double loggitude;
	private String token;
	private String firebasetokenCus;
	@Column(name = "is_profile_completed")
	private boolean isProfileCompleted;
	
	@Column(name = "cus_referral_code", unique = true)
	private String cusReferralCode;
	
	@OneToMany(mappedBy = "customerMain")
    private List<WalletHistory> referralHistory = new ArrayList<>();
	
	@OneToMany(mappedBy = "customerMain")
    private List<Notification> notification = new ArrayList<>();
	
	public CustomerMain() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerMain(int primarykey, String firstName, String lastName, String emailId, String mobileNumber,
			String address, String city, double latitude, double loggitude, String token, String firebasetokenCus,
			boolean isProfileCompleted, String cusReferralCode, List<WalletHistory> referralHistory,
			List<Notification> notification) {
		super();
		this.primarykey = primarykey;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.mobileNumber = mobileNumber;
		this.address = address;
		this.city = city;
		this.latitude = latitude;
		this.loggitude = loggitude;
		this.token = token;
		this.firebasetokenCus = firebasetokenCus;
		this.isProfileCompleted = isProfileCompleted;
		this.cusReferralCode = cusReferralCode;
		this.referralHistory = referralHistory;
		this.notification = notification;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getPrimarykey() {
		return primarykey;
	}
	public void setPrimarykey(int primarykey) {
		this.primarykey = primarykey;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLoggitude() {
		return loggitude;
	}
	public void setLoggitude(double loggitude) {
		this.loggitude = loggitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isProfileCompleted() {
		return isProfileCompleted;
	}

	public void setProfileCompleted(boolean isProfileCompleted) {
		this.isProfileCompleted = isProfileCompleted;
	}

	public String getFirebasetokenCus() {
		return firebasetokenCus;
	}

	public void setFirebasetokenCus(String firebasetokenCus) {
		this.firebasetokenCus = firebasetokenCus;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCusReferralCode() {
		return cusReferralCode;
	}

	public void setCusReferralCode(String cusReferralCode) {
		this.cusReferralCode = cusReferralCode;
	}

	public List<WalletHistory> getReferralHistory() {
		return referralHistory;
	}

	public void setReferralHistory(List<WalletHistory> referralHistory) {
		this.referralHistory = referralHistory;
	}

	public List<Notification> getNotification() {
		return notification;
	}

	public void setNotification(List<Notification> notification) {
		this.notification = notification;
	}
	
	
	
}
