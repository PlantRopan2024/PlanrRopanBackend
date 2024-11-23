package com.plants.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CusOrderCreate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primarykey;
	
	
	private int cusPkId;
	private String CusfirstName;
	private String CuslastName;
	private String CusEmailId;
	private String CusmobileNumber;
	private String CusAddress;
	private String Cuslatitude;
	private String Cuslongitude;
	
	private int PlanPkId;
	private String plansRs;
	private String timeDuration;
	private String servicesName;
	private String planType;
	private String planPacks;
	private String uptoPots;
	
	private int AgentIDPk;
	private String AgenFirstName;
	private String AgenLastName;
	private String AgenMobileNumber;
	private String AgenAddress;
	private String Agenlatitude;
	private String Agenlongitude;
	
	public CusOrderCreate() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CusOrderCreate(int primarykey, int cusPkId, String cusfirstName, String cuslastName, String cusEmailId,
			String cusmobileNumber, String cusAddress, String cuslatitude, String cuslongitude, int planPkId,
			String plansRs, String timeDuration, String servicesName, String planType, String planPacks,
			String uptoPots, int agentIDPk, String agenFirstName, String agenLastName, String agenMobileNumber,
			String agenAddress, String agenlatitude, String agenlongitude) {
		super();
		this.primarykey = primarykey;
		this.cusPkId = cusPkId;
		CusfirstName = cusfirstName;
		CuslastName = cuslastName;
		CusEmailId = cusEmailId;
		CusmobileNumber = cusmobileNumber;
		CusAddress = cusAddress;
		Cuslatitude = cuslatitude;
		Cuslongitude = cuslongitude;
		PlanPkId = planPkId;
		this.plansRs = plansRs;
		this.timeDuration = timeDuration;
		this.servicesName = servicesName;
		this.planType = planType;
		this.planPacks = planPacks;
		this.uptoPots = uptoPots;
		AgentIDPk = agentIDPk;
		AgenFirstName = agenFirstName;
		AgenLastName = agenLastName;
		AgenMobileNumber = agenMobileNumber;
		AgenAddress = agenAddress;
		Agenlatitude = agenlatitude;
		Agenlongitude = agenlongitude;
	}
	public int getPrimarykey() {
		return primarykey;
	}
	public void setPrimarykey(int primarykey) {
		this.primarykey = primarykey;
	}
	public int getCusPkId() {
		return cusPkId;
	}
	public void setCusPkId(int cusPkId) {
		this.cusPkId = cusPkId;
	}
	public String getCusfirstName() {
		return CusfirstName;
	}
	public void setCusfirstName(String cusfirstName) {
		CusfirstName = cusfirstName;
	}
	public String getCuslastName() {
		return CuslastName;
	}
	public void setCuslastName(String cuslastName) {
		CuslastName = cuslastName;
	}
	public String getCusEmailId() {
		return CusEmailId;
	}
	public void setCusEmailId(String cusEmailId) {
		CusEmailId = cusEmailId;
	}
	public String getCusmobileNumber() {
		return CusmobileNumber;
	}
	public void setCusmobileNumber(String cusmobileNumber) {
		CusmobileNumber = cusmobileNumber;
	}
	public String getCusAddress() {
		return CusAddress;
	}
	public void setCusAddress(String cusAddress) {
		CusAddress = cusAddress;
	}
	public String getCuslatitude() {
		return Cuslatitude;
	}
	public void setCuslatitude(String cuslatitude) {
		Cuslatitude = cuslatitude;
	}
	public String getCuslongitude() {
		return Cuslongitude;
	}
	public void setCuslongitude(String cuslongitude) {
		Cuslongitude = cuslongitude;
	}
	public int getPlanPkId() {
		return PlanPkId;
	}
	public void setPlanPkId(int planPkId) {
		PlanPkId = planPkId;
	}
	public String getPlansRs() {
		return plansRs;
	}
	public void setPlansRs(String plansRs) {
		this.plansRs = plansRs;
	}
	public String getTimeDuration() {
		return timeDuration;
	}
	public void setTimeDuration(String timeDuration) {
		this.timeDuration = timeDuration;
	}
	public String getServicesName() {
		return servicesName;
	}
	public void setServicesName(String servicesName) {
		this.servicesName = servicesName;
	}
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
	public String getPlanPacks() {
		return planPacks;
	}
	public void setPlanPacks(String planPacks) {
		this.planPacks = planPacks;
	}
	public String getUptoPots() {
		return uptoPots;
	}
	public void setUptoPots(String uptoPots) {
		this.uptoPots = uptoPots;
	}
	public int getAgentIDPk() {
		return AgentIDPk;
	}
	public void setAgentIDPk(int agentIDPk) {
		AgentIDPk = agentIDPk;
	}
	public String getAgenFirstName() {
		return AgenFirstName;
	}
	public void setAgenFirstName(String agenFirstName) {
		AgenFirstName = agenFirstName;
	}
	public String getAgenLastName() {
		return AgenLastName;
	}
	public void setAgenLastName(String agenLastName) {
		AgenLastName = agenLastName;
	}
	public String getAgenMobileNumber() {
		return AgenMobileNumber;
	}
	public void setAgenMobileNumber(String agenMobileNumber) {
		AgenMobileNumber = agenMobileNumber;
	}
	public String getAgenAddress() {
		return AgenAddress;
	}
	public void setAgenAddress(String agenAddress) {
		AgenAddress = agenAddress;
	}
	public String getAgenlatitude() {
		return Agenlatitude;
	}
	public void setAgenlatitude(String agenlatitude) {
		Agenlatitude = agenlatitude;
	}
	public String getAgenlongitude() {
		return Agenlongitude;
	}
	public void setAgenlongitude(String agenlongitude) {
		Agenlongitude = agenlongitude;
	}
	@Override
	public String toString() {
		return "CusOrderCreate [primarykey=" + primarykey + ", cusPkId=" + cusPkId + ", CusfirstName=" + CusfirstName
				+ ", CuslastName=" + CuslastName + ", CusEmailId=" + CusEmailId + ", CusmobileNumber=" + CusmobileNumber
				+ ", CusAddress=" + CusAddress + ", Cuslatitude=" + Cuslatitude + ", Cuslongitude=" + Cuslongitude
				+ ", PlanPkId=" + PlanPkId + ", plansRs=" + plansRs + ", timeDuration=" + timeDuration
				+ ", servicesName=" + servicesName + ", planType=" + planType + ", planPacks=" + planPacks
				+ ", uptoPots=" + uptoPots + ", AgentIDPk=" + AgentIDPk + ", AgenFirstName=" + AgenFirstName
				+ ", AgenLastName=" + AgenLastName + ", AgenMobileNumber=" + AgenMobileNumber + ", AgenAddress="
				+ AgenAddress + ", Agenlatitude=" + Agenlatitude + ", Agenlongitude=" + Agenlongitude + "]";
	}
}
