package com.itsonin.service;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.itsonin.dao.EventDao;
import com.itsonin.entity.Event;
import com.itsonin.security.AuthContextService;

/**
 * @author nkislitsin
 * 
 */
public class AdminService {

	private EventDao eventDao;
	private AuthContextService authContextService;

	@Inject
	public AdminService(EventDao eventDao, AuthContextService authContextService) {
		this.eventDao = eventDao;
		this.authContextService = authContextService;
	}

	public List<Event> list(String city) {//TODO: filter by city
		return eventDao.list();
	}

	public Event save(Event event) {
		return eventDao.saveAndGet(event);
	}

	public void remove(List<Long> ids) {
		List<Key<Event>> keys = new ArrayList<Key<Event>>();
		for(Long id : ids) {
			keys.add(Key.create(Event.class, id));
		}
		eventDao.deleteAll(keys);
	}
}
