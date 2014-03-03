package com.itsonin.mocks;

import java.util.HashMap;
import java.util.Map;

import com.itsonin.entity.Device;
import com.itsonin.security.AuthContext;
import com.itsonin.security.AuthContextService;

/**
 * @author nkislitsin
 *
 */
public class MockAuthContextService implements AuthContextService{

	private static final String ATTRIBUTE_NAME = "authContext";
	
	private Map<String, MockHttpSession> sessions = new HashMap<String, MockHttpSession>();
	private String activeSessionId = null;

	public AuthContext get(){
		if(activeSessionId == null)
			return null;

		Object obj = sessions.get(activeSessionId).getAttribute(ATTRIBUTE_NAME);
		
		if (obj == null){
			return null;
		}
		return (AuthContext) obj;
	}
	
	public void set(AuthContext context){
		MockHttpSession session = sessions.get("device_"+context.getDevice().getDeviceId()+"_session");
		if(session == null)
			session = new MockHttpSession();
		session.setAttribute("authContext", context);
	}
	
	public Device getDevice(){
		AuthContext context = get();
		return context == null? null : context.getDevice();
	}
	
	public void createSession(Device device){
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(ATTRIBUTE_NAME, new AuthContext(device));
		sessions.put("device_" + device.getDeviceId() + "_session", session);
	}
	
	public void invalidateSession(Device device){
		sessions.remove(device.getDeviceId());
	}
	
	public void invalidateSessions(){
		sessions.clear();
	}
	
	public void setActiveSession(Device device) {
		String sessionId = "device_"+device.getDeviceId()+"_session";
		MockHttpSession session = sessions.get(sessionId);
		activeSessionId = (session == null) ? null : sessionId;
	}
	
}
