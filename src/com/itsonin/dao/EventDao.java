package com.itsonin.dao;

import static com.itsonin.ofy.OfyService.ofy;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.cmd.Query;
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

	public List<Event> list(Date date) {
		Query<Event> q = ofy().load().type(clazz);

		if(date != null){
		    Calendar from = Calendar.getInstance();
		    from.setTime(date);
		    from.set(Calendar.HOUR_OF_DAY, 0);
		    from.set(Calendar.MINUTE, 0);
		    from.set(Calendar.SECOND, 0);
		    from.set(Calendar.MILLISECOND, 0);

		    Calendar to = Calendar.getInstance();
		    to.setTime(from.getTime());
		    to.add(Calendar.DAY_OF_MONTH, 1);
		    to.add(Calendar.HOUR_OF_DAY, 4);

			q = q.filter("days >=", from.getTime()).filter("days <=", to.getTime());
		}

		return q.list();
	}

	public List<Event> listFutureEvents() {
	    Calendar from = Calendar.getInstance();
	    from.setTime(new Date());
	    from.set(Calendar.HOUR_OF_DAY, 0);
	    from.set(Calendar.MINUTE, 0);
	    from.set(Calendar.SECOND, 0);
	    from.set(Calendar.MILLISECOND, 0);
		Query<Event> q = ofy().load().type(clazz).filter("days >=", from.getTime());
		return q.list();
	}

	public Event getByUid(String uid) {
		Query<Event> q = ofy().load().type(clazz).filter("uid =", uid);
		List <Event> matches = q.list();
		return matches.size() > 0 ? matches.get(0) : null;
	}
}
