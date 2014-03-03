package com.itsonin.dao;

import static com.itsonin.ofy.OfyService.ofy;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.cmd.Query;
import com.itsonin.entity.Event;
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
	
	public List<Event> list(Long deviceId,String name, Date created, String comment, String sortField,
			SortOrder sortOrder, Integer numberOfLevels, Integer offset, Integer limit) {
		Query<Event> q = ofy().load().type(clazz);
		
		if(name != null)
			q = q.filter("name >=", name).filter("name <=", name);
		
		if(created != null)
			q = q.filter("created", created);
		
		if(name != null)
			q = q.filter("comment >=", comment).filter("comment <=", comment);
		
		if(sortOrder != null && sortField != null && !sortField.isEmpty()){
			if(sortOrder.equals(SortOrder.ASC))
				q.order(sortField);
			else
				q.order("-" + sortField);
		}
		
		if(offset != null)
			q.offset(offset);
		
		if(offset != null)
			q.limit(limit);
		
//TODO: number of levels
		return q.list();
	}
	
}
