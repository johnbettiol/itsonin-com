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
import com.itsonin.dto.EventWithGuest;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.response.SuccessResponse;
import com.itsonin.service.EventService;

/**
 * @author nkislitsin
 *
 */
@Path("/api")
public class EventApi {
	
	private EventService eventService;
	
	@Inject
	public EventApi(EventService eventService){
		this.eventService = eventService;
	}
	
	@POST
	@Path("/event/create")
	@Consumes("application/json")
	@Produces("application/json")
	public EventWithGuest create(EventWithGuest eventWithGuest) {
		return eventService.create(eventWithGuest.getEvent(), eventWithGuest.getGuest());
	}
	
	@PUT
	@Path("/event/{id}/update")
	@Produces("application/json")
	public Response update(@PathParam("id")Long id, Event event) {
		eventService.update(id, event);
		return Response.ok().entity(new SuccessResponse("Event updated successfully")).build();
	}
	
	@GET
	@Path("/event/{id}/info")
	@Produces("application/json")
	public Event info(@PathParam("id")Long id) {
		return eventService.get(id);
	}
	
	@GET
	@Path("/event/{eventId}/attend")
	@Produces("application/json")
	public Guest attend(@PathParam("eventId")Long eventId, Guest guest) {
		return eventService.attend(eventId, guest);
	}
	
	@GET
	@Path("/event/list")
	@Produces("application/json")
	public List<Event> list() {
		return eventService.list();
	}
	
	@GET
	@Path("/event/{eventId}/{guestId}/cancel")
	@Produces("application/json")
	public Response cancel(@PathParam("eventId")Long eventId,
						   @PathParam("guestId")Long guestId) {
		eventService.cancel(eventId, guestId);
		return Response.ok().entity(new SuccessResponse("Event cancelled successfully")).build();
	}
	
	@GET
	@Path("/event/{eventId}/cancel")
	@Produces("application/json")
	public Response cancel(@PathParam("eventId")Long eventId) {
		return cancel(eventId, null);
	}
	
	@GET
	@Path("/event/{eventId}/{guestId}/decline")
	@Produces("application/json")
	public Response decline(@PathParam("eventId")Long eventId,
							@PathParam("guestId")Long guestId) {
		eventService.decline(eventId, guestId);
		return Response.ok().entity(new SuccessResponse("Event declined successfully")).build();
	}
	
	/*
	@PUT
	@Path("/event/{eventId}/{guestId}/update")
	@Produces("application/json")
	public Response update(@PathParam("eventId")Long eventId,
						   @PathParam("guestId")Long guestId, 
						   Event event) {
		eventService.update(eventId, event);
		return Response.ok().entity(new SuccessResponse("Event updated successfully")).build();
	}*/ //TODO:Guest?

}
