package com.plants.customer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plants.Dao.ImageUploadRepo;
import com.plants.Service.StorageService;
import com.plants.config.Utils;
import com.plants.entities.ImageUpload;

@RestController
public class ImageUploadController {

    @Autowired
    private ImageUploadRepo imageUploadRepo;

    @PostMapping("/Uploadimage")
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty!");
        }

        // Validate file type and size
        String fileType = file.getContentType();
		/*
		 * if (!List.of("image/jpeg", "image/png").contains(fileType)) { return
		 * ResponseEntity.status(HttpStatus.BAD_REQUEST).
		 * body("Only JPEG and PNG files are allowed!"); }
		 */
		/*
		 * if (file.getSize() > 5 * 1024 * 1024) { // 5MB size limit return
		 * ResponseEntity.status(HttpStatus.BAD_REQUEST).
		 * body("File size exceeds the 5MB limit!"); }
		 */

        try {
            // Call the service to handle the file upload
            String uploadMessage = service.uploadImageUI(file);
            return ResponseEntity.ok(uploadMessage);
        } catch (IOException e) {
            // Log the exception
           // logger.error("Error while uploading image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading the image!");
        }
    }


	/*
	 * @GetMapping("/getImage") public ResponseEntity<List<String>> getImages() {
	 * List<ImageUpload> images = imageUploadRepo.findAll(); List<String>
	 * updatedImages = new ArrayList<>(); for (ImageUpload image : images) { String
	 * imgPath = Utils.findImgPath(image.getFileName()); updatedImages.add(imgPath);
	 * // Add resolved image path to the response } return
	 * ResponseEntity.status(HttpStatus.OK).body(updatedImages); }
	 */
    
    @GetMapping("/getImagelist")
    public ResponseEntity<List<String>> getImageList() {
        List<ImageUpload> images = imageUploadRepo.findAll();

        if (images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }
        // Convert images to Base64 strings
        List<String> imageDataList = images.stream()
            .map(image -> Utils.decompressImage(image.getImageData()))
            .filter(Objects::nonNull)
            .map(Base64.getEncoder()::encodeToString) // Convert byte array to Base64
            .toList();

        return ResponseEntity.ok(imageDataList);
    }


    
    @Autowired
	private StorageService service;

	@PostMapping("/uploadImageaa")
	public ResponseEntity<?> uploadImageaa(@RequestParam("image")MultipartFile file) throws IOException {
		String uploadImage = service.uploadImage(file);
		return ResponseEntity.status(HttpStatus.OK)
				.body(uploadImage);
	}

	/*
	 * @GetMapping("/{fileName}") public ResponseEntity<?>
	 * downloadFile(@PathVariable String fileName) { byte[] fileData =
	 * service.downloadImage(fileName);
	 * 
	 * MediaType fileExtensionName = Utils.getFileExtensionName(fileName);
	 * 
	 * 
	 * return ResponseEntity.status(HttpStatus.OK) .contentType(fileExtensionName)
	 * .body(fileData); }
	 */


}
