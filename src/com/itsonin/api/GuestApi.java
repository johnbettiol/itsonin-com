package com.itsonin.api;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.entity.Guest;
import com.itsonin.enums.SortOrder;
import com.itsonin.response.SuccessResponse;
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
	
	@POST
	@Path("/event/{eventId}/guest/create")
	@Consumes("application/json")
	@Produces("application/json")
	public Guest create(@PathParam("eventId")Long eventId, Guest guest) {
		return guestService.create(eventId, guest);
	}
	
	@GET
	@Path("/event/{eventId}/{guestId}/guest/list")
	@Produces("application/json")
	public List<Guest> list(@PathParam("eventId")Long eventId,
							@PathParam("guestId")String guestId,
							@QueryParam("name")String name,
							@QueryParam("created")Date created,
							@QueryParam("comment")String comment,
							@QueryParam("sortField")String sortField,
							@QueryParam("sortOrder")SortOrder sortOrder,
							@QueryParam("numberOfLevels")Integer numberOfLevels,
							@QueryParam("offset")Integer offset,
							@QueryParam("limit")Integer limit) {
		return guestService.list(eventId, name, created, comment, sortField, sortOrder, numberOfLevels, offset, limit);
	}
	
	@GET
	@Path("/event/{eventId}/guest/list")
	@Produces("application/json")
	public List<Guest> list(@PathParam("eventId")Long eventId,
							@QueryParam("name")String name,
							@QueryParam("created")Date created,
							@QueryParam("comment")String comment,
							@QueryParam("sortField")String sortField,
							@QueryParam("sortOrder")SortOrder sortOrder,
							@QueryParam("numberOfLevels")Integer numberOfLevels,
							@QueryParam("offset")Integer offset,
							@QueryParam("limit")Integer limit) {
		return list(eventId, null, name, created, comment, sortField, sortOrder, numberOfLevels, offset, limit);
	}
	
	@PUT
	@Path("/event/{eventId}/{guestId}/update")
	@Produces("application/json")
	public Response update(@PathParam("eventId")Long eventId,
						   @PathParam("guestId")Long guestId, 
						   Guest guest) {
		guestService.update(eventId, guestId, guest);
		return Response.ok().entity(new SuccessResponse("Guest updated successfully")).build();
	}

}
