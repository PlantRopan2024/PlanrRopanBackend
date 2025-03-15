package com.plants.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plants.Service.GeocodingService;

@RestController
@RequestMapping("/geocode")
public class GeocodingController {
	
	private final GeocodingService geocodingService;

    public GeocodingController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping("/address")
    public String getAddress(@RequestParam double latitude, @RequestParam double longitude) {
        return geocodingService.getAddressFromCoordinates(latitude, longitude);
    }
}
