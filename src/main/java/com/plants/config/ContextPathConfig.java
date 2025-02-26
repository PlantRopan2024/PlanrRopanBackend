package com.plants.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletContext;

@RestController
public class ContextPathConfig {

	@Value("${server.servlet.context-path}") // Get context path from application.properties
	private String contextPath;

	@GetMapping("/api/contextPath")
	public String getContextPath(ServletContext servletContext) {
		System.out.println(" servlet conext name --- " + servletContext.getContextPath());
		return servletContext.getContextPath(); // Returns dynamically set context path
	}
}
