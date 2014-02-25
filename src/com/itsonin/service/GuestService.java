package com.itsonin.service;

import com.google.inject.Inject;
import com.itsonin.dao.GuestDao;

/**
 * @author nkislitsin
 *
 */
public class GuestService {

	private GuestDao guestDao;
	
	@Inject
	public GuestService(GuestDao guestDao){
		this.guestDao = guestDao;
	}
	
}
