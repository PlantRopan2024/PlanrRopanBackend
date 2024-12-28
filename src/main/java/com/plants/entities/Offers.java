package com.plants.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Offers {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int primaryKey;
	
	private String title;
	private String description;
	private String validity;
	private String discount;
	private String conditions;
	private boolean isActive;
	private String TypeID;
	
	public Offers(String title, String description, String validity, String discount, String conditions,
			boolean isActive, String typeID) {
		super();
		this.title = title;
		this.description = description;
		this.validity = validity;
		this.discount = discount;
		this.conditions = conditions;
		this.isActive = isActive;
		TypeID = typeID;
	}
	public Offers() {
		
	}
	
	public int getPrimarykey() {
		return primaryKey;
	}
	public void setPrimarykey(int primarykey) {
		this.primaryKey = primarykey;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getConditions() {
		return conditions;
	}
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getTypeID() {
		return TypeID;
	}
	public void setTypeID(String typeID) {
		TypeID = typeID;
	}
	
	@Override
	public String toString() {
		return "Offers [primarykey=" + primaryKey + ", title=" + title + ", description=" + description + ", validity="
				+ validity + ", discount=" + discount + ", conditions=" + conditions + ", isActive=" + isActive
				+ ", TypeID=" + TypeID + "]";
	}
	
	
	
	

}
