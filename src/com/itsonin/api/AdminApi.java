package com.itsonin.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.itsonin.crawl.EventimSeeder;
import com.itsonin.crawl.PrinzDeSeeder;
import com.itsonin.entity.Comment;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.GuestStatus;
import com.itsonin.service.AdminService;
import com.itsonin.service.CommentService;
import com.itsonin.service.EventService;

/**
 * @author nkislitsin
 * 
 */
@Path("/api/admin")
public class AdminApi {

	private AdminService adminService;
	private EventService eventService;
	private CommentService commentService;

	@Inject
	public AdminApi(AdminService adminService, EventService eventService, CommentService commentService) {
		this.adminService = adminService;
		this.eventService = eventService;
		this.commentService = commentService;
	}

	@GET
	@Path("/event/{city}")
	@Produces("application/json")
	public List<Event> list(@PathParam("city") String city) {
		return adminService.list(city);
	}

	@PUT
	@Path("/event")
	@Consumes("application/json")
	@Produces("application/json")
	public Event update(Event event) {
		return adminService.save(event);
	}

	@POST
	@Path("/event/remove")
	@Produces("application/json")
	public Response remove(List<Long> ids) {
		adminService.remove(ids);
		return Response.ok().build();
	}

	@GET
	@Path("/seed/{engine}")
	@Produces("application/json")
	public Response seed(@PathParam("engine") String engine) {
		Guest guest = new Guest("Joey McCloud");
		switch (engine) {
			case "prinz":
				guest.setStatus(GuestStatus.YES);
				ArrayList<Event> eventsListP = new PrinzDeSeeder().getNewEvents();
				for (Event event : eventsListP) {
					Map<String, Object> created = eventService.create(event, guest);
					Long eventId = ((Event)created.get("event")).getEventId();
					Long guestId = ((Guest)created.get("guest")).getGuestId();
					commentService.create(new Comment(eventId, guestId, null, "Question"));
					commentService.create(new Comment(eventId, guestId, null, "Answer"));
				}
				break;
			case "eventim":
				guest.setStatus(GuestStatus.YES);
				ArrayList<Event> eventsListE = new EventimSeeder().getNewEvents();
				for (Event event : eventsListE) {
					Map<String, Object> created = eventService.create(event, guest);
					Long eventId = ((Event)created.get("event")).getEventId();
					Long guestId = ((Guest)created.get("guest")).getGuestId();
					commentService.create(new Comment(eventId, guestId, null, "Question"));
					commentService.create(new Comment(eventId, guestId, null, "Answer"));
				}
				break;
			default:
				return Response.status(Status.BAD_REQUEST).entity("Unknown engine").build();
		}
		return Response.ok().build();
	}

}
