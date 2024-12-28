package com.plants.config;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

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
}
