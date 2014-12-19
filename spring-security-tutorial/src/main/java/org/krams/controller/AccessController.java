package org.krams.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.krams.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.core.context.SecurityContext;


import org.springframework.security.core.userdetails.User;

@Controller
@RequestMapping
public class AccessController {

	@RequestMapping("/login")
	public String login(Model model, @RequestParam(required=false) String message) {
		System.out.println("login1="+message);
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
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public @ResponseBody() String addDepartment(@RequestBody String body,HttpServletResponse response) {
	
			return "{\"message\":\"error\"}";
		}	
	@RequestMapping(value = "/create", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public @ResponseBody() String newDepartment(@RequestBody String body,HttpServletResponse response) {
	
			return "{\"message\":\"create error\"}";
		}	

	
	@Autowired
	private HttpServletRequest context;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@RequestMapping(value = "/autologin", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public @ResponseBody() String autoLogin(@RequestBody String body,HttpServletResponse response) {
	
		
	//	authenticationManager.
	//	UserDetails user = myuserdetailsservice.loadUserByUsername("user");
			
//		authenticateUserAndSetSession(user,context);
		
			return "{\"message\":\"create error\"}";
		}	

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	private void authenticateUserAndSetSession(UserDetails user, HttpServletRequest request)
	{
	     UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
	     user.getUsername(), user.getPassword());

	     // generate session if one doesn't exist
	     request.getSession();

	     token.setDetails(new WebAuthenticationDetails(request));
	     Authentication authenticatedUser = authenticationManager.authenticate(token);

	     SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
	     SecurityContext context = SecurityContextHolder.getContext();
	
	     SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

	     request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

	     
	     
	}
	
}
