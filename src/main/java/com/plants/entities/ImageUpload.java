package com.plants.entities;

import java.util.Arrays;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class ImageUpload {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primaryKey;

	private String fileName;

	private boolean isActive;

	private String type;

	@Lob
	@Basic(fetch = FetchType.LAZY, optional = true)
	@Column(name = "imagedata")
	byte[] imageData;

	public ImageUpload(int primaryKey, String fileName, boolean isActive, String type, byte[] imageData) {
		super();
		this.primaryKey = primaryKey;
		this.fileName = fileName;
		this.isActive = isActive;
		this.type = type;
		this.imageData = imageData;
	}

	public ImageUpload() {
		// TODO Auto-generated constructor stub
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	@Override
	public String toString() {
		return "ImageUpload [primaryKey=" + primaryKey + ", fileName=" + fileName + ", isActive=" + isActive + ", type="
				+ type + ", imageData=" + Arrays.toString(imageData) + "]";
	}

}
