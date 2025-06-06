package com.plants.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	//@Value("${app.name}")
	//private String appName;
	
	@GetMapping("/login")
	public  String login() {
		return "login";
	}
	
	@GetMapping("/userhome")
	public  String userhome() {
		return "userhome";
	}
	
	@GetMapping("/planManage")
	public  String planManage() {
		return "planManage";
	}
	@GetMapping("/addPlan")
	public  String addPlan() {
		return "addPlan";
	}
	@GetMapping("/AdminDashboard")
	public  String AdminDashboard() {
		return "AdminDashboard";
	}
	
	@GetMapping("/agentDetailPage.html")
	public  String agentDetailPage() {
		return "agentDetailPage.html";
	}
	@GetMapping("/agentVerfication")
	public  String agentVerfication(){
		return "agentVerfication";
	}
	
	@GetMapping("/test")
	public  String test(){
		return "test";
	}
	
	@GetMapping("/ImageUpload")
	public String ImageUpload() {
		return "ImageUpload";
	}
	@GetMapping("/logoutUser")
    public String logout(HttpServletRequest request) {
	 System.out.println(" logout");
        HttpSession session = request.getSession(false); // Get session, if exists
        if (session != null) {
            session.invalidate(); // Invalidate session
        }
        return "redirect:/login";
    }
	@GetMapping("/addOffers")
	public String addOffers() {
		return "addOffers";
	}
	
	@GetMapping("/order")
	public String orders() {
		return "order";
	}
	
	@GetMapping("/viewService")
	public String viewService() {
		return "viewService";
	}
	
	@GetMapping("/viewPlans")
	public String viewPlans() {
		return "viewPlans";
	}
	
	@GetMapping("/includingServices")
	public String includingServices() {
		return "includingServices";
	}
	
	@GetMapping("/Role")
	public String role() {
		return "Role";
	}
	
	@GetMapping("/addUsers")
	public String addUsers() {
		return "addUsers";
	}
	
	@GetMapping("/SubRole")
	public String SubRole() {
		return "SubRole";
	}
	
	@GetMapping("/NotifyTemplate")
	public String NotifyTemplate() {
		return "NotifyTemplate";
	}
	
	@GetMapping("/Blog")
	public String Blog() {
		return "Blog";
	}
	
	@GetMapping("/Report")
	public String Report() {
		return "Report";
	}
	
	@GetMapping("/SendNotify")
	public String SendNotify() {
		return "SendNotify";
	}
	
	@GetMapping("/orderHistory")
	public String orderHistory() {
		return "orderHistory";
	}
	
	@GetMapping("/viewCityOrder")
	public String viewCityOrder() {
		return "viewCityOrder";
	}

	@GetMapping("/viewState")
	public String viewState() {
		return "viewState";
	}
	
	@GetMapping("/viewCity")
	public String viewCity() {
		return "viewCity";
	}
	
	
}
