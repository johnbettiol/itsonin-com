package com.itsonin.dao;

import static com.itsonin.ofy.OfyService.ofy;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.cmd.Query;
import com.itsonin.entity.Guest;
import com.itsonin.enums.GuestType;
import com.itsonin.enums.SortOrder;
import com.itsonin.ofy.ObjectifyGenericDao;

/**
 * @author nkislitsin
 *
 */
public class GuestDao extends ObjectifyGenericDao<Guest>{

	public GuestDao() {
		super(Guest.class);
	}

	public List<Guest> list(Long eventId, String name, Date created, String sortField,
			SortOrder sortOrder, Integer numberOfLevels, Integer offset, Integer limit) {
		Query<Guest> q = ofy().load().type(clazz);
		
		q.filter("eventId", eventId);
		
		if(name != null)
			q = q.filter("name >=", name).filter("name <=", name);
		
		if(created != null)
			q = q.filter("created", created);
		
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
	
	public Long getHostGuestForEvent(Long eventId){
		Guest guest = ofy().load().type(Guest.class).filter("eventId", eventId)
				.filter("type", GuestType.HOST).first().now();
		
		if(guest == null){
			return null;
		}else{
			return guest.getGuestId();
		}
	}
	
}
