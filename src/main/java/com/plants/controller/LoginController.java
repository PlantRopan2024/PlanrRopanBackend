package com.plants.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.plants.Dao.userDao;
import com.plants.entities.user;

import org.springframework.ui.Model;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {
	
	//@Autowired
	//private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	userDao userdao;
	
	@RequestMapping("/sign")
	public  String sign() {
			
		return "sign";
	}

	
	@RequestMapping("/test")
	public  String test() {
		
		return "test";
	}
	
	@RequestMapping("/loginUser")
	public ModelAndView loginUser(@ModelAttribute user Usr ,Model model, HttpSession session) {
			try {
				user fectchUsernameAndPassword= this.userdao.finduserNameAndPassword(Usr.getUsername() , Usr.getPassword());
				
				if(fectchUsernameAndPassword != null && fectchUsernameAndPassword.getUserrole().equals("ADMIN") ) {
					System.out.println("ADMIN LOGIN");
					session.setAttribute("sessionCreated", fectchUsernameAndPassword);
					return new ModelAndView("redirect:/AdminDashboard");
				}else if(fectchUsernameAndPassword != null && fectchUsernameAndPassword.getUserrole().equals("USER")) {
					System.out.println("USER LOGIN");
					session.setAttribute("sessionCreated", fectchUsernameAndPassword);
					return new ModelAndView("redirect:/dashboard");
				}else if(fectchUsernameAndPassword != null && fectchUsernameAndPassword.getUserrole().equals("STUDENT")) {
					System.out.println("STUDENT LOGIN");
					session.setAttribute("sessionCreated", fectchUsernameAndPassword);
					return new ModelAndView("redirect:/studentdashboard");
				} else {
				ModelAndView modelAndView = new ModelAndView("login");
		        modelAndView.addObject("error", "Invalid username or password");
		        return modelAndView;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new ModelAndView("login");	
			}		
	}
	
	
	 

}
