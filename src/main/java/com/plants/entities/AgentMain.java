package com.plants.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Agent_Main" , uniqueConstraints = {@UniqueConstraint(columnNames = {"mobileNumber","emailId","aadhaarNumber","accNumber","AccMobNumber"})})
public class AgentMain {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int AgentIDPk;
	
	private String firstName;
	private String lastName;
	private String gender;
	private String selfieImg;
	@Column(unique = true)
	private String emailId;
	@Column(unique = true)
	private String mobileNumber;
	 @Column(name = "agent_verified", nullable = true)
	boolean AgentVerified;
	 @Column(name = "is_active_agent", nullable = true)
	boolean isActiveAgent;
	private String state;
	private String city;
	private String address;
	private String pincode;
	private double latitude;
	private double longitude;
	
	@Column(unique = true)
	private String aadhaarNumber;
	
	private String aadharImgFrontSide;
	private String aadharImgBackSide;
	
	private String token;
	private String accHolderName;
	@Column(unique = true)
	private String accNumber;
	private String bankName;
	private String ifscCode;
	private String bankPassBookImage;
	@Column(name = "is_profile_completed", nullable = true)
	private boolean isProfileCompleted;
	private String fcmTokenAgent;
	public AgentMain() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AgentMain(int agentIDPk, String firstName, String lastName, String selfieImg, String emailId,
			String mobileNumber, boolean agentVerified, boolean isActiveAgent, String state, String city,
			String address, String pincode, double latitude, double longitude, String aadhaarNumber,
			String aadharImgFrontSide, String aadharImgBackSide, String accHolderName, String accNumber,
			String bankName, String ifscCode, String bankPassBookImage, String fcmTokenAgent , String token , boolean isProfileCompleted, String gender ) {
		super();
		this.AgentIDPk = agentIDPk;
		this.firstName = firstName;
		this.lastName = lastName;
		this.selfieImg = selfieImg;
		this.emailId = emailId;
		this.mobileNumber = mobileNumber;
		this.AgentVerified = agentVerified;
		this.isActiveAgent = isActiveAgent;
		this.state = state;
		this.city = city;
		this.address = address;
		this.pincode = pincode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.aadhaarNumber = aadhaarNumber;
		this.aadharImgFrontSide = aadharImgFrontSide;
		this.aadharImgBackSide = aadharImgBackSide;
		this.accHolderName = accHolderName;
		this.accNumber = accNumber;
		this.bankName = bankName;
		this.ifscCode = ifscCode;
		this.bankPassBookImage = bankPassBookImage;
		this.fcmTokenAgent = fcmTokenAgent;
		this.token=token;
		this.isProfileCompleted= isProfileCompleted;
		this.gender=gender;
	}


	public int getAgentIDPk() {
		return AgentIDPk;
	}
	public void setAgentIDPk(int agentIDPk) {
		AgentIDPk = agentIDPk;
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
	public String getSelfieImg() {
		return selfieImg;
	}
	public void setSelfieImg(String selfieImg) {
		this.selfieImg = selfieImg;
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
	public boolean isAgentVerified() {
		return AgentVerified;
	}
	public void setAgentVerified(boolean agentVerified) {
		AgentVerified = agentVerified;
	}
	public boolean isActiveAgent() {
		return isActiveAgent;
	}
	public void setActiveAgent(boolean isActiveAgent) {
		this.isActiveAgent = isActiveAgent;
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
	public String getAadhaarNumber() {
		return aadhaarNumber;
	}
	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}
	public String getAadharImgFrontSide() {
		return aadharImgFrontSide;
	}
	public void setAadharImgFrontSide(String aadharImgFrontSide) {
		this.aadharImgFrontSide = aadharImgFrontSide;
	}
	public String getAadharImgBackSide() {
		return aadharImgBackSide;
	}
	public void setAadharImgBackSide(String aadharImgBackSide) {
		this.aadharImgBackSide = aadharImgBackSide;
	}
	public String getAccHolderName() {
		return accHolderName;
	}
	public void setAccHolderName(String accHolderName) {
		this.accHolderName = accHolderName;
	}
	public String getAccNumber() {
		return accNumber;
	}
	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getBankPassBookImage() {
		return bankPassBookImage;
	}
	public void setBankPassBookImage(String bankPassBookImage) {
		this.bankPassBookImage = bankPassBookImage;
	}
	public String getFcmTokenAgent() {
		return fcmTokenAgent;
	}
	public void setFcmTokenAgent(String fcmTokenAgent) {
		this.fcmTokenAgent = fcmTokenAgent;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean isProfileCompleted() {
		return isProfileCompleted;
	}
	public void setProfileCompleted(boolean isProfileCompleted) {
		this.isProfileCompleted = isProfileCompleted;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "AgentMain [AgentIDPk=" + AgentIDPk + ", firstName=" + firstName + ", lastName=" + lastName + ", gender="
				+ gender + ", selfieImg=" + selfieImg + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber
				+ ", AgentVerified=" + AgentVerified + ", isActiveAgent=" + isActiveAgent + ", state=" + state
				+ ", city=" + city + ", address=" + address + ", pincode=" + pincode + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", aadhaarNumber=" + aadhaarNumber + ", aadharImgFrontSide="
				+ aadharImgFrontSide + ", aadharImgBackSide=" + aadharImgBackSide + ", token=" + token
				+ ", accHolderName=" + accHolderName + ", accNumber=" + accNumber + ", bankName=" + bankName
				+ ", ifscCode=" + ifscCode + ", bankPassBookImage=" + bankPassBookImage + ", isProfileCompleted="
				+ isProfileCompleted + ", fcmTokenAgent=" + fcmTokenAgent + "]";
	}
}