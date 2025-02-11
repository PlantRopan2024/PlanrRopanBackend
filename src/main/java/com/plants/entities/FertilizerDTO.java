package com.plants.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FertilizerDTO {
	
	private String fertilizerName;
    private String amount;
    @JsonProperty("Kg")
    private String Kg;

    // Getters and Setters
    public String getFertilizerName() {
        return fertilizerName;
    }

    public void setFertilizerName(String fertilizerName) {
        this.fertilizerName = fertilizerName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

	public String getKg() {
		return Kg;
	}

	public void setKg(String kg) {
		Kg = kg;
	}

	@Override
	public String toString() {
		return "FertilizerDTO [fertilizerName=" + fertilizerName + ", amount=" + amount + ", Kg=" + Kg + "]";
	}

	
    
}
