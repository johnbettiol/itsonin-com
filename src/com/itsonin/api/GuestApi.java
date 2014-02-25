package com.itsonin.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.itsonin.entity.Guest;
import com.itsonin.service.GuestService;

/**
 * @author nkislitsin
 *
 */
@Path("/api")
public class GuestApi {
	
	private GuestService guestService;
	
	@Inject
	public GuestApi(GuestService guestService){
		this.guestService = guestService;
	}
	
	@GET
	@Path("/event/{eventId}/{guestId}/guest/list")
	@Produces("application/json")
	public List<Guest> list(@PathParam("eventId")String eventId,
							@PathParam("guestId")String guestId) {
		return null;
	}

}
