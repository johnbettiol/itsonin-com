package com.itsonin.service;

import java.util.List;

import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.dao.EventDao;
import com.itsonin.entity.Event;

/**
 * @author nkislitsin
 *
 */
public class EventService {

	private EventDao eventDao;
	
	@Inject
	public EventService(EventDao eventDao){
		this.eventDao = eventDao;
	}

	public Event create(Event event) {
		eventDao.save(event);
		return event;
	}
	
	public Response update(String id) {
		return Response.ok().build();
	}
	
	public Response attend(String id) {
		return Response.ok().build();
	}
	
	public List<Event> list() {
		return eventDao.list();
	}
	
	public Response cancel(String eventId, String guestId) {
		return Response.ok().build();
	}

	public Response decline(String eventId, String guestId) {
		return Response.ok().build();
	}

	public Response invite(String eventId, String guestId) {
		return Response.ok().build();
	}
	
	public Event update(String eventId, String guestId) {
		return null;
	}
	
}
