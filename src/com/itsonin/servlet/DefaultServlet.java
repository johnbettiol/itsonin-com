package com.itsonin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.security.AuthAndRouteFilter;
import com.itsonin.security.AuthContextService;
import com.itsonin.web.IoiRouterContext;

@SuppressWarnings("serial")
@Singleton
public class DefaultServlet extends HttpServlet {

	protected AuthContextService authContextService;
	protected IoiRouterContext irc;

	@Inject
	public DefaultServlet(AuthContextService authContextService) {
		this.authContextService = authContextService;
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		doPost(req, res);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		/* @TODO Make this global */
		irc = (IoiRouterContext) req
				.getAttribute(AuthAndRouteFilter.REQ_ATTRIB_IOI_ROUTER_CONTEXT);
		doIoiAction(req, res);
		doIoiForward(req, res);
	}

	protected void doIoiAction(HttpServletRequest req, HttpServletResponse res) {
		
	}

	protected void doIoiForward(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		req.getRequestDispatcher(irc.getInternalRoute()).forward(req, res);
	}

}
