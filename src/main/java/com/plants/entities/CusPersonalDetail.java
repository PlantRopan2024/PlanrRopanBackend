package com.plants.entities;

public class CusPersonalDetail {
	
	private int primarykey;
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobileNumber;
    private String address;
    private double latitude;
    private double loggitude;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	@Override
	public String toString() {
		return "CusPersonalDetail [primarykey=" + primarykey + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailId=" + emailId + ", mobileNumber=" + mobileNumber + ", address=" + address + ", latitude="
				+ latitude + ", loggitude=" + loggitude + "]";
	}
    
    
}
