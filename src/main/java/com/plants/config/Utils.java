package com.plants.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Utils {

	public static String saveImgFile(MultipartFile file) throws IOException {	
		String externalDirectory = "src/main/resources/static/uploadImages";
		File directory = new File(externalDirectory);
		
		if (!directory.exists()) {
			directory.mkdirs();
		}
		
		if (file == null || file.isEmpty()) {
	        throw new IOException("File is empty");
	    }

	    // Create a Path object with the directory path and the file name
	    Path filePath = Paths.get(externalDirectory, file.getOriginalFilename());

	    // Copy the file to the specified location
	    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	    file.getInputStream().close();

	    return ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/uploadImages/")
	            .path(file.getOriginalFilename())
	            .toUriString();
	}
	
	public static String findImgPath(String file) {
		return ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/uploadImages/")
	            .path(file)
	            .toUriString();
	}
}
