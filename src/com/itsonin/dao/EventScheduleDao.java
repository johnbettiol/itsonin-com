package com.itsonin.dao;

import com.itsonin.entity.EventSchedule;

/**
 * @author nkislitsin
 *
 */
public class EventScheduleDao extends ObjectifyGenericDao<EventSchedule>{

	public EventScheduleDao() {
		super(EventSchedule.class);
	}
	
}
