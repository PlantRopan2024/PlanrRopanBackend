package com.plants.entities;

public class AgentPersonalDetails {
	
	private String selfieImg;
    private String firstName;
    private String lastName;
    private String gender;
    private String emailId;
    private String mobileNumber;
    private String state;
    private String city;
    private String address;
    private String pincode;
    private double latitude;
    private double longitude;
    private String fcmtoken;
	public String getSelfieImg() {
		return selfieImg;
	}
	public void setSelfieImg(String selfieImg) {
		this.selfieImg = selfieImg;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getFcmtoken() {
		return fcmtoken;
	}
	public void setFcmtoken(String fcmtoken) {
		this.fcmtoken = fcmtoken;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@Override
	public String toString() {
		return "AgentPersonalDetails [selfieImg=" + selfieImg + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailId=" + emailId + ", mobileNumber=" + mobileNumber + ", state=" + state + ", city=" + city
				+ ", address=" + address + ", pincode=" + pincode + ", latitude=" + latitude + ", longitude="
				+ longitude + ", fcmtoken=" + fcmtoken + "]";
	}
    
    
}
