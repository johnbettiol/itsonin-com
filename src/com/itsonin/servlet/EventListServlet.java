package com.itsonin.servlet;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.dto.EventWithGuest;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.EventFlexibility;
import com.itsonin.enums.EventSharability;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.EventCategory;
import com.itsonin.enums.EventSubCategory;
import com.itsonin.enums.EventVisibility;
import com.itsonin.security.AuthContextService;
import com.itsonin.service.EventService;

@SuppressWarnings("serial")
@Singleton
public class EventListServlet extends DefaultServlet {

	private EventService eventService;

	@Inject
	public EventListServlet(AuthContextService authContextService,
			EventService eventService) {
		super(authContextService);
		this.eventService = eventService;
	}

	public void doIoiAction(HttpServletRequest req, HttpServletResponse res) {

		/*
		 * @TODO - We need to move towards having all of the parameters passed
		 * by the IoiRouterContext
		 */
		List<Event> events = eventService.list(true, null, null, null, null,
				null, null, null, null, null, null);
		// temp hack to add an event just for show
		/*Guest guest = new Guest("Guest name");
		Event event = new Event(EventCategory.GOTO, EventSubCategory.PARTY, EventSharability.NORMAL,
				EventVisibility.PUBLIC, EventStatus.ACTIVE,
				EventFlexibility.NEGOTIABLE, "event title" + events.size(),
				"event description", "event notes", new Date(), new Date(),
				1.0d, 2.0d, "location.url", "location title",
				"location address", new Date());
		eventService.create(event, guest);
		events = eventService.list(true, null, null, null, null, null, null,
				null, null, null, null);*/
		req.setAttribute("events", events);
		
		req.setAttribute("eventCategories", EventCategory.values());
		req.setAttribute("eventSubCategories", EventSubCategory.values());
	}

}
