package com.plants.config;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.imageio.ImageIO;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Utils {

	private static final String UPLOAD_DIR = "src/main/resources/static/uploadImages";

	public static String saveImgFile(MultipartFile file) throws IOException {
		File directory = new File(UPLOAD_DIR);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		if (file == null || file.isEmpty()) {
			throw new IOException("File is empty");
		}

		BufferedImage originalImage = ImageIO.read(file.getInputStream());
		if (originalImage == null) {
			throw new IOException("Invalid image file");
		}

		int targetWidth = originalImage.getWidth() / 2;
		int targetHeight = originalImage.getHeight() / 2;
		Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

		BufferedImage compressedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = compressedImage.createGraphics();
		g2d.drawImage(scaledImage, 0, 0, null);
		g2d.dispose();

		String compressedFileName = "compressed_" + file.getOriginalFilename();
		Path compressedFilePath = Paths.get(UPLOAD_DIR, compressedFileName);
		File outputFile = compressedFilePath.toFile();
		ImageIO.write(compressedImage, "jpg", outputFile);

		// Create a Path object with the directory path and the file name
		// Path filePath = Paths.get(externalDirectory, file.getOriginalFilename());

		// Copy the file to the specified location
		// Files.copy(file.getInputStream(), filePath,
		// StandardCopyOption.REPLACE_EXISTING);

		// file.getInputStream().close();

		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploadImages/")
				.path("compressed_"+file.getOriginalFilename()).toUriString();
	}

	public static String findImgPath(String file) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploadImages/compressed_").path(file).toUriString();
	}
	
	public static MediaType getFileExtensionName(String fileName) {
	    String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
	    switch (fileExtension) {
	        case "png":
	            return MediaType.IMAGE_PNG;
	        case "jpg":
	        case "jpeg":
	            return MediaType.IMAGE_JPEG;
	        case "gif":
	            return MediaType.IMAGE_GIF;
	        case "pdf":
	            return MediaType.APPLICATION_PDF;
	        default:
	            return MediaType.APPLICATION_OCTET_STREAM; // Generic binary stream for unknown types
	    }
	}

	
	public static String resolveImage(byte[] imageDataName) {
	    try {
	        // Fetch the image data from the database

	        // Optionally, convert image data to Base64 or serve it as a downloadable URL
	        String base64Image = Base64.getEncoder().encodeToString(imageDataName);
	        return "data:image/png;base64," + base64Image;

	        // Alternatively, return a downloadable URL
	        // return "/download/" + imageName;
	    } catch (Exception e) {
	        // Log the exception and return a placeholder or error message
	        return "Error resolving image: " + e.getMessage();
	    }
	}
	
	public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }
    
    public static String imageToBase64(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
    }


}
