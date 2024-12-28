package com.plants.customer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plants.Dao.ImageUploadRepo;
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

        try {
            // Save image and get URL
            String selfieImageUrl = Utils.saveImgFile(file);

            // Save to the database
            ImageUpload image = new ImageUpload();
            image.setFileName(file.getOriginalFilename());
            image.setActive(true);
            this.imageUploadRepo.save(image);

            return ResponseEntity.status(HttpStatus.OK).body("Image uploaded successfully ");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading the image!");
        }
    }

    @GetMapping("/getImage")
    public ResponseEntity<List<String>> getImages() {
        List<ImageUpload> images = imageUploadRepo.findAll();
        List<String> updatedImages = new ArrayList<>();

        for (ImageUpload image : images) {
            String imgPath = Utils.findImgPath(image.getFileName());
            updatedImages.add(imgPath); // Add resolved image path to the response
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedImages);
    }
}
