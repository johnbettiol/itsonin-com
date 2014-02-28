package com.itsonin.mocks;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class MockHttpSession implements HttpSession
{
    private Map<String, Object> attributes = new HashMap<String, Object>();
 
    @Override
    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }
 
    @Override
    public void setAttribute(String name, Object value)
    {
        attributes.put(name, value);
    }

	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeValue(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub
		
	}

}
