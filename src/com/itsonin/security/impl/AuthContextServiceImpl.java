package com.itsonin.security.impl;

import javax.servlet.http.HttpSession;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.itsonin.entity.Device;
import com.itsonin.security.AuthContext;
import com.itsonin.security.AuthContextService;

/**
 * @author nkislitsin
 *
 */
@Singleton
public class AuthContextServiceImpl implements AuthContextService{

	@Inject 
	private Provider<HttpSession> sessionProvider;
	
	private static final String name = "authContext";
	
	public AuthContext get(){
		Object obj = sessionProvider.get().getAttribute(name);
		if (obj == null){
			return null;
		}
		return (AuthContext) obj;
	}
	
	public void set(AuthContext context){
		sessionProvider.get().setAttribute(name, context);
	}
	
	public Device getDevice(){
		AuthContext context = get();
		return context == null? null : context.getDevice();
	}
	
}
