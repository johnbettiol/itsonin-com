package com.itsonin.security;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.entity.Device;
import com.itsonin.service.DeviceService;
import com.itsonin.utils.CookieUtils;
import com.itsonin.web.IoiRouterContext;
import com.itsonin.web.IoiRouterContext.IoiActionType;

/**
 * @author nkislitsin
 * 
 */
@Singleton
public class AuthAndRouteFilter implements Filter {

	public static final String REQ_ATTRIB_IOI_ROUTER_CONTEXT = "ioiContext";
	public static final String REQ_ATTRIB_DESTINATION = "ioiDestination";
	private static final String COOKIE_TOKEN = "token";
	private static final String COOKIE_DESTINATION = "destination";

	protected static final Logger log = Logger
			.getLogger(AuthAndRouteFilter.class.getName());

	private DeviceService deviceService;
	private AuthContextService authContextService;

	@Inject
	public AuthAndRouteFilter(DeviceService deviceService,
			AuthContextService authContextService) {
		this.deviceService = deviceService;
		this.authContextService = authContextService;
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpServletResponse res = (HttpServletResponse) servletResponse;

		if (req.getRequestURI() != null
				&& (req.getRequestURI().startsWith("/api/") || req
						.getRequestURI().startsWith("/static/") || req
						.getRequestURI().startsWith("/_ah/"))) {
			filterChain.doFilter(req, res);
			return;
		}
		Device device = null;
		String token = CookieUtils.getCookieValue(COOKIE_TOKEN, req);

		if (token != null && !token.isEmpty()) {
			device = deviceService.getDeviceByToken(token);
		}

		IoiRouterContext ioiRouter = new IoiRouterContext(req);
		req.setAttribute(REQ_ATTRIB_IOI_ROUTER_CONTEXT, ioiRouter);
		if (device == null) {
			// New user to site, redirect to related welcome page,
			// set a destination cookie
			device = deviceService.create(new Device());
			res.setHeader("Content-Type", "text/html");
			token = device.getToken();
			CookieUtils.setCookie(COOKIE_TOKEN, token, res);
			CookieUtils.setCookie(COOKIE_DESTINATION, ioiRouter.getDestination(), res);
			res.sendRedirect(ioiRouter.getPublicRoute(IoiActionType.WELCOME)); 
			return;
		}
		// If device token exists, then check if there is a destination cookie
		// pass destination cookie as a request attribute.
		Cookie destCookie;
		if ((destCookie = CookieUtils.getCookie(COOKIE_DESTINATION, req)) != null) {
			CookieUtils.setCookie(COOKIE_DESTINATION, null, 0, res);
			req.setAttribute(REQ_ATTRIB_DESTINATION, destCookie.getValue());
		} else if (ioiRouter.getActionType() == IoiActionType.WELCOME) {
			req.setAttribute(REQ_ATTRIB_DESTINATION, "/");
		}
		
		if (ioiRouter.doRedirect()) {
			res.sendRedirect(ioiRouter.getPublicRoute()); 
			return;
		} 
		authContextService.set(new AuthContext(device));
		req.getRequestDispatcher(ioiRouter.getInternalServlet()).forward(req,
				res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void destroy() {

	}

}
