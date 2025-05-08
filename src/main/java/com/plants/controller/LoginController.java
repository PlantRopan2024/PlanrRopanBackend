package com.plants.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.plants.Dao.userDao;
import com.plants.config.RoleConstants;
import com.plants.entities.Role;
import com.plants.entities.SubRole;
import com.plants.entities.user;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	// @Autowired
	// private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	userDao userdao;

	@RequestMapping("/sign")
	public String sign() {

		return "sign";
	}

	@RequestMapping("/test")
	public String test() {
		return "test";
	}

//	@RequestMapping("/loginUser")
//	public ModelAndView loginUser(@ModelAttribute user Usr ,Model model, HttpSession session) {
//			try {
//				user fectchUsernameAndPassword= this.userdao.finduserNameAndPassword(Usr.getUsername() , Usr.getPassword());
//				
//				if(fectchUsernameAndPassword != null && fectchUsernameAndPassword.getUserrole().equals("ADMIN") ) {
//					System.out.println("ADMIN LOGIN");
//					session.setAttribute("sessionCreated", fectchUsernameAndPassword);
//					return new ModelAndView("redirect:/AdminDashboard");
//				}else if(fectchUsernameAndPassword != null && fectchUsernameAndPassword.getUserrole().equals("USER")) {
//					System.out.println("USER LOGIN");
//					session.setAttribute("sessionCreated", fectchUsernameAndPassword);
//					return new ModelAndView("redirect:/dashboard");
//				}else if(fectchUsernameAndPassword != null && fectchUsernameAndPassword.getUserrole().equals("STUDENT")) {
//					System.out.println("STUDENT LOGIN");
//					session.setAttribute("sessionCreated", fectchUsernameAndPassword);
//					return new ModelAndView("redirect:/studentdashboard");
//				} else {
//				ModelAndView modelAndView = new ModelAndView("login");
//		        modelAndView.addObject("error", "Invalid username or password");
//		        return modelAndView;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				return new ModelAndView("login");	
//			}		
//	}

	@RequestMapping("/loginUser")
	public ModelAndView loginUser(@ModelAttribute user Usr, Model model, HttpSession session) {
		try {
			user fetchedUser = this.userdao.finduserNameAndPassword(Usr.getUsername(), Usr.getPassword());

			if (fetchedUser != null && fetchedUser.isActive()) {
				session.setAttribute("sessionCreated", fetchedUser);
				session.setAttribute("loggedInRole", fetchedUser.getSubRole().getRole().getRoleName());
				session.setAttribute("loggedInSubRole", fetchedUser.getSubRole().getSubRoleName());
				SubRole subRole = fetchedUser.getSubRole();
				Role role = (subRole != null) ? subRole.getRole() : null;

				if (role != null) {
					String mainRole = role.getRoleName().toUpperCase();
					String subRoleName = subRole.getSubRoleName().toUpperCase();

					// Parent role check
					if (mainRole.equals(RoleConstants.ROLE_SUPER_ADMIN)) {
						
						// if parent role access
						if (subRoleName.equals(RoleConstants.ROLE_SUPER_ADMIN)) {
							return new ModelAndView("redirect:/AdminDashboard");
						}
						
						// child sub role check all
						if (subRoleName.equals(RoleConstants.ROLE_ADMIN)) {
							return new ModelAndView("redirect:/AdminDashboard");
						}
					}
					return new ModelAndView("redirect:/defaultDashboard");
				} else {
					return new ModelAndView("redirect:/defaultDashboard");
				}
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
