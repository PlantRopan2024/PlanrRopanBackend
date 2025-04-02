package com.plants.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Agent_Main" , uniqueConstraints = {@UniqueConstraint(columnNames = {"mobileNumber"})})
public class AgentMain {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int AgentIDPk;
	private String firstName;
	private String lastName;
	private String gender;
	private String selfieImg;
	@Column(columnDefinition = "TEXT")
	private String selfieImagePath;
	private String selfieImg_type;    
	private String emailId;
	@Column(unique = true)
	private String mobileNumber;
	@Column(name = "agent_approved", nullable = true)
	boolean agentApproved;
	@Column(name = "is_active_agent", nullable = true)
	boolean isActiveAgent;
	private String state;
	private String city;
	private String address;
	private String pincode;
	private double latitude;
	private double longitude;
	private String aadhaarNumber;
	private String aadharImgFrontSide;
	private String aadharImgFrontSide_type;    
	@Column(columnDefinition = "TEXT")
	private String aadharImagFrontSidePath;
	private String aadharImgBackSide;
	private String aadharImgBackSide_type;
	@Column(columnDefinition = "TEXT")
	private String aadharImagBackSidePath;
	private String token;
	private String accHolderName;
	private String accNumber;
	private String bankName;
	private String ifscCode;
	private String bankPassBookImage;
	private String bankPassBookImage_type;    
	private boolean isReferral;
	@Column(columnDefinition = "TEXT")
	private String bankPassBookImagePath;
	@Column(name = "is_profile_completed", nullable = true)
	private boolean isProfileCompleted;
	private String fcmTokenAgent;
	@Column(name = "is_Profile_Info_Step_First", nullable = true)
	private boolean isProfileInfoStepFirst;
	@Column(name = "is_Aadhar_Info_Step_Second", nullable = true)
	private boolean isAadharInfoStepSecond;
	@Column(name = "is_Bank_Info_Step_Third", nullable = true)
	private boolean isBankInfoStepThird;
	
	@Column(name = "agent_referral_code", unique = true)
	private String agentReferralCode;
	
	@OneToMany(mappedBy = "agentMain")
    private List<WalletHistory> referralHistory = new ArrayList<>();
	
	@OneToMany(mappedBy = "agentMain")
    private List<Notification> notification = new ArrayList<>();
	
	private String workStatus;
	public AgentMain() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AgentMain(int agentIDPk, String firstName, String lastName, String gender, String selfieImg,
			String selfieImagePath, String selfieImg_type, String emailId, String mobileNumber, boolean agentApproved,
			boolean isActiveAgent, String state, String city, String address, String pincode, double latitude,
			double longitude, String aadhaarNumber, String aadharImgFrontSide, String aadharImgFrontSide_type,
			String aadharImagFrontSidePath, String aadharImgBackSide, String aadharImgBackSide_type,
			String aadharImagBackSidePath, String token, String accHolderName, String accNumber, String bankName,
			String ifscCode, String bankPassBookImage, String bankPassBookImage_type, String bankPassBookImagePath,
			boolean isProfileCompleted, String fcmTokenAgent, boolean isProfileInfoStepFirst,
			boolean isAadharInfoStepSecond, boolean isBankInfoStepThird, String agentReferralCode,
			List<WalletHistory> referralHistory, List<Notification> notification,boolean isReferral,String workStatus) {
		super();
		AgentIDPk = agentIDPk;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.selfieImg = selfieImg;
		this.selfieImagePath = selfieImagePath;
		this.selfieImg_type = selfieImg_type;
		this.emailId = emailId;
		this.mobileNumber = mobileNumber;
		this.agentApproved = agentApproved;
		this.isActiveAgent = isActiveAgent;
		this.state = state;
		this.city = city;
		this.address = address;
		this.pincode = pincode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.aadhaarNumber = aadhaarNumber;
		this.aadharImgFrontSide = aadharImgFrontSide;
		this.aadharImgFrontSide_type = aadharImgFrontSide_type;
		this.aadharImagFrontSidePath = aadharImagFrontSidePath;
		this.aadharImgBackSide = aadharImgBackSide;
		this.aadharImgBackSide_type = aadharImgBackSide_type;
		this.aadharImagBackSidePath = aadharImagBackSidePath;
		this.token = token;
		this.accHolderName = accHolderName;
		this.accNumber = accNumber;
		this.bankName = bankName;
		this.ifscCode = ifscCode;
		this.bankPassBookImage = bankPassBookImage;
		this.bankPassBookImage_type = bankPassBookImage_type;
		this.bankPassBookImagePath = bankPassBookImagePath;
		this.isProfileCompleted = isProfileCompleted;
		this.fcmTokenAgent = fcmTokenAgent;
		this.isProfileInfoStepFirst = isProfileInfoStepFirst;
		this.isAadharInfoStepSecond = isAadharInfoStepSecond;
		this.isBankInfoStepThird = isBankInfoStepThird;
		this.agentReferralCode = agentReferralCode;
		this.referralHistory = referralHistory;
		this.notification = notification;
		this.isReferral= isReferral;
		this.workStatus =workStatus;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSelfieImg() {
		return selfieImg;
	}
	public void setSelfieImg(String selfieImg) {
		this.selfieImg = selfieImg;
	}
	public String getSelfieImagePath() {
		return selfieImagePath;
	}
	public void setSelfieImagePath(String selfieImagePath) {
		this.selfieImagePath = selfieImagePath;
	}
	public String getSelfieImg_type() {
		return selfieImg_type;
	}
	public void setSelfieImg_type(String selfieImg_type) {
		this.selfieImg_type = selfieImg_type;
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
	public boolean isAgentApproved() {
		return agentApproved;
	}
	public void setAgentApproved(boolean agentApproved) {
		this.agentApproved = agentApproved;
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
	public String getAadharImgFrontSide_type() {
		return aadharImgFrontSide_type;
	}
	public void setAadharImgFrontSide_type(String aadharImgFrontSide_type) {
		this.aadharImgFrontSide_type = aadharImgFrontSide_type;
	}
	public String getAadharImagFrontSidePath() {
		return aadharImagFrontSidePath;
	}
	public void setAadharImagFrontSidePath(String aadharImagFrontSidePath) {
		this.aadharImagFrontSidePath = aadharImagFrontSidePath;
	}
	public String getAadharImgBackSide() {
		return aadharImgBackSide;
	}
	public void setAadharImgBackSide(String aadharImgBackSide) {
		this.aadharImgBackSide = aadharImgBackSide;
	}
	public String getAadharImgBackSide_type() {
		return aadharImgBackSide_type;
	}
	public void setAadharImgBackSide_type(String aadharImgBackSide_type) {
		this.aadharImgBackSide_type = aadharImgBackSide_type;
	}
	public String getAadharImagBackSidePath() {
		return aadharImagBackSidePath;
	}
	public void setAadharImagBackSidePath(String aadharImagBackSidePath) {
		this.aadharImagBackSidePath = aadharImagBackSidePath;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	public String getBankPassBookImage_type() {
		return bankPassBookImage_type;
	}
	public void setBankPassBookImage_type(String bankPassBookImage_type) {
		this.bankPassBookImage_type = bankPassBookImage_type;
	}
	public String getBankPassBookImagePath() {
		return bankPassBookImagePath;
	}
	public void setBankPassBookImagePath(String bankPassBookImagePath) {
		this.bankPassBookImagePath = bankPassBookImagePath;
	}
	public boolean isProfileCompleted() {
		return isProfileCompleted;
	}
	public void setProfileCompleted(boolean isProfileCompleted) {
		this.isProfileCompleted = isProfileCompleted;
	}
	public String getFcmTokenAgent() {
		return fcmTokenAgent;
	}
	public void setFcmTokenAgent(String fcmTokenAgent) {
		this.fcmTokenAgent = fcmTokenAgent;
	}
	public boolean isProfileInfoStepFirst() {
		return isProfileInfoStepFirst;
	}
	public void setProfileInfoStepFirst(boolean isProfileInfoStepFirst) {
		this.isProfileInfoStepFirst = isProfileInfoStepFirst;
	}
	public boolean isAadharInfoStepSecond() {
		return isAadharInfoStepSecond;
	}
	public void setAadharInfoStepSecond(boolean isAadharInfoStepSecond) {
		this.isAadharInfoStepSecond = isAadharInfoStepSecond;
	}
	public boolean isBankInfoStepThird() {
		return isBankInfoStepThird;
	}
	public void setBankInfoStepThird(boolean isBankInfoStepThird) {
		this.isBankInfoStepThird = isBankInfoStepThird;
	}
	
	public String getAgentReferralCode() {
		return agentReferralCode;
	}
	public void setAgentReferralCode(String agentReferralCode) {
		this.agentReferralCode = agentReferralCode;
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
	public boolean isReferral() {
		return isReferral;
	}
	public void setReferral(boolean isReferral) {
		this.isReferral = isReferral;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	@Override
	public String toString() {
		return "AgentMain [AgentIDPk=" + AgentIDPk + ", firstName=" + firstName + ", lastName=" + lastName + ", gender="
				+ gender + ", selfieImg=" + selfieImg + ", selfieImagePath=" + selfieImagePath + ", selfieImg_type="
				+ selfieImg_type + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber + ", agentApproved="
				+ agentApproved + ", isActiveAgent=" + isActiveAgent + ", state=" + state + ", city=" + city
				+ ", address=" + address + ", pincode=" + pincode + ", latitude=" + latitude + ", longitude="
				+ longitude + ", aadhaarNumber=" + aadhaarNumber + ", aadharImgFrontSide=" + aadharImgFrontSide
				+ ", aadharImgFrontSide_type=" + aadharImgFrontSide_type + ", aadharImagFrontSidePath="
				+ aadharImagFrontSidePath + ", aadharImgBackSide=" + aadharImgBackSide + ", aadharImgBackSide_type="
				+ aadharImgBackSide_type + ", aadharImagBackSidePath=" + aadharImagBackSidePath + ", token=" + token
				+ ", accHolderName=" + accHolderName + ", accNumber=" + accNumber + ", bankName=" + bankName
				+ ", ifscCode=" + ifscCode + ", bankPassBookImage=" + bankPassBookImage + ", bankPassBookImage_type="
				+ bankPassBookImage_type + ", bankPassBookImagePath=" + bankPassBookImagePath + ", isProfileCompleted="
				+ isProfileCompleted + ", fcmTokenAgent=" + fcmTokenAgent + ", isProfileInfoStepFirst="
				+ isProfileInfoStepFirst + ", isAadharInfoStepSecond=" + isAadharInfoStepSecond
				+ ", isBankInfoStepThird=" + isBankInfoStepThird + ", agentReferralCode=" + agentReferralCode
				+ ", referralHistory=" + referralHistory + ", notification=" + notification + "]";
	}
}