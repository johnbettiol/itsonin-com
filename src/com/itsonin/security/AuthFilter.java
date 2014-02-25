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

/**
 * @author nkislitsin
 *
 */
public class AuthFilter implements Filter{
	
	protected static final Logger log = Logger.getLogger(AuthFilter.class.getName());	
	
	static final ThreadLocal<Context> localContext = new ThreadLocal<Context>();

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)servletRequest;
		HttpServletResponse resp = (HttpServletResponse)servletResponse;
		
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

