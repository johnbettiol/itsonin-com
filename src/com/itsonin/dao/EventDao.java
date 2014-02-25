package com.itsonin.dao;

import com.itsonin.entity.Event;
import com.itsonin.ofy.ObjectifyGenericDao;

/**
 * @author nkislitsin
 *
 */
public class EventDao extends ObjectifyGenericDao<Event>{

	public EventDao() {
		super(Event.class);
	}
	
}
