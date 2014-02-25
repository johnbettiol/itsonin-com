package com.itsonin.security;

import java.io.Serializable;

import com.itsonin.entity.User;

public class AuthContext implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public AuthContext(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean isLoggedIn() {
		return this.user != null;
	}

}
