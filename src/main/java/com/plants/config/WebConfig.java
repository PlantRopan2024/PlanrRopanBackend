package com.plants.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/**")
	            .allowedOrigins("http://localhost:8080","http://localhost:4200" )  // Add both local and deployed URLs
	            .allowedMethods("GET", "POST", "PUT", "DELETE")
	            .allowedHeaders("*") // Allow all headers
                .allowCredentials(true);
	}
}

