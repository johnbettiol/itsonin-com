package com.itsonin.security.impl;

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

	static final ThreadLocal<AuthContext> localContext = new ThreadLocal<AuthContext>();
	
	public AuthContext get(){
		Object obj = localContext.get();
		if (obj == null){
			return null;
		}
		return (AuthContext) obj;
	}
	
	public void set(AuthContext context){
		localContext.set(context);
	}
	
	public Device getDevice(){
		AuthContext context = get();
		return context == null? null : context.getDevice();
	}
	
}
