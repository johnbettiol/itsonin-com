package com.itsonin.servlet;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
	private static final DateFormat ymdf = SafeDateFormat.forPattern("yyyy-MM-dd");
	private static final DateFormat mdf = SafeDateFormat.forPattern("MMMM dd");
	private static final DateFormat emdf = SafeDateFormat.forPattern("EEEE MMMM dd");

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
		String prettyDate = null;
		boolean favourites = false;
		boolean offers = false;
		boolean hot = false;

		if("Yesterday".equals(dateFilter)) {
			date = DateUtils.addDays(date, -1);
			prettyDate = "Yesterday " + mdf.format(date);
		} else if("Today".equals(dateFilter)) {
			date = new Date();
			prettyDate = "Today " + mdf.format(date);
		} else if("Tomorrow".equals(dateFilter)) {
			date = DateUtils.addDays(date, 1);
			prettyDate = "Tomorrow " + mdf.format(date);
		} else if ("Favourites".equals(dateFilter)) {
			favourites = true;
			prettyDate = "Favourites";
		} else if ("Offers".equals(dateFilter)) {
			offers = true;
			prettyDate = "Offers";
		} else if ("Hot".equals(dateFilter)) {
			hot = true;
			prettyDate = "Hot";
		} else if (!StringUtils.isBlank(dateFilter)) {
			try {
				date = ymdf.parse(dateFilter);
				prettyDate = emdf.format(date);
			} catch (ParseException e) {
				//do nothing
			}
		} else {
			prettyDate = "Today " + mdf.format(date);
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
		req.setAttribute("categoryUrls", getCategoryUrls());
		req.setAttribute("prettyDate", prettyDate);
	}

	private Map<String, String> getCategoryUrls() {
		Map<String, String> categoryUrls = new HashMap<String, String>();
		String strDate = (irc.getDateFilter() == null) ? "Today" : irc.getDateFilter();
		for(EventCategory eventCategory : EventCategory.values()) {
			List<String> cats = new ArrayList<String>();
			boolean contains = false;
			if(irc.getCategoryFilter() != null) {
				for(EventCategory filterCategory : irc.getCategoryFilter()) {
					if(eventCategory != filterCategory) {
						cats.add(StringUtils.capitalize(filterCategory.toString().toLowerCase()));
					} else {
						contains = true;
					}
				}
			}
			if(!contains) {
				cats.add(StringUtils.capitalize(eventCategory.toString().toLowerCase()));
			}
			String url = String.format("/%s/%s/Events/%s/%s", irc.getLocale(),
					irc.getCity(), strDate, StringUtils.join(cats, ","));
			categoryUrls.put(eventCategory.toString(), url);
		}
		return categoryUrls;
	}
}
