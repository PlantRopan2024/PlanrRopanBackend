package com.plants.config;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.util.JSONPObject;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
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
    
    public static ResponseEntity<String> getImageUrl(String baseDirectory, String folderName, String fileName, HttpServletRequest request) {
        try {
            Path filePath = Paths.get(baseDirectory, folderName, fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            System.out.println(" resource   -- " + resource);

            System.out.println(" filePath   -- " + filePath);
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }

            String fileUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + "/uploads/" + folderName + "/" + fileName;

            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating file URL");
        }
    }


	public static String findImgPath(String baseDirectory, String folderName, String fileName) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/"+baseDirectory+"/"+folderName).path(fileName).toUriString();
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
    
    public static ResponseEntity<Map<String, Object>> createErrorResponse(Map<String, Object> response, String message) {
	    response.put("status", false);
	    response.put("message", message);
	    return ResponseEntity.ok(response);
	}
    
    public static ResponseEntity<Map<String, Object>> createSuccessResponse(Map<String, Object> response, String message) {
	    response.put("status", true);
	    response.put("message", message);
	    return ResponseEntity.ok(response);
	}
    
    public static String getAddressFromCoordinates(double latitude, double longitude,String apiKeyGoogle,String urlAddressGoogle) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(urlAddressGoogle)
                .queryParam("latlng", latitude + "," + longitude)
                .queryParam("key", apiKeyGoogle)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("results")) {
            var results = (java.util.List<Map<String, Object>>) response.get("results");
            if (!results.isEmpty()) {
                return (String) results.get(0).get("formatted_address");
            }
        }
        return "Address not found";
    }
    
    public static Map<String, Object> getCoordinates(String getLatidudeOrLongitutdeUrl, String apiKey, String address) {
        Map<String, Object> result = new HashMap<>();
        try {
            String formattedAddress = address.replace(" ", "+");
            String urlString = getLatidudeOrLongitutdeUrl + "=" + formattedAddress + "&key=" + apiKey;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON Response
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.getString("status").equals("OK")) {
                JSONArray results = jsonResponse.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject location = results.getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location");

                    double latitude = location.getDouble("lat");
                    double longitude = location.getDouble("lng");

                    result.put("latitude", latitude);
                    result.put("longitude", longitude);
                    System.out.println("Latitude: " + latitude);
                    System.out.println("Longitude: " + longitude);
                } else {
                    System.out.println("No results found.");
                }
            } else {
                System.out.println("Error: " + jsonResponse.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static String decimalFormatString(double value) {
        return String.format("%.2f", value);
    }

    public static double decimalFormat(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }

    /**
	 * Builds pagination URLs for navigation.
	 */
	public static Map<String, String> buildPaginationUrls(String baseUrl, int currentPage, int totalPages, int pageSize,List<Map<String, Object>> dataList) {
	    Map<String, String> paginationUrls = new HashMap<>();
	    
	    if(dataList.isEmpty()) {
	    	paginationUrls.put("first_page_url", baseUrl + "?pageNumber=1&pageSize=" + pageSize);
	 	    paginationUrls.put("last_page_url", baseUrl + "?pageNumber=1&pageSize=" + pageSize);
	    }else {
	    	paginationUrls.put("first_page_url", totalPages > 0 ? baseUrl + "?pageNumber=1&pageSize=" + pageSize : null);
	 	    paginationUrls.put("last_page_url", totalPages > 0 ? baseUrl + "?pageNumber=" + totalPages + "&pageSize=" + pageSize : null);
	    }
	    paginationUrls.put("next_page_url", currentPage < totalPages ? baseUrl + "?pageNumber=" + (currentPage + 1) + "&pageSize=" + pageSize : null);
	    paginationUrls.put("prev_page_url", currentPage > 1 ? baseUrl + "?pageNumber=" + (currentPage - 1) + "&pageSize=" + pageSize : null);

	    return paginationUrls;
	}

	
	/**
	 * Builds a dynamic pagination response.
	 */
	public static <T> Map<String, Object> buildPaginationResponse(Page<T> pageData, String baseUrl, int pageSize, List<Map<String, Object>> dataList) {
	    int currentPage = pageData.getNumber() + 1;
	    int totalPages = pageData.getTotalPages();
	    long totalItems = pageData.getTotalElements();

	    Map<String, Object> pagination = new HashMap<>();
	    pagination.put("current_page", currentPage);
	    pagination.put("data", dataList);
	    pagination.put("first_page_url", baseUrl + "?pageNumber=1&pageSize=" + pageSize);
	    pagination.put("last_page", totalPages);
	    pagination.putAll(buildPaginationUrls(baseUrl, currentPage, totalPages, pageSize,dataList));
	    pagination.put("path", baseUrl);
	    pagination.put("per_page", pageSize);
	    pagination.put("from", ((currentPage - 1) * pageSize) + 1);
	    pagination.put("to", Math.min(currentPage * pageSize, totalItems));
	    pagination.put("total", totalItems);

	    return pagination;
	}
	


	public static Map<String, Object> buildPaginationResponse(List<Map<String, Object>> ordersList, String baseUrl,int pageNumber,
			int pageSize, List<Map<String, Object>> ordersList2) {
		 int totalItems = ordersList.size();
		    int totalPages = (int) Math.ceil((double) totalItems / pageSize);
		    int currentPage = Math.max(1, Math.min(pageNumber, totalPages));

		    int fromIndex = (currentPage - 1) * pageSize;
		    int toIndex = Math.min(fromIndex + pageSize, totalItems);
		    
		    List<Map<String, Object>> paginatedList = ordersList.subList(fromIndex, toIndex);

		    Map<String, Object> pagination = new HashMap<>();
		    pagination.put("current_page", currentPage);
		    pagination.put("data", paginatedList);
		    pagination.put("first_page_url", totalPages > 0 ? baseUrl + "?pageNumber=1&pageSize=" + pageSize : null);
		    pagination.put("last_page", totalPages);
		    pagination.putAll(buildPaginationUrls(baseUrl, currentPage, totalPages, pageSize,paginatedList));
		    pagination.put("path", baseUrl);
		    pagination.put("per_page", pageSize);
		    pagination.put("from", totalItems == 0 ? 0 : fromIndex + 1);
		    pagination.put("to", totalItems == 0 ? 0 : toIndex);
		    pagination.put("total", totalItems);
		return pagination;
	}
}
