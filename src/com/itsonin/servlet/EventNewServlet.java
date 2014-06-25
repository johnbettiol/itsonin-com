package com.itsonin.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.enums.EventCategory;
import com.itsonin.enums.EventSubCategory;
import com.itsonin.security.AuthContextService;
import com.itsonin.service.EventService;

@SuppressWarnings("serial")
@Singleton
public class EventNewServlet extends DefaultServlet {

	@Inject
	public EventNewServlet(AuthContextService authContextService,
			EventService eventService) {
		super(authContextService);
	}

	public void doIoiGet(HttpServletRequest req, HttpServletResponse res) {
		req.setAttribute("eventCategories", EventCategory.values());
		req.setAttribute("eventSubCategories", EventSubCategory.values());
	}

}
