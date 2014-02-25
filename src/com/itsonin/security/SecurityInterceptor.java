package com.itsonin.security;

import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import com.google.inject.Inject;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.security.annotations.PermitAll;
import com.itsonin.security.annotations.DeviceLevelAllowed;

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
        DeviceLevelAllowed userTypesAllowed = method.getMethod().getAnnotation(DeviceLevelAllowed.class);
        PermitAll permitAll = method.getMethod().getAnnotation(PermitAll.class);
 /*
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
        }*/
        return response;
	}
	
    private boolean isDeviceAlowed(DeviceLevel deviceLevel, DeviceLevel[] allowedDeviceLevels){
    	for (DeviceLevel allowedDeviceLevel : allowedDeviceLevels){
    		if (allowedDeviceLevel.equals(deviceLevel)) return true;
    	}
    	return false;
    }
}

