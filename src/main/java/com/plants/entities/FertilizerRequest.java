package com.plants.entities;

public class FertilizerRequest {
	
	 private String name;
	 private int quantity;
	 private double price;
	public FertilizerRequest(String name, int quantity, double price) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "FertilizerRequest [name=" + name + ", quantity=" + quantity + ", price=" + price + "]";
	}
	 
	 

}
