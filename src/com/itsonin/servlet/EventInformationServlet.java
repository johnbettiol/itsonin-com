package com.itsonin.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.dto.EventInfo;
import com.itsonin.resteasy.CustomDateTimeSerializer;
import com.itsonin.security.AuthContextService;
import com.itsonin.service.EventService;

@SuppressWarnings("serial")
@Singleton
public class EventInformationServlet extends DefaultServlet {

	private EventService eventService;

	@Inject
	public EventInformationServlet(AuthContextService authContextService,
			EventService eventService) {
		super(authContextService);
		this.eventService = eventService;
	}

	public void doIoiAction(HttpServletRequest req, HttpServletResponse res) {
		EventInfo eventInfo = eventService.info(Long.parseLong(irc.getEventId()), false);
		req.setAttribute("event", eventInfo.getEvent());
		req.setAttribute("guest", eventInfo.getGuest());
		req.setAttribute("comments", eventInfo.getComments());
		req.setAttribute("guests", eventInfo.getGuests());
		Gson gson = new GsonBuilder().setDateFormat(CustomDateTimeSerializer.ITSONIN_DATES).create();
		req.setAttribute("eventJson", gson.toJson(eventInfo.getEvent()));
		req.setAttribute("guestJson", gson.toJson(eventInfo.getGuest()));
		req.setAttribute("commentsJson", gson.toJson(eventInfo.getComments()));
	}
}
