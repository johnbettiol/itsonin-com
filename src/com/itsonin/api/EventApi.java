package com.itsonin.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.itsonin.dto.EventWithGuest;
import com.itsonin.entity.Comment;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.EventType;
import com.itsonin.enums.GuestStatus;
import com.itsonin.enums.SortOrder;
import com.itsonin.response.SuccessResponse;
import com.itsonin.resteasy.CustomDateFormat;
import com.itsonin.service.CommentService;
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
	private CommentService commentService;

	@Inject
	public EventApi(EventService eventService, GuestService guestService,
			CommentService commentService) {
		this.eventService = eventService;
		this.guestService = guestService;
		this.commentService = commentService;
	}

	@POST
	@Path("/event/create")
	@Consumes("application/json")
	@Produces("application/json")
	public Map<String, Object> create(EventWithGuest eventWithGuest) {
		return eventService.create(eventWithGuest.getEvent(),
				eventWithGuest.getGuest());
	}

	@PUT
	@Path("/event/{eventId}/update")
	@Produces("application/json")
	public Response update(@PathParam("eventId") Long eventId, Event event) {
		eventService.update(eventId, event);
		return Response.ok()
				.entity(new SuccessResponse("Event updated successfully"))
				.build();
	}

	@GET
	@Path("/event/{eventId}/info")
	@Produces("application/json")
	public Map<String, Object> info(@PathParam("eventId") Long eventId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Event event = eventService.get(eventId);
		List<Guest> guest = guestService.listByEvent(eventId,
				GuestStatus.ATTENDING);
		List<Comment> comments = commentService.list(eventId);

		result.put("event", event);
		result.put("guests", guest);
		result.put("comments", comments);
		return result;
	}

	@GET
	@Path("/event/{eventId}/attend")
	@Produces("application/json")
	public Guest attend(@PathParam("eventId") Long eventId) {
		return eventService.attend(eventId);
	}

	@GET
	@Path("/event/list")
	@Produces("application/json")
	public List<Event> list(
			@QueryParam("allEvents") Boolean allEvents,
			@QueryParam("types") List<EventType> types,
			@QueryParam("name") String name,
			@QueryParam("startTime") @CustomDateFormat("yyyy-MM-dd HH:mm") Date startTime,
			@QueryParam("comment") String comment,
			@QueryParam("sortField") String sortField,
			@QueryParam("sortOrder") SortOrder sortOrder,
			@QueryParam("numberOfLevels") Integer numberOfLevels,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return eventService.list(allEvents, types, name, startTime, comment,
				sortField, sortOrder, numberOfLevels, offset, limit);
	}

	@GET
	@Path("/event/{eventId}/{guestId}/cancel")
	@Produces("application/json")
	public Response cancel(@PathParam("eventId") Long eventId,
			@PathParam("guestId") Long guestId) {
		eventService.cancel(eventId, guestId);
		return Response.ok()
				.entity(new SuccessResponse("Event cancelled successfully"))
				.build();
	}

	@GET
	@Path("/event/{eventId}/cancel")
	@Produces("application/json")
	public Response cancel(@PathParam("eventId") Long eventId) {
		return cancel(eventId, null);
	}

	@GET
	@Path("/event/{eventId}/decline")
	@Produces("application/json")
	public Response decline(@PathParam("eventId") Long eventId) {
		eventService.decline(eventId);
		return Response.ok()
				.entity(new SuccessResponse("Event declined successfully"))
				.build();
	}

}
