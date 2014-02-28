package com.itsonin.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class Page2Servlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.getWriter().print("<a href=\"/page3\">Page 3</a>");
	}

}
