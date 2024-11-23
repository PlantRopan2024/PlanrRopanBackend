package com.plants.customer.Service;

import org.springframework.stereotype.Service;

@Service
public class LocationService {
	
	private static final double EARTH_RADIUS_KM = 6371.0;  // Earth's radius in kilometers
	private static final double RADIUS_KM = 5.0;  // 5 km range for checking proximity

    // Method to calculate distance using the Haversine formula
    public double calculateDistance(double customerLat, double customerLon, double agentLat, double agentLon) {
        double latDistance = Math.toRadians(agentLat - customerLat);
        double lonDistance = Math.toRadians(agentLon - customerLon);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(customerLat)) * Math.cos(Math.toRadians(agentLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;

        return distance;  // Distance in kilometers
    }

    // Method to check if distance is within a certain radius (e.g., 5 km)
    public boolean isWithinRange(double customerLat, double customerLon, double agentLat, double agentLon) {
        double distance = calculateDistance(customerLat, customerLon, agentLat, agentLon);
        System.out.println("Calculated distance: " + distance + " km");
        return distance <= RADIUS_KM;  // True if within 5 km range
    }
}

