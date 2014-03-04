package com.itsonin.security;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.ResourceMethodInvoker;

import com.google.inject.Inject;
import com.itsonin.exception.UnauthorizedException;
import com.itsonin.security.annotations.PermitAll;

/**
 * @author nkislitsin
 *
 */
@Provider
public class ApiSecurityFilter implements ContainerRequestFilter{
	
	private AuthContextService authContextService;
	
	@Inject
	public ApiSecurityFilter(AuthContextService authContextService){
		this.authContextService = authContextService;
	}

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) context.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();
        if(!method.isAnnotationPresent(PermitAll.class) && authContextService.get() == null){
        	throw new UnauthorizedException("Not authenticated");
        }
	}

}
