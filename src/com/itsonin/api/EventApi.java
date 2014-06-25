package com.itsonin.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.dto.EventInfo;
import com.itsonin.dto.EventWithGuest;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.EventCategory;
import com.itsonin.enums.SortOrder;
import com.itsonin.response.SuccessResponse;
import com.itsonin.resteasy.CustomDateFormat;
import com.itsonin.service.EventService;
import com.itsonin.service.GuestService;

/**
 * @author nkislitsin
 * 
 */
@Path("/api")
public class EventApi {

	private EventService eventService;
	private GuestService guestService;

	@Inject
	public EventApi(EventService eventService, GuestService guestService) {
		this.eventService = eventService;
		this.guestService = guestService;
	}

	@POST
	@Path("/event/create")
	@Consumes("application/json")
	@Produces("application/json")
	public Map<String, Object> create(EventWithGuest eventWithGuest) {
		return eventService.create(eventWithGuest.getEvent(),
				eventWithGuest.getGuest());
	}

	@POST
	@Path("/event/{eventId}/update")
	@Consumes("application/json")
	@Produces("application/json")
	public Response update(@PathParam("eventId") Long eventId, EventWithGuest eventWithGuest) {
		eventService.update(eventId, eventWithGuest.getEvent());
		if(eventWithGuest.getGuest() != null){
			guestService.update(eventWithGuest.getGuest());
		}
		return Response.ok()
				.entity(new SuccessResponse("Event updated successfully"))
				.build();
	}

	@GET
	@Path("/event/{eventId}/info")
	@Produces("application/json")
	public EventInfo info(@PathParam("eventId") Long eventId, 
			@QueryParam("forInvitation") Boolean forInvitation) {
		return eventService.info(eventId, forInvitation);
	}

	@POST
	@Path("/event/{eventId}/attend/{guestName}")
	@Produces("application/json")
	public Guest attend(@PathParam("eventId") Long eventId, @PathParam("guestName") String guestName) {
		return eventService.attend(eventId, guestName);
	}

	@GET
	@Path("/event/list")
	@Produces("application/json")
	public List<Event> list(
			@QueryParam("allEvents") Boolean allEvents,
			@QueryParam("types") List<EventCategory> types,
			@QueryParam("name") String name,
			@QueryParam("startTime") @CustomDateFormat("yyyy-MM-dd HH:mm") Date startTime,
			@QueryParam("endTime") @CustomDateFormat("yyyy-MM-dd HH:mm") Date endTime,
			@QueryParam("comment") String comment,
			@QueryParam("sortField") String sortField,
			@QueryParam("sortOrder") SortOrder sortOrder,
			@QueryParam("numberOfLevels") Integer numberOfLevels,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return eventService.list(allEvents, types, name, startTime, endTime, comment,
				sortField, sortOrder, numberOfLevels, offset, limit);
	}

	@POST
	@Path("/event/{eventId}/{guestId}/cancel")
	@Produces("application/json")
	public Response cancel(@PathParam("eventId") Long eventId,
			@PathParam("guestId") Long guestId) {
		eventService.cancel(eventId, guestId);
		return Response.ok()
				.entity(new SuccessResponse("Event cancelled successfully"))
				.build();
	}

	@POST
	@Path("/event/{eventId}/cancel")
	@Produces("application/json")
	public Response cancel(@PathParam("eventId") Long eventId) {
		return cancel(eventId, null);
	}

	@POST
	@Path("/event/{eventId}/decline")
	@Produces("application/json")
	public Guest decline(@PathParam("eventId") Long eventId) {
		return eventService.decline(eventId);
	}

}
