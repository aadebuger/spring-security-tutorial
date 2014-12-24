package org.krams.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.krams.repository.UserRepository;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.User;

import org.aadebuger.MyTokenBasedRememberMeServices;

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
		
		String message1 = getErrorMessage(context, "SPRING_SECURITY_LAST_EXCEPTION");
		System.out.println("login fail1"+ message1);
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

	
	private String getErrorMessage(HttpServletRequest request, String key) {

		Exception exception = (Exception) request.getSession()
				.getAttribute(key);

		String error = "";
		if (exception instanceof BadCredentialsException) {
			error = "Invalid username and password!";
		} else if (exception instanceof LockedException) {
			error = exception.getMessage();
		} else {
			error = "Invalid username and password!";
		}

		return error;
	}
	protected String getRedirectUrl(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if(session != null) {
	        SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
	        if(savedRequest != null) {
	            return savedRequest.getRedirectUrl();
	        }
	    }

	    /* return a sane default in case data isn't there */
	    return request.getContextPath() + "/";
	}
	
	@Autowired
	private HttpServletRequest context;
	
	@Autowired
	     @Qualifier("authenticationManager")
	      protected AuthenticationManager authenticationManager;
//	@Autowired
//	private HttpServletRequest context;
	@Autowired
	private MyTokenBasedRememberMeServices myservies;

	
	@RequestMapping(value = "/autologin", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public @ResponseBody() String autoLogin(HttpServletResponse response, HttpServletRequest request) {
	          UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
	                 "john", "admin");
	 
	         request.getSession();
	 
	         token.setDetails(new WebAuthenticationDetails(request));
	         Authentication authenticatedUser = authenticationManager.authenticate(token);
	 
	         
	         System.out.println("myservies= "+   myservies);
	         
	        SecurityContextHolder.getContext(). setAuthentication(authenticatedUser);
	        
	        
	        return "{\"message\":\"auto login ok\"}";
	     }
	@RequestMapping(value = "/faillogin", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public @ResponseBody() String faillogin(String account, HttpServletRequest request) {
	          UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
	                 "john", "adminfail");
	 
	         request.getSession();
	 
	         token.setDetails(new WebAuthenticationDetails(request));
	         Authentication authenticatedUser = authenticationManager.authenticate(token);
	 
	        SecurityContextHolder.getContext(). setAuthentication(authenticatedUser);
	        
	  //      Authentication rememberMeAuth = rememberMeServices.autoLogin(request, response);

	        
	        return "{\"message\":\"auto fail error\"}";
	     }
	 
	 
	/*
prvate CustomUserDetailsService userDetailsService;
	
	@RequestMapping(value = "/autologin", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public @ResponseBody() String autoLogin(@RequestBody String body,HttpServletResponse response) {
	
		
	//	authenticationManager.
		UserDetails user = loadUserByUsername("jane");
			
		System.out.println("user="+user);
		authenticateUserAndSetSession(user,context);
		
			return "{\"message\":\"create error\"}";
		}	

	
	@Autowired
	private UserRepository userRepository;

	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			org.krams.domain.User domainUser = userRepository.findByUsername(username);
			
			boolean enabled = true;
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;
			
			return new User(
					domainUser.getUsername(), 
					domainUser.getPassword().toLowerCase(),
					enabled,
					accountNonExpired,
					credentialsNonExpired,
					accountNonLocked,
					getAuthorities(domainUser.getRole().getRole()));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
		return authList;
	}
	
	
	public List<String> getRoles(Integer role) {
		List<String> roles = new ArrayList<String>();
		
		if (role.intValue() == 1) {
			roles.add("ROLE_USER");
			roles.add("ROLE_ADMIN");
			
		} else if (role.intValue() == 2) {
			roles.add("ROLE_USER");
		}
		
		return roles;
	}
	
	
	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	private void authenticateUserAndSetSession(UserDetails user, HttpServletRequest request)
	{
	     UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
	     user.getUsername(), user.getPassword(),user.getAuthorities());

	     // generate session if one doesn't exist
	     request.getSession();

	     token.setDetails(new WebAuthenticationDetails(request));
	     Authentication authenticatedUser = authenticationManager.authenticate(token);

	     SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
	     SecurityContext context = SecurityContextHolder.getContext();
	
	  //   SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

	     request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

	     
	     
	}
	*/
}
