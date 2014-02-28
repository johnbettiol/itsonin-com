package com.itsonin.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.security.AuthContextService;

@SuppressWarnings("serial")
@Singleton
public class Page3Servlet extends HttpServlet {
	
	private AuthContextService authContextService;
	
	@Inject
	public Page3Servlet(AuthContextService authContextService){
		this.authContextService = authContextService;
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		ObjectMapper mapper = new ObjectMapper();
		resp.getWriter().print(mapper.writeValueAsString(authContextService.getDevice()));
	}

}
