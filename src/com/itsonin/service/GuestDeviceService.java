package com.itsonin.service;

import com.google.inject.Inject;
import com.itsonin.dao.GuestDao;

/**
 * @author nkislitsin
 *
 */
public class GuestDeviceService {

	private GuestDao guestDao;
	
	@Inject
	public GuestDeviceService(GuestDao guestDao){
		this.guestDao = guestDao;
	}
	
}
