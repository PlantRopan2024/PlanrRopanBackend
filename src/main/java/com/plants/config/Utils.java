package com.plants.config;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.imageio.ImageIO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;

public class Utils {

	public static String saveImgFile(MultipartFile file, String baseDirectory,String subDirectoryFolderName,String fileName) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("File is empty or null");
        }
        
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IOException("Unknown file type");
        }
        
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IOException("Invalid file name");
        }
        
        Path directoryPath = findOrCreateFolder(baseDirectory, subDirectoryFolderName);
        Path filePath = directoryPath.resolve(fileName);
        if (contentType.startsWith("image/")) {
            return processImage(file, filePath);
        } else if ("application/pdf".equals(contentType)) {
            return processPDF(file, filePath);
        } else {
            throw new IOException("Unsupported file type: " + contentType);
        }
    }

    /**
     * Finds an existing folder or creates a new one based on the image name.
     *
     * @param baseDirectory     The main directory where folders are created
     * @param fileNameWithoutExt The name of the file without an extension (used as folder name)
     * @return Path of the folder
     * @throws IOException If folder creation fails
     */
    private static Path findOrCreateFolder(String baseDirectory, String subDirectoryFolderName) throws IOException {
        Path folderPath = Paths.get(baseDirectory, subDirectoryFolderName);
        File directory = folderPath.toFile();
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("Failed to create directory: " + folderPath);
            }
        }
        return folderPath;
    }

    /**
     * Processes an image by compressing and saving it.
     *
     * @param file     The image file
     * @param filePath The path where the processed image will be saved
     * @return The URI of the saved image
     * @throws IOException If an error occurs during image processing
     */
    private static String processImage(MultipartFile file, Path filePath) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            throw new IOException("Invalid image file");
        }
        int targetWidth = originalImage.getWidth() / 2;
        int targetHeight = originalImage.getHeight() / 2;
        BufferedImage compressedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = compressedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();

        File outputFile = filePath.toFile();
        ImageIO.write(compressedImage, "jpg", outputFile);

        return filePath.toUri().toString();
    }

    /**
     * Saves a PDF file in the given directory.
     *
     * @param file     The PDF file
     * @param filePath The path where the PDF will be saved
     * @return The URI of the saved PDF
     * @throws IOException If an error occurs during file saving
     */
    private static String processPDF(MultipartFile file, Path filePath) throws IOException {
        try {
            Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return filePath.toUri().toString();
        } catch (IOException e) {
            throw new IOException("Error saving PDF file", e);
        }
    }
    
    public static ResponseEntity<Resource> getPathFileResponse(String baseDirectory,String folderName, String fileName) {
        try {
            Path filePath = Paths.get(baseDirectory, folderName, fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
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
	        String base64Image = Base64.getEncoder().encodeToString(imageDataName);
	        return "data:image/png;base64," + base64Image;
	    } catch (Exception e) {
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

    public static Map<String, Object> paganationInApi(int size, int page, int totalElements) {
        if (size <= 0) size = 15;
        if (page < 0) page = 0;
        int totalPages = (int) Math.ceil((double) totalElements / size);
        if (page >= totalPages && totalPages > 0) {
            page = totalPages - 1;
        }
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        Map<String, Object> paginationData = new HashMap<>();
        paginationData.put("startIndex", startIndex);
        paginationData.put("endIndex", endIndex);
        paginationData.put("totalPages", totalPages);
        paginationData.put("currentPage", page);
        return paginationData;
    }

}
