package com.plants.Service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import com.plants.Dao.ImageUploadRepo;
import com.plants.Dao.StorageRepository;
import com.plants.config.Utils;
import com.plants.entities.ImageData;
import com.plants.entities.ImageUpload;


@Service
public class StorageService {

	@Autowired
	private StorageRepository repository;
	
	@Autowired
	private ImageUploadRepo imageUploadRepo;

	public String uploadImage(MultipartFile file) throws IOException {
		ImageData imageData = new ImageData(null, // ID will be auto-generated
				file.getOriginalFilename(), file.getContentType(), Utils.compressImage(file.getBytes()),"ajeet");
		ImageData savedImageData = repository.save(imageData);
		if (savedImageData != null) {
			return "File uploaded successfully: " + file.getOriginalFilename();
		}
		return null;
	}
	
	public String uploadImageUI(MultipartFile file) throws IOException {
		ImageUpload image = new ImageUpload();
        image.setFileName(file.getOriginalFilename());
        image.setActive(true);
        image.setType(file.getContentType());
        image.setImageData(Utils.compressImage(file.getBytes()));
        ImageUpload  savedImageData =this.imageUploadRepo.save(image);

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
