package com.itsonin.ofy;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.itsonin.entity.Comment;
import com.itsonin.entity.Counter;
import com.itsonin.entity.Device;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;

/**
 * @author nkislitsin
 *
 */
public class OfyService {
	static {
        register(Comment.class);
        register(Counter.class);
        register(Device.class);
        register(Event.class);
        register(Guest.class);
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
