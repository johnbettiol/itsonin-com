package com.itsonin.api;

import javax.ws.rs.Path;

import com.google.inject.Inject;
import com.itsonin.dao.EventScheduleDao;

/**
 * @author nkislitsin
 *
 */
@Path("/api")
public class EventScheduleApi {
	
	private EventScheduleDao eventScheduleDao;
	
	@Inject
	public EventScheduleApi(EventScheduleDao eventScheduleDao){
		this.eventScheduleDao = eventScheduleDao;
	}

}
