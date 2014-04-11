package com.itsonin.tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.service.EventService;

/**
 * @author nkislitsin
 *
 */
@Path("/tasks")
public class AdminTasks {
	private static final Logger log = Logger.getLogger(AdminTasks.class.getName());
	
	private EventService eventService;
	
	@Inject
	public AdminTasks(EventService eventService){
		this.eventService = eventService;
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

}
