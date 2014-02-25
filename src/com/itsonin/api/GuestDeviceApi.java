package com.itsonin.api;

import javax.ws.rs.Path;

import com.google.inject.Inject;
import com.itsonin.service.GuestDeviceService;

/**
 * @author nkislitsin
 *
 */
@Path("/api")
public class GuestDeviceApi {
	
	private GuestDeviceService guestDeviceService;
	
	@Inject
	public GuestDeviceApi(GuestDeviceService guestDeviceService){
		this.guestDeviceService = guestDeviceService;
	}

}
