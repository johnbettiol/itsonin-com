package com.itsonin.servlet;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.dto.EventInfo;
import com.itsonin.entity.Comment;
import com.itsonin.entity.Guest;
import com.itsonin.enums.GuestStatus;
import com.itsonin.enums.GuestType;
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
		Gson gson = new GsonBuilder().setDateFormat(CustomDateTimeSerializer.ITSONIN_DATES).create();
		req.setAttribute("eventJson", gson.toJson(eventInfo.getEvent()));

		Guest guest = new Guest(1L, 1L, 1L, "Nikolai", GuestType.GUEST,
			GuestStatus.ATTENDING, new Date());
		eventInfo.setGuests(Arrays.asList(guest));
		Comment comment1 = new Comment(1L, 1L, 1L, "is there a dress code?", new Date());
		Comment comment2 = new Comment(1L, 1L, 1L, "do we have to pay entrance?", new Date());
		eventInfo.setComments(Arrays.asList(comment1, comment2));

		req.setAttribute("comments", eventInfo.getComments());
		req.setAttribute("guests", eventInfo.getGuests());
	}
}
