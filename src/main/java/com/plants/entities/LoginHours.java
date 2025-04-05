package com.plants.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class LoginHours {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int primaryKey;
	
	private double loginHoursCount;
	
	private long hr;
	private long minutes;
	private long seconds;
	
    private String formattedLoginHours; // Store "0 Hour 5 minutes 41 seconds"
	
	 private LocalDateTime loginTime;
	 private LocalDateTime logoutTime;
	    
	 private LocalDateTime createdAt;
	
    @ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_agent_main_id" ,nullable = false)
	private AgentMain agentMain;

	public LoginHours() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginHours(int primaryKey, double loginHoursCount, long hr, long minutes, long seconds,
			String formattedLoginHours, LocalDateTime loginTime, LocalDateTime logoutTime, LocalDateTime createdAt,
			AgentMain agentMain) {
		super();
		this.primaryKey = primaryKey;
		this.loginHoursCount = loginHoursCount;
		this.hr = hr;
		this.minutes = minutes;
		this.seconds = seconds;
		this.formattedLoginHours = formattedLoginHours;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
		this.createdAt = createdAt;
		this.agentMain = agentMain;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public double getLoginHoursCount() {
		return loginHoursCount;
	}

	public void setLoginHoursCount(double loginHoursCount) {
		this.loginHoursCount = loginHoursCount;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}

	public LocalDateTime getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(LocalDateTime logoutTime) {
		this.logoutTime = logoutTime;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public AgentMain getAgentMain() {
		return agentMain;
	}

	public void setAgentMain(AgentMain agentMain) {
		this.agentMain = agentMain;
	}

	public String getFormattedLoginHours() {
		return formattedLoginHours;
	}

	public void setFormattedLoginHours(String formattedLoginHours) {
		this.formattedLoginHours = formattedLoginHours;
	}

	public long getHr() {
		return hr;
	}

	public void setHr(long hr) {
		this.hr = hr;
	}

	public long getMinutes() {
		return minutes;
	}

	public void setMinutes(long minutes) {
		this.minutes = minutes;
	}

	public long getSeconds() {
		return seconds;
	}

	public void setSeconds(long seconds) {
		this.seconds = seconds;
	}
    
    
}
