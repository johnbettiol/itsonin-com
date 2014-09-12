package com.itsonin.servlet;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.entity.Event;
import com.itsonin.enums.EventCategory;
import com.itsonin.enums.EventSubCategory;
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
		List<Event> events = eventService.list(true, null, null,
				new Date(), null, null, null, null);

		req.setAttribute("events", events);
		
		Gson gson = new GsonBuilder().setDateFormat(CustomDateTimeSerializer.ITSONIN_DATES).create();
		req.setAttribute("eventsJson", gson.toJson(events));

		req.setAttribute("eventCategories", EventCategory.values());
		req.setAttribute("eventSubCategories", EventSubCategory.values());
	}

}
