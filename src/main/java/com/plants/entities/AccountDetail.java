package com.plants.entities;

public class AccountDetail {
	
	private String agentIdPk;
	private String accHolderName;
    private String accNumber;
    private String bankName;
    private String ifscCode;
    private String bankPassBookImage;
    
	public String getAgentIdPk() {
		return agentIdPk;
	}
	public void setAgentIdPk(String agentIdPk) {
		this.agentIdPk = agentIdPk;
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
	@Override
	public String toString() {
		return "AccountDetail [AgentIdPk=" + agentIdPk + ", accHolderName=" + accHolderName + ", accNumber=" + accNumber
				+ ", bankName=" + bankName + ", ifscCode=" + ifscCode + ", bankPassBookImage=" + bankPassBookImage
				+ "]";
	}
    
    
}
