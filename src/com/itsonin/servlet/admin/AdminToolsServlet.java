package com.itsonin.servlet.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.itsonin.crawl.DusEventimSeeder;
import com.itsonin.crawl.DusPrinzDeSeeder;
import com.itsonin.crawl.DusTourismoSeeder;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.EventFlexibility;
import com.itsonin.enums.EventSharability;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.EventSubCategory;
import com.itsonin.enums.EventVisibility;
import com.itsonin.enums.GuestStatus;
import com.itsonin.security.AuthContextService;
import com.itsonin.service.CommentService;
import com.itsonin.service.DataImportService;
import com.itsonin.service.DeviceService;
import com.itsonin.service.EventService;
import com.itsonin.servlet.DefaultServlet;
import com.itsonin.utils.DateTimeUtil;

@SuppressWarnings("serial")
@Singleton
public class AdminToolsServlet extends DefaultServlet {

	private EventService eventService;
	private DeviceService deviceService;
	private CommentService commentService;
	private DataImportService dis;

	@Inject
	public AdminToolsServlet(AuthContextService authContextService,
			EventService eventService, DeviceService deviceService, CommentService commentService, 
			DataImportService dis) {
		super(authContextService);
		this.eventService = eventService;
		this.deviceService = deviceService;
		this.commentService = commentService;
		this.dis = dis;
	}

	public void doIoiAction(HttpServletRequest req, HttpServletResponse res) {
		if (irc.getCommand() == null) {
			return;
		}
		Guest guest = new Guest("Public Event");
		switch (irc.getCommand()) {
		case "UpdateAccount":
			irc.getDevice().setLevel(DeviceLevel.valueOf(req.getParameter("level")));
			deviceService.updateDevice(irc.getDevice());
			req.setAttribute("message","Device Level Updated");
			break;
		case "QuickSeed":
			List<Double> lats = Arrays.asList(51.2384547, 51.218514, 51.2272899, 51.2201704, 51.2528229);
			List<Double> longs = Arrays.asList(6.8143503, 6.7707483, 6.7725422, 6.772928, 6.7782096);
			for(int i=1;i<=5;i++) {
				Event event = new Event(EventSubCategory.PARTY, EventSharability.NORMAL,
						EventVisibility.PUBLIC, EventStatus.ACTIVE,
						EventFlexibility.NEGOTIABLE, "Germany vs Argentina party " + i, "event summary", 
						"event description", "event notes", new Date(), new Date(), DateTimeUtil.getDaysBetweenDates(new Date(), new Date()),
						lats.get(i-1), longs.get(i-1), "location.url", "ratinger Straße",
						"location address", new Date(), "qseed");
				event.setEventId(Long.valueOf(i));
				eventService.create(event, guest);
			}
			break;
		case "EventimSeed":
			guest.setStatus(GuestStatus.YES);
			ArrayList<Event> eventsListE = new DusEventimSeeder().getNewEvents();
			for (Event event : eventsListE) {
				Map<String, Object> created = eventService.create(event, guest);
			}
			break;
		case "PrinzSeed":
			guest.setStatus(GuestStatus.YES);
			ArrayList<Event> eventsListP = new DusPrinzDeSeeder().getNewEvents();
			for (Event event : eventsListP) {
				Map<String, Object> created = eventService.create(event, guest);
			}
			break;		
		case "DusSeed":
				guest.setStatus(GuestStatus.YES);
				ArrayList<Event> eventsListD = new DusTourismoSeeder().getNewEvents();
				for (Event event : eventsListD) {
					Map<String, Object> created = eventService.create(event, guest);
				}
				break;
		case "ImportData":
			if (req.getParameter("type") == null) {
				req.setAttribute("message","No upload type specified!");	
			}
			if (req.getParameter("dataType") == null) {
				req.setAttribute("message","No data type specified!");
			}
			switch (req.getParameter("type")) {
			case "default":
				req.setAttribute("result", dis.fromDefaultFiles(irc));
				break;
			case "upload":
				String [] uploadedFiles = (String []) req.getAttribute("_rmFiles");
				if (uploadedFiles == null || uploadedFiles.length ==0) {
					req.setAttribute("message","No files uploaded!");
					break;
				}
				req.setAttribute("result", dis.fromFiles(irc, uploadedFiles));
			case "download":
				if (req.getParameter("url") == null) {
					req.setAttribute("message","No url specified!");
					break;
				}
				req.setAttribute("results", dis.fromUrls(irc, req.getParameterValues("url")));
				break;
			case "text":
				if (req.getParameter("data") == null) {
					req.setAttribute("message","No input data provided specified!");
					break;
				}
				req.setAttribute("results", dis.fromStrings(irc, req.getParameterValues("data")));
				break;
			}
			break;
		}
	}
}
