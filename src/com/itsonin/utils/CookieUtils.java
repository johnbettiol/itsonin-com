package com.itsonin.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author nkislitsin
 *
 */
public class CookieUtils {
	
	private static final DateTimeFormatter expireFormat = DateTimeFormat.forPattern("EEE, dd-MMM-yyyy HH:mm:ss zzz").withLocale(Locale.US).withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT")));

	
	public static void setCookie(String name, String value, HttpServletResponse resp){
		setCookie(name, value, 0x0fffffff, "/", null, resp);
	}
	
	public static void setCookie(String name, String value, int maxAge, HttpServletResponse resp){
		setCookie(name, value, maxAge, "/", null, resp);
	}
	
	public static void setCookie(String name, String value, int maxAge, String path, String domain, HttpServletResponse resp){
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		if (domain!=null)
			cookie.setDomain(domain);
		
		resp.addCookie(cookie);
	
	}
	
	/**
	 * add a HttpOnly Cookie to the response
	 * @param name of the cookie
	 * @param value of the cookie
	 * @param maxAge in minutes before this cookie expires, if null won't set any expire data (session cookie), if set <=0 will set expire to the past to cause immediate deletion of cookie on client side 
	 * @param path of cookie, set null to avoid setting a path, which mean browser will set it (most likely to context) - so its better to put "/"
	 * @param domain can be null
	 * @param resp
	 */
	public static void setHttpOnlyCookie(String name, String value, Integer maxAge, String path, String domain, boolean secure, HttpServletResponse resp){
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("=").append(value).append(";");
		if (domain!=null) {
			sb.append(" Domain=").append(domain).append(";");
		}
		if (path!=null) {
			sb.append(" Path=").append(path).append(";");
		}
		if (maxAge!=null){
			if (maxAge>0) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MINUTE, maxAge);
				sb.append(" Expires=").append(expireFormat.print(cal.getTimeInMillis())).append(";");
			} else {
				sb.append(" Expires=").append("Thu, 01 Jan 1970 00:00:01 GMT").append(";");
			}
			
			
		}
		
		if (secure){
			sb.append(" Secure;");
		}
		
		sb.append(" HttpOnly");
		
		
		resp.addHeader("Set-Cookie", sb.toString());
	}
	
	public static Cookie getCookie(String name, HttpServletRequest req){
		Cookie[] cookies = req.getCookies();

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];

				if (c.getName().equals(name)) {
					return c;
				}
			}
		}

		return null;
	}
	
	public static String getCookieValue(String name, HttpServletRequest req){
		Cookie c = getCookie(name, req);
		if (c!=null)
			return c.getValue();
		else
			return null;
	}
	
	public static List<Cookie> getCookies(Pattern p, HttpServletRequest req){
		Cookie[] cookies = req.getCookies();
		List<Cookie> results = new ArrayList<Cookie>();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];

				if (p.matcher(c.getName()).matches()) {
					results.add(c);
				}
			}
		}

		return results;
	}
	
	public static void removeCookie(Cookie cookie, HttpServletResponse resp){
		cookie.setValue("");
		cookie.setMaxAge(0);
		resp.addCookie(cookie);
	}
	
	public static void removeCookie(String name, HttpServletRequest req, HttpServletResponse resp){
		Cookie cookie = getCookie(name, req);
		if (cookie==null) {
			cookie = new Cookie(name, "");
		}
		cookie.setPath("/");
		removeCookie(cookie, resp);
	}
}
