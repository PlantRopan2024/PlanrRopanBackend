package com.plants.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LocationService {
	
	private static final double EARTH_RADIUS_KM = 6371.0;  // Earth's radius in kilometers
	private static final double RADIUS_KM = 3.0;  // 3 km range for checking proximity
    private static final String GOOGLE_API_KEY = "AIzaSyBIrx-pgB-AOQOAEyMvpmhYvisaY6kSwgw"; // Replace with your API key

    public double calculateDistance(double customerLat, double customerLon, double agentLat, double agentLon) {
        double latDistance = Math.toRadians(agentLat - customerLat);
        double lonDistance = Math.toRadians(agentLon - customerLon);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(customerLat)) * Math.cos(Math.toRadians(agentLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;

        return distance;  
    }

    public boolean isWithinRange(double customerLat, double customerLon, double agentLat, double agentLon) {
        double distance = calculateDistance(customerLat, customerLon, agentLat, agentLon);
        System.out.println("Calculated distance: " + distance + " km");
        return distance <= RADIUS_KM;  // True if within 3 km range
    }
    
    public double estimateArrivalTime(double customerLat, double customerLon, double agentLat, double agentLon, double speedKmPerHour) {
        double distance = calculateDistance(customerLat, customerLon, agentLat, agentLon); // Get distance in km
        if (distance > RADIUS_KM) {
            return -1;
        }
        return (distance / speedKmPerHour) * 60; // Convert time to minutes
    }
    
  /*  public String getAddressFromCoordinates(double latitude, double longitude) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" 
                     + latitude + "," + longitude 
                     + "&key=" + GOOGLE_API_KEY;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        try {
            JSONPObject json = new JSONObject(response);
            if (json.getJSONArray("results").length() > 0) {
                return json.getJSONArray("results").getJSONObject(0).getString("formatted_address");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Address not found";
    }  */
    
    public String getAddressFromCoordinates(double latitude, double longitude) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" 
                     + latitude + "," + longitude 
                     + "&key=" + GOOGLE_API_KEY;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(" response  - " + response);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println(" jsonNode  - " + jsonNode);
            System.out.println(" -(jsonNode.has(\"results\") && jsonNode.get(\"results\").size()--" +  jsonNode.get("results").size());
            if (jsonNode.has("results") && jsonNode.get("results").size() > 0) {
                return jsonNode.get("results").get(0).get("formatted_address").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Address not found";
    }
}

