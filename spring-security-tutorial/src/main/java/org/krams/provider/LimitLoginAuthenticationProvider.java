package org.krams.provider;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
 
//import com.mkyong.users.dao.UserDetailsDao;
//import com.mkyong.users.model.UserAttempts;
 
@Component("authenticationProvider")
public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {

	int count=0;
	/*
	@Autowired
	UserDetailsDao userDetailsDao;
 */
	@Autowired
	@Qualifier("userDetailsService")
	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}
 
	@Override
	public Authentication authenticate(Authentication authentication) 
          throws AuthenticationException {
 
	  try {
 
		Authentication auth = super.authenticate(authentication);
 
		//if reach here, means login success, else an exception will be thrown
		//reset the user_attempts
	//	userDetailsDao.resetFailAttempts(authentication.getName());
		count = 0 ;
		return auth;
 
	  } catch (BadCredentialsException e) {	
 
		 System.out.println("BadCredentialsException "+ e+ authentication.getName());
		 System.out.println("BadCredentialsException count  "+ count);
			 
		 count=count+1;
		//invalid login, update to user_attempts
//		userDetailsDao.updateFailAttempts(authentication.getName());
		 if (count >=3)
			 throw new LockedException("User Account is locked!");
		throw e;
 
	  } catch (LockedException e){
 
		  System.out.println("LockedException "+ e);
		//this user is locked!
		String error = "";
		/*
		UserAttempts userAttempts = 
                    userDetailsDao.getUserAttempts(authentication.getName());
 
               if(userAttempts!=null){
			Date lastAttempts = userAttempts.getLastModified();
			error = "User account is locked! <br><br>Username : " 
                           + authentication.getName() + "<br>Last Attempts : " + lastAttempts;
		}else{
			error = e.getMessage();
		}
 		*/
		 error="User account is locked";
		
	  throw new LockedException(error);
	}
 
	}
 
}

