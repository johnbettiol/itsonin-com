package com.itsonin.tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.GuestStatus;
import com.itsonin.service.EventService;
import com.itsonin.service.GuestService;

/**
 * @author nkislitsin
 *
 */
@Path("/tasks")
public class AdminTasks {
	private static final Logger log = Logger.getLogger(AdminTasks.class.getName());
	
	private EventService eventService;
	private GuestService guestService;
	
	@Inject
	public AdminTasks(EventService eventService, GuestService guestService){
		this.eventService = eventService;
		this.guestService = guestService;
	}

	@GET
	@Path("/deleteCompletedEvents")
	@Produces("application/json")
	public Response deleteCompletedEvents() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -2);
		
		eventService.deleteCompletedEvents(cal.getTime());
		
		log.info("Old events deleted successfully");
		return Response.ok().build();
	}

	@GET
	@Path("/computeHotScore")
	@Produces("application/json")
	public Response computeHotScore() {
		List<Event> events = eventService.listFutureEvents();//TODO: computation, cursors, chunks !!!
		for(Event event : events) {
			List<Guest> guests = guestService.listByEvent(event.getEventId());
			int attending = 0;
			for(Guest guest : guests) {
				if(guest.getStatus() == GuestStatus.YES) {
					attending++;
				}
			}
			event.setHotScore(attending);
		}
		eventService.saveAll(events);

		return Response.ok().build();
	}
}
