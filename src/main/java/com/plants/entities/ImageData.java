package com.plants.entities;

import java.util.Arrays;

import org.hibernate.annotations.Type;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "ImageData")
public class ImageData {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String type;
    
    @Lob
    @Basic(fetch=FetchType.LAZY, optional=true)
    @Column(name = "imagedata")
    byte[] imageData;

	public ImageData() {
		super();
	}

	public ImageData(Long id, String name, String type, byte[] imageData) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.imageData = imageData;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "ImageData [id=" + id + ", name=" + name + ", type=" + type + ", imageData=" + Arrays.toString(imageData)
				+ "]";
	}
    
    
}