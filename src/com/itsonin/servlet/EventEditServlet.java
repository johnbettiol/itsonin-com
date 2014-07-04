package com.itsonin.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.dto.EventInfo;
import com.itsonin.security.AuthContextService;
import com.itsonin.service.EventService;

@SuppressWarnings("serial")
@Singleton
public class EventEditServlet extends DefaultServlet {

	private EventService eventService;

	@Inject
	public EventEditServlet(AuthContextService authContextService,
			EventService eventService) {
		super(authContextService);
		this.eventService = eventService;
	}

	public void doIoiAction(HttpServletRequest req, HttpServletResponse res) {
		EventInfo eventInfo = eventService.info(Long.parseLong(irc.getEventId()), false);
		req.setAttribute("event", eventInfo.getEvent());
	}
}
