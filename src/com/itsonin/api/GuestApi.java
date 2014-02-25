package com.itsonin.api;

import javax.ws.rs.Path;

import com.google.inject.Inject;
import com.itsonin.dao.GuestDao;

/**
 * @author nkislitsin
 *
 */
@Path("/api")
public class GuestApi {
	
	private GuestDao guestDao;
	
	@Inject
	public GuestApi(GuestDao guestDao){
		this.guestDao = guestDao;
	}

}
