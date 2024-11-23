package com.plants.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Customer_Main" , uniqueConstraints = {@UniqueConstraint(columnNames = {"mobileNumber" , "emailId"})})
public class CustomerMain {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primarykey;
	
	public CustomerMain(String firstName, String lastName, String emailId, String mobileNumber ,String address,  double latitude, double loggitude) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.address = address;
		this.mobileNumber = mobileNumber;
		this.latitude=latitude;
		this.loggitude=loggitude;
	}
	public CustomerMain () {
		
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
	@Column(unique = true)
	private String firstName;
	private String lastName;
	@Column(unique = true)
	private String emailId;
	@Column(unique = true)
	private String mobileNumber;
	private String address;
	private double latitude;
	private double loggitude;

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
	
}
