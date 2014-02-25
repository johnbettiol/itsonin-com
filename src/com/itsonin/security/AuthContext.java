package com.itsonin.security;

import java.io.Serializable;

import com.itsonin.entity.Device;

public class AuthContext implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Device device;
	
	public AuthContext(Device device) {
		this.device = device;
	}
	
	public Device getDevice() {
		return device;
	}
	
	public void setDevice(Device device) {
		this.device = device;
	}
	
	public boolean isLoggedIn() {
		return this.device != null;
	}

}
