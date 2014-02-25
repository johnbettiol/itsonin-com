package com.itsonin.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.dao.EventDao;
import com.itsonin.entity.Event;

/**
 * @author nkislitsin
 *
 */
@Path("/api")
public class EventApi {
	
	private EventDao eventDao;
	
	@Inject
	public EventApi(EventDao eventDao){
		this.eventDao = eventDao;
	}
	
	@POST
	@Path("/event/create")
	@Consumes("application/json")
	@Produces("application/json")
	public Event create(Event event) {
		eventDao.save(event);
		return event;
	}
	
	@PUT
	@Path("/event/{id}/update")
	@Produces("application/json")
	public Response update(@PathParam("id")String id) {
		return Response.ok().build();
	}
	
	@GET
	@Path("/event/{id}/attend")
	@Produces("application/json")
	public Response attend(@PathParam("id")String id) {
		return Response.ok().build();
	}
	
	@GET
	@Path("/event/list")
	@Produces("application/json")
	public List<Event> list() {
		return eventDao.list();
	}
	
	@GET
	@Path("/event/{eventId}/{guestId}/cancel")
	@Produces("application/json")
	public Response cancel(@PathParam("eventId")String eventId,
								@PathParam("guestId")String guestId) {
		return Response.ok().build();
	}
	
	@GET
	@Path("/event/{eventId}/{guestId}/decline")
	@Produces("application/json")
	public Response decline(@PathParam("eventId")String eventId,
							@PathParam("guestId")String guestId) {
		return Response.ok().build();
	}
	
	@GET
	@Path("/event/{eventId}/{guestId}/invite")
	@Produces("application/json")
	public Response invite(@PathParam("eventId")String eventId,
							@PathParam("guestId")String guestId) {
		return Response.ok().build();
	}
	
	@PUT
	@Path("/event/{eventId}/{guestId}/update")
	@Produces("application/json")
	public Event update(@PathParam("eventId")String eventId,
							@PathParam("guestId")String guestId) {
		return null;
	}

}
