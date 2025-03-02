package com.plants.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
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
	private LocalDate validity;    
	@Column(unique = true, nullable = false)
	private String offerCode;
	private String discount;
	@Column(columnDefinition = "TEXT")
	private String conditions;
	public boolean isNewActive;
	private String TypeID;
	
	public Offers(String title, String description, LocalDate validity, String discount, String conditions,
			 String typeID,boolean isNewActive,String offerCode) {
		super();
		this.title = title;
		this.description = description;
		this.validity = validity;
		this.discount = discount;
		this.conditions = conditions;
		this.TypeID = typeID;
		this.isNewActive=isNewActive;
		this.offerCode=offerCode;
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
	public LocalDate getValidity() {
		return validity;
	}
	public void setValidity(LocalDate validity) {
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
	public String getTypeID() {
		return TypeID;
	}
	public void setTypeID(String typeID) {
		TypeID = typeID;
	}
	public String getOfferCode() {
		return offerCode;
	}
	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}
	public void setNewActive(boolean isNewActive) {
		this.isNewActive = isNewActive;
	}
	@Override
	public String toString() {
		return "Offers [primarykey=" + primaryKey + ", title=" + title + ", description=" + description + ", validity="
				+ validity + ", discount=" + discount + ", conditions=" + conditions +  ", TypeID=" + TypeID + "]";
	}
}
