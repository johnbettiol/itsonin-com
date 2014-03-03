package com.itsonin.security;

import com.itsonin.entity.Device;

/**
 * @author nkislitsin
 *
 */
public interface AuthContextService {
	
	public AuthContext get();
	public void set(AuthContext context);
	public Device getDevice();
	
}
