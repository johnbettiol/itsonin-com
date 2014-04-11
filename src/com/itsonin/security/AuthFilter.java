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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.entity.Device;
import com.itsonin.enums.DeviceType;
import com.itsonin.service.DeviceService;
import com.itsonin.utils.CookieUtils;

/**
 * @author nkislitsin
 * 
 */
@Singleton
public class AuthFilter implements Filter {

	private static final String TOKEN_COOKIE = "token";

	protected static final Logger log = Logger.getLogger(AuthFilter.class
			.getName());

	private DeviceService deviceService;
	private AuthContextService authContextService;

	@Inject
	public AuthFilter(DeviceService deviceService, AuthContextService authContextService) {
		this.deviceService = deviceService;
		this.authContextService = authContextService;
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpServletResponse res = (HttpServletResponse) servletResponse;
		
		if (!"/".equals(req.getRequestURI()) && !req.getRequestURI().startsWith("/api") &&
			!req.getRequestURI().startsWith("/tasks") &&!req.getRequestURI().startsWith("/_ah")) {			
			req.getRequestDispatcher("/index.jsp").forward(req, res);
			return;
		}else{

			Device device = null;
			String token = CookieUtils.getCookieValue(TOKEN_COOKIE, req);

			if (token != null && !token.isEmpty()) {
				device = deviceService.getDeviceByToken(token);
			}

			if (device == null) {
				// @TODO We no longer need device type, just create with nothing
				device = deviceService.create(new Device(DeviceType.BROWSER));
				CookieUtils.setCookie(TOKEN_COOKIE, device.getToken(), res);
				if (servletRequest instanceof HttpServletRequest) {
					if ("/".equals(req.getRequestURI())) {
						res.sendRedirect("/welcome");
					}
				}
			}
			authContextService.set(new AuthContext(device));
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void destroy() {

	}

}
