package com.plants.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import com.plants.Service.S3Service;
import com.plants.config.Utils;


@RestController
@RequestMapping("/images")
public class FileUploadController {
	
	
	    @Autowired
	    private S3Service s3Service;
	    
	    @Value("${file.upload-dir}")
	    private String uploadDir;

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
	    
	    @PostMapping("/uploadImageServer")
	    public ResponseEntity<String> uploadImageServer(@RequestParam("images") MultipartFile image) {
	        try {
		        String mobileNameFolder = "8081654589";
		        String fileName = "8081654589"+image.getOriginalFilename();
		      String selfieImageUrl = Utils.saveImgFile(image,uploadDir,mobileNameFolder,fileName);
	            return ResponseEntity.ok(selfieImageUrl);
	        } catch (IOException e) {
	            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
	        }
	    }
	    
	    @GetMapping("/downloadFile/{folderName}/{fileName}")
	    public ResponseEntity<Resource> downloadFile(@PathVariable String folderName,@PathVariable String fileName) {
	        return Utils.getPathFileResponse(uploadDir,folderName, fileName);
	    }
	    
//	    @GetMapping("/getAllListImage")
//	    public List<String> toGetList(){
//	    	return this.s3Service.allFiles();
//	    }
	    
}
