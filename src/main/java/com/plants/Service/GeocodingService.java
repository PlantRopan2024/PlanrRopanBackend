package com.plants.Service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingService {
	
	 private static final String API_KEY = "AIzaSyBR9n1842MV-SwPuUJRIuDDu_C7hcem93k";
	    private static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json";

	    public String getAddressFromCoordinates(double latitude, double longitude) {
	        RestTemplate restTemplate = new RestTemplate();

	        String url = UriComponentsBuilder.fromHttpUrl(GEOCODING_URL)
	                .queryParam("latlng", latitude + "," + longitude)
	                .queryParam("key", API_KEY)
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
	
}
