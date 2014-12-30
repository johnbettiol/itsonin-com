package com.itsonin.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.dto.EventInfo;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
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
		Guest guest = eventInfo.getGuest();
		Event event = eventInfo.getEvent();
		req.setAttribute("event", event);
		req.setAttribute("guest", guest);
		req.setAttribute("comments", eventInfo.getComments());
		req.setAttribute("guests", eventInfo.getGuests());
		Gson gson = new GsonBuilder().setDateFormat(CustomDateTimeSerializer.ITSONIN_DATES).create();
		req.setAttribute("eventJson", gson.toJson(event));
		req.setAttribute("guestJson", gson.toJson(guest));
		req.setAttribute("commentsJson", gson.toJson(eventInfo.getComments()));
		req.setAttribute("guestsJson", gson.toJson(eventInfo.getGuests()));
		req.setAttribute("shareUrl", req.getScheme() + "://" + req.getServerName() + "/" +
		irc.getLocale() + "/" + irc.getCity() + "/i/" + event.getEventId() + "." + guest.getGuestId());
	}
}
