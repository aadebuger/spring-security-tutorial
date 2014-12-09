package org.krams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
@Controller
@RequestMapping
public class AccessController {

	@RequestMapping("/login")
	public String login(Model model, @RequestParam(required=false) String message) {
		System.out.println("login="+message);
		model.addAttribute("message", message);
		return "access/login";
	}
	
	@RequestMapping(value = "/denied")
 	public String denied() {
		return "access/denied";
	}
	
	@RequestMapping(value = "/login/failure")
 	public String loginFailure() {
		String message = "Login Failure!";
		return "redirect:/login?message="+message;
	}
	
	@RequestMapping(value = "/logout/success")
 	public String logoutSuccess() {
		String message = "Logout Success!";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username ;
		if (principal instanceof UserDetails) {
  		username = ((UserDetails)principal).getUsername();
		} else {
  	username = principal.toString();
		}
		System.out.println("username="+username);

		return "redirect:/login?message="+message;
	}
}
