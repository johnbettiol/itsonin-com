package com.itsonin.tasks;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.EventFlexibility;
import com.itsonin.enums.EventSharability;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.EventType;
import com.itsonin.enums.EventVisibility;
import com.itsonin.service.EventService;

/**
 * @author nkislitsin
 *
 */
@Path("/tasks")
public class AdminTasks {
	
	private EventService eventService;
	
	@Inject
	public AdminTasks(EventService eventService){
		this.eventService = eventService;
	}
	
	@GET
	@Path("/initdb")
	@Produces("application/json")
	public Response initdb() {
		
		Event event = new Event(EventType.PICNIC, EventSharability.NORMAL, EventVisibility.PUBLIC, 
				EventStatus.ACTIVE, EventFlexibility.NEGOTIABLE, "event title", "event description", 
				"event notes", new Date(), new Date(), 1.0d, 2.0d, "location.url", "location title", 
        		"location address", new Date());
		Guest guest = new Guest("Guest name");
		eventService.create(event, guest);
		
		return Response.ok().entity("Datastore initialized successfully").build();
	}

}
