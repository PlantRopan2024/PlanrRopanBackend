package com.plants.controller;

import java.io.IOException;
import java.util.List;

import org.checkerframework.common.reflection.qual.GetClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plants.Service.S3Service;


@RestController
@RequestMapping("/images")
public class FileUploadController {
	
	
	    @Autowired
	    private S3Service s3Service;

	    @PostMapping("/upload")
	    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
	        try {
		        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

	            String fileUrl = s3Service.uploadFile(file,fileName);
	            return ResponseEntity.ok(fileUrl);
	        } catch (IOException e) {
	            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
	        }
	    }
	    
	    @GetMapping("/getAllListImage")
	    public List<String> toGetList(){
	    	return this.s3Service.allFiles();
	    }
	    
		/*
		 * @GetMapping("/{getFileNameImage}") public String
		 * getFileNameImage(@PathVariable("getFileNameImage") String fileName) { return
		 * this.s3Service.getFileNameImage(fileName); }
		 */

}
