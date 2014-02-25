package com.itsonin.dao;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.itsonin.entity.Comment;
import com.itsonin.entity.Event;
import com.itsonin.entity.EventSchedule;
import com.itsonin.entity.Guest;
import com.itsonin.entity.User;

/**
 * @author nkislitsin
 *
 */
public class OfyService {
	static {
        register(Comment.class);
        register(Event.class);
        register(EventSchedule.class);
        register(Guest.class);
        register(User.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
    
    public static void register(Class<?> clazz){
    	factory().register(clazz);
    }
}
