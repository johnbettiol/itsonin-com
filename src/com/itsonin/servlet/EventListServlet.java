package com.itsonin.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.EventCategory;
import com.itsonin.enums.EventFlexibility;
import com.itsonin.enums.EventSharability;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.EventSubCategory;
import com.itsonin.enums.EventVisibility;
import com.itsonin.resteasy.CustomDateTimeSerializer;
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
		List<Event> events = new ArrayList<Event>();
		//eventService.list(true, null, null, null, null,
		//		null, null, null, null, null, null);
		// temp hack to add an event just for show
		
		List<Double> lats = Arrays.asList(51.2384547, 51.218514, 51.2272899, 51.2201704, 51.2528229);
		List<Double> longs = Arrays.asList(6.8143503, 6.7707483, 6.7725422, 6.772928, 6.7782096);
		
		for(int i=1;i<=5;i++) {
			Guest guest = new Guest("Guest name");
			Event event = new Event(EventCategory.GOTO, EventSubCategory.PARTY, EventSharability.NORMAL,
					EventVisibility.PUBLIC, EventStatus.ACTIVE,
					EventFlexibility.NEGOTIABLE, "Germany vs Argentina party number " + i,
					"event description", "event notes", new Date(), new Date(),
					lats.get(i-1), longs.get(i-1), "location.url", "ratinger Straße",
					"location address", new Date());
			event.setEventId(Long.valueOf(i));
			events.add(event);		
		}
		//eventService.create(event, guest);
		//events = eventService.list(true, null, null, null, null, null, null,
		//		null, null, null, null);
		req.setAttribute("events", events);
		
		Gson gson = new GsonBuilder().setDateFormat(CustomDateTimeSerializer.ITSONIN_DATES).create();
		req.setAttribute("eventsJson", gson.toJson(events));

		req.setAttribute("eventCategories", EventCategory.values());
		req.setAttribute("eventSubCategories", EventSubCategory.values());
	}

}
