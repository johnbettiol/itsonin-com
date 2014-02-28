package com.itsonin.security;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.entity.Device;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.DeviceType;
import com.itsonin.service.DeviceService;
import com.itsonin.utils.CookieUtils;

/**
 * @author nkislitsin
 *
 */
@Singleton
public class AuthFilter implements Filter{
	
	protected static final Logger log = Logger.getLogger(AuthFilter.class.getName());	
	
	static final ThreadLocal<Context> localContext = new ThreadLocal<Context>();
	
	private DeviceService deviceService;
	
	@Inject
	public AuthFilter(DeviceService deviceService){
		this.deviceService = deviceService;
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)servletRequest;
		HttpServletResponse resp = (HttpServletResponse)servletResponse;
		HttpSession session = req.getSession();
		
		AuthContext authContext = (AuthContext)session.getAttribute("authContext");
		if(authContext == null){
			Device device = null;
			String token = CookieUtils.getCookieValue("token", req);
			
			if(token != null && !token.isEmpty())
				device = deviceService.getDeviceByToken(token);

			if(device == null){
				device = deviceService.create(new Device(DeviceType.BROWSER, DeviceLevel.NORMAL));
				CookieUtils.setCookie("token", device.getToken(), resp);
			}
			
			session.setAttribute("authContext", new AuthContext(device));
		}
		
		Context previous = localContext.get();

		try {
			localContext.set(new Context(req, resp));
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			localContext.set(previous);
		}
	}
	
	public static HttpServletRequest getRequest() {
		return getContext().getRequest();
	}

	public static HttpServletResponse getResponse() {
		return getContext().getResponse();
	}
	
	public static Context getContext() {
		return localContext.get();
	}

	static class Context {

		final HttpServletRequest request;
		final HttpServletResponse response;

		Context(HttpServletRequest request, HttpServletResponse response) {
			this.request = request;
			this.response = response;
		}

		HttpServletRequest getRequest() {
			return request;
		}

		HttpServletResponse getResponse() {
			return response;
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

	@Override
	public void destroy() {
		
	}

}

