package com.plants.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentJsonRequest {
	
	private AgentPersonalDetails agentPersonalDetails;
	
    private AadharIdentityDetail aadharIdentityDetail;
    
    private AccountDetail accountDetail;
    
	public AgentPersonalDetails getAgentPersonalDetails() {
		return agentPersonalDetails;
	}
	public void setAgentPersonalDetails(AgentPersonalDetails agentPersonalDetails) {
		this.agentPersonalDetails = agentPersonalDetails;
	}
	public AadharIdentityDetail getAadharIdentityDetail() {
		return aadharIdentityDetail;
	}
	public void setAadharIdentityDetail(AadharIdentityDetail aadharIdentityDetail) {
		this.aadharIdentityDetail = aadharIdentityDetail;
	}
	public AccountDetail getAccountDetail() {
		return accountDetail;
	}
	public void setAccountDetail(AccountDetail accountDetail) {
		this.accountDetail = accountDetail;
	}
	@Override
	public String toString() {
		return "AgentJsonRequest [agentPersonalDetails=" + agentPersonalDetails + ", aadharIdentityDetail="
				+ aadharIdentityDetail + ", accountDetail=" + accountDetail + "]";
	}
    
    
}
