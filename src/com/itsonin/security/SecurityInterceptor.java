package com.itsonin.security;

import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import com.google.inject.Inject;
import com.itsonin.entity.User;
import com.itsonin.enums.UserType;
import com.itsonin.security.annotations.PermitAll;
import com.itsonin.security.annotations.UserTypeAllowed;

/**
 * @author nkislitsin
 *
 */
@Provider
@ServerInterceptor
@SuppressWarnings("deprecation")
public class SecurityInterceptor implements PreProcessInterceptor{
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SecurityInterceptor.class.getName());
	
	@Inject
	private AuthContextService authContextService;

	@Override
	public ServerResponse preProcess(HttpRequest request,
			ResourceMethodInvoker method) throws Failure, WebApplicationException {
        ServerResponse response = null;
        UserTypeAllowed userTypesAllowed = method.getMethod().getAnnotation(UserTypeAllowed.class);
        PermitAll permitAll = method.getMethod().getAnnotation(PermitAll.class);
 
        User user = authContextService.getUser();
        
        if (userTypesAllowed==null && permitAll==null){
           Class<?> resourceClass = method.getResourceClass();
           userTypesAllowed = resourceClass.getAnnotation(UserTypeAllowed.class);
           permitAll = resourceClass.getAnnotation(PermitAll.class);
        }
        
        if(permitAll == null){
	        if(user == null){
	        	response = new ServerResponse("Not authorized",401, new Headers<Object>());
	        }else{
	        	if (userTypesAllowed != null && !isUserInRole(user.getType(), userTypesAllowed.value())) 
	        		response = new ServerResponse("Not allowed", 403, new Headers<Object>());
	        }
        }
        return response;
	}
	
    private boolean isUserInRole(UserType userType, UserType[] allowedUserTypes){
    	for (UserType allowedUserType : allowedUserTypes){
    		if (allowedUserType.equals(userType)) return true;
    	}
    	return false;
    }
}

