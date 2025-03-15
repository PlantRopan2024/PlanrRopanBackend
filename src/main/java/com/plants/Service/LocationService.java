package com.plants.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LocationService {
		
	@Value("${earth.Radius.Km}")
	private double earthRadiusKm;
	
	@Value("${speed.Km.Per.Hour}")
	private double speedKmPerHour;
	
	@Value("${radius.Km}")
	private double radiusKm;

    public double calculateDistance(double customerLat, double customerLon, double agentLat, double agentLon) {
        double latDistance = Math.toRadians(agentLat - customerLat);
        double lonDistance = Math.toRadians(agentLon - customerLon);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(customerLat)) * Math.cos(Math.toRadians(agentLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadiusKm * c;

        return distance;  
    }

    public boolean isWithinRange(double customerLat, double customerLon, double agentLat, double agentLon) {
        double distance = calculateDistance(customerLat, customerLon, agentLat, agentLon);
        return distance <= radiusKm;
    }
    
    public double estimateArrivalTime(double customerLat, double customerLon, double agentLat, double agentLon) {
        double distance = calculateDistance(customerLat, customerLon, agentLat, agentLon); // Get distance in km
        if (distance > radiusKm) {
            return -1;
        }
        return (distance / speedKmPerHour) * 60;
    }
    
  
    
    
}

