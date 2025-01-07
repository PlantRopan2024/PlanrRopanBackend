package com.plants.Service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import com.plants.Dao.StorageRepository;
import com.plants.config.Utils;
import com.plants.entities.ImageData;


@Service
public class StorageService {

	@Autowired
	private StorageRepository repository;

	public String uploadImage(MultipartFile file) throws IOException {
		ImageData imageData = new ImageData(null, // ID will be auto-generated
				file.getOriginalFilename(), file.getContentType(), Utils.compressImage(file.getBytes()));
		ImageData savedImageData = repository.save(imageData);
		if (savedImageData != null) {
			return "File uploaded successfully: " + file.getOriginalFilename();
		}
		return null;
	}

	public byte[] downloadImage(String fileName) {
		Optional<ImageData> dbImageData = repository.findByName(fileName);
		byte[] images = Utils.decompressImage(dbImageData.get().getImageData());
		return images;
	}

}
