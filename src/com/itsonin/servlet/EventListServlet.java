package com.itsonin.servlet;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;

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
import com.itsonin.utils.SafeDateFormat;

@SuppressWarnings("serial")
@Singleton
public class EventListServlet extends DefaultServlet {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(EventListServlet.class.getName());
	private static final DateFormat df = SafeDateFormat.forPattern("yyyy-MM-dd");

	private EventService eventService;

	@Inject
	public EventListServlet(AuthContextService authContextService,
			EventService eventService) {
		super(authContextService);
		this.eventService = eventService;
	}

	public void doIoiAction(HttpServletRequest req, HttpServletResponse res) {

		String dateFilter = irc.getDateFilter();
		Date date = new Date();
		boolean favourites = false;
		boolean offers = false;
		boolean hot = false;

		if("Yesterday".equals(dateFilter)) {
			date = DateUtils.addDays(date, -1);
		} else if("Today".equals(dateFilter)) {
			date = new Date();
		} else if("Tomorrow".equals(dateFilter)) {
			date = DateUtils.addDays(date, 1);
		} else if ("Favourites".equals(dateFilter)) {
			favourites = true;
		} else if ("Offers".equals(dateFilter)) {
			offers = true;
		} else if ("Hot".equals(dateFilter)) {
			hot = true;
		} else if ( dateFilter != null) {
			try {
				date = df.parse(dateFilter);
			} catch (ParseException e) {
				//do nothing
			}
		}

		List<Event> events;
		if(favourites == true || offers == true || hot == true) {
			events = eventService.listFutureEvents();
		} else {
			events = eventService.listByDate(DateUtils.truncate(date, java.util.Calendar.DAY_OF_MONTH));
		}
		
		List<Event> filteredEvents = eventService.filter(events, irc.getCategoryFilter(),
				hot, offers, favourites);

		req.setAttribute("events", filteredEvents);
		
		Gson gson = new GsonBuilder().setDateFormat(CustomDateTimeSerializer.ITSONIN_DATES).create();
		req.setAttribute("eventsJson", gson.toJson(events));

		req.setAttribute("eventCategories", EventCategory.values());
		req.setAttribute("eventSubCategories", EventSubCategory.values());

	}
}
