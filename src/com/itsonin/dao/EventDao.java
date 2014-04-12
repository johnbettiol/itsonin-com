package com.itsonin.dao;

import static com.itsonin.ofy.OfyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.cmd.Query;
import com.itsonin.entity.Event;
import com.itsonin.enums.EventType;
import com.itsonin.enums.SortOrder;
import com.itsonin.ofy.ObjectifyGenericDao;

/**
 * @author nkislitsin
 *
 */
public class EventDao extends ObjectifyGenericDao<Event>{

	public EventDao() {
		super(Event.class);
	}
	
	public List<Event> list(List<EventType> types, String name, Date startTime,
			Date endTime, String comment, String sortField, SortOrder sortOrder, 
			Integer numberOfLevels, Integer offset, Integer limit) {
		//TODO: improve
		if(startTime != null && endTime != null && startTime.equals(endTime)){
			return listCurrentEvents(types, name, startTime, comment, 
					sortField, sortOrder, numberOfLevels, offset, limit);
		} else if(startTime != null && endTime != null && !startTime.equals(endTime)){
			return listTomorrowEvents(types, name, startTime, endTime, comment, 
					sortField, sortOrder, numberOfLevels, offset, limit);
		}
		
		Query<Event> q = ofy().load().type(clazz);
		
		if(types != null && types.size() != 0){
			q = q.filter("type in", types);
		}
		
		if(name != null){
			q = q.filter("name >=", name).filter("name <=", name);
		}

		if(startTime != null){
			q = q.filter("startTime >", startTime);
		}
			
		if(endTime != null){
			q = q.filter("endTime >", endTime);
		}
		
		if(sortOrder != null && sortField != null && !sortField.isEmpty()){
			if(sortOrder.equals(SortOrder.ASC)){
				q = q.order(sortField);
			}else{
				q = q.order("-" + sortField);
			}
		}
		
		if(offset != null){
			q.offset(offset);
		}
		
		if(offset != null){
			q.limit(limit);
		}

		return q.list();
	}
	
	public List<Event> listCurrentEvents(List<EventType> types, String name, Date now,
			String comment, String sortField, SortOrder sortOrder, 
			Integer numberOfLevels, Integer offset, Integer limit) {
		
		Query<Event> q = ofy().load().type(clazz);
		
		if(types != null && types.size() != 0){
			q = q.filter("type in", types);
		}

		q = q.filter("startTime <", now);
		
		if(sortOrder != null && sortField != null && !sortField.isEmpty()){
			if(sortOrder.equals(SortOrder.ASC)){
				q = q.order(sortField);
			}else{
				q = q.order("-" + sortField);
			}
		}
		
		List<Event> eventList = q.list();
		List<Event> filteredList = new ArrayList<Event>();
		
		for (Event event : eventList) {
			if(event.getEndTime() == null || event.getEndTime().after(now)){
				filteredList.add(event);
			}
		}

		return filteredList;
	}
	
	public List<Event> listTomorrowEvents(List<EventType> types, String name, Date startTime,
			Date endTime, String comment, String sortField, SortOrder sortOrder, 
			Integer numberOfLevels, Integer offset, Integer limit) {
		
		Query<Event> q = ofy().load().type(clazz);
		
		if(types != null && types.size() != 0){
			q = q.filter("type in", types);
		}

		q = q.filter("startTime <", endTime);
		
		if(sortOrder != null && sortField != null && !sortField.isEmpty()){
			if(sortOrder.equals(SortOrder.ASC)){
				q = q.order(sortField);
			}else{
				q = q.order("-" + sortField);
			}
		}
		
		List<Event> eventList = q.list();
		List<Event> filteredList = new ArrayList<Event>();
		
		for (Event event : eventList) {
			if(event.getStartTime() != null && event.getStartTime().after(startTime)){
				filteredList.add(event);
			}
		}

		return filteredList;
	}
	
}
