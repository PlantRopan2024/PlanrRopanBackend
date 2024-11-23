package com.plants.entities;

public class AadharIdentityDetail {
	
	private String agentIdPk;
	private String aadhaarNumber;
	private String aadharImgFrontSide;
	private String aadharImgBackSide;
	
	public String getAgentIdPk() {
		return agentIdPk;
	}
	public void setAgentIdPk(String agentIdPk) {
		this.agentIdPk = agentIdPk;
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
	@Override
	public String toString() {
		return "AadharIdentityDetail [AgentIdPk=" + agentIdPk + ", aadhaarNumber=" + aadhaarNumber
				+ ", aadharImgFrontSide=" + aadharImgFrontSide + ", aadharImgBackSide=" + aadharImgBackSide + "]";
	}
	
	
	
}
