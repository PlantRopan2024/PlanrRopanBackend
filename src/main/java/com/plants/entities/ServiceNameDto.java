package com.plants.entities;

import org.springframework.web.multipart.MultipartFile;

public class ServiceNameDto {

	private String serviceName;
	private String servicePk;

	private MultipartFile serviceImage;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServicePk() {
		return servicePk;
	}

	public void setServicePk(String servicePk) {
		this.servicePk = servicePk;
	}

	public MultipartFile getServiceImage() {
		return serviceImage;
	}

	public void setServiceImage(MultipartFile serviceImage) {
		this.serviceImage = serviceImage;
	}

}
