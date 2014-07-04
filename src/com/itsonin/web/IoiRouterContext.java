package com.itsonin.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import com.itsonin.entity.Device;
import com.itsonin.enums.DeviceLevel;

public class IoiRouterContext {

	public String getLocale() {
		return locale;
	}

	public String getCity() {
		return city;
	}

	public String getEventId() {
		return eventId;
	}

	public String getInviterId() {
		return inviterId;
	}

	public String getLocationFilter() {
		return locationFilter;
	}

	public String getDateFilter() {
		return dateFilter;
	}

	public String getCategoryFilter() {
		return categoryFilter;
	}

	public String getSubCategoryFilter() {
		return subCategoryFilter;
	}

	public String getOffsetFilter() {
		return offsetFilter;
	}

	public boolean isCitySupported() {
		return citySupported;
	}

	public boolean isMyEvents() {
		return myEvents;
	}

	public Device getDevice() {
		return device;
	}

	public String getCommand() {
		return command;
	}

	private static final String REQ_HEADER_ACCEPT_LANGUAGE = "Accept-Language";

	private static final String URI_ALIASES_EVENTS = ",Events";
	private static final String URI_ALIASES_MYEVENTS = ",MyEvents";
	private static final String URI_PART_ADMIN = "Admin";
	private static final String URI_PART_TOOLS = "Tools";
	private static final String URI_PART_WELCOME = "Welcome";
	private static final String URI_PART_INVITATION_BASE = "i";
	private static final String URI_PART_EVENT_BASE = "e";

	private static final String LOCALE_EN = "en";
	private static final String LOCALE_DE = "de";
	private static final String LOCALE_DEFAULT = LOCALE_EN;
	private static final String LOCALE_ALLOWED = "," + LOCALE_EN + ","
			+ LOCALE_DE;

	private static final String CITY_DUS = "Düsseldorf";
	private static final String CITY_AUS = "Austin";
	private static final String CITY_ADMIN = "Admin";
	private static final String CITY_DEFAULT = CITY_DUS;
	private static final String CITY_ALLOWED = "," + CITY_DUS + "," + CITY_AUS
			+ "," + CITY_ADMIN;

	public enum IoiActionType {
		E_400, E_403, E_404, CITY_NOT_FOUND, WELCOME, EVENT_INVITATION, EVENT_INFORMATION, EVENT_NEW, EVENT_EDIT, EVENT_LIST, ADMIN, TOOLS
	}

	private IoiActionType actionType;
	private String destination, locale, city, eventId, inviterId,
			locationFilter, dateFilter, categoryFilter, subCategoryFilter,
			offsetFilter, command;
	private boolean citySupported, myEvents, doRedirect;
	private Device device;

	public IoiRouterContext(HttpServletRequest req) {
		this(req, null);
	}

	public IoiRouterContext(HttpServletRequest req, Device device) {
		this.device = device;
		parseActionData(req);
	}

	private void parseActionData(HttpServletRequest req) {
		String requestUri = decode(req.getRequestURI());
		String requestLocaleHeader = req.getHeader(REQ_HEADER_ACCEPT_LANGUAGE);
		this.actionType = IoiActionType.EVENT_LIST; // Default Action Type
		String[] requestChunks = requestUri.split("/");

		// If someone accesses the website root directly, send them to the
		// default city index URL
		if (null == requestUri || "/".equals(requestUri)
				|| requestChunks == null || requestChunks.length == 0) {
			setLocaleByBrowser(requestLocaleHeader);
			setDefaultCity();
			return;
		}

		String localeChunk = requestChunks[1].toLowerCase();
		// If locale not detected, redirect to default start page
		if (localeChunk.length() != 2
				|| LOCALE_ALLOWED.indexOf("," + localeChunk) < 0) {
			setLocaleByBrowser(requestLocaleHeader);
			setDefaultCity();
			return;
		} else {
			locale = localeChunk;
		}

		// If no city provided, redirect to default city page
		if (requestChunks.length == 2) {
			setDefaultCity();
			return;
		}

		city = requestChunks[2];
		// If city not valid, redirect to city not found page
		if (CITY_ALLOWED.indexOf("," + city) < 0) {
			this.actionType = IoiActionType.CITY_NOT_FOUND;
			return;
		}
		if (requestChunks.length == 2) {
			return;
		}

		// / en / Düsseldorf / Events / Bilk / 514 / Meet / Cultural,Conventions
		// / en / Düsseldorf / Events / new
		// / en / Düsseldorf / i / 45.1
		// / en / Düsseldorf / e / 45

		String actionTypeName = requestChunks[3];
		if (URI_PART_EVENT_BASE.equals(actionTypeName)) {
			parseEventInfoData(requestChunks);
		} else if (URI_PART_INVITATION_BASE.equals(actionTypeName)) {
			parseEventInvitationData(requestChunks);
		} else if (URI_ALIASES_EVENTS.indexOf("," + actionTypeName) >= 0) {
			this.myEvents = false;
			this.actionType = IoiActionType.EVENT_LIST;
			parseEventListData(requestChunks);
		} else if (URI_ALIASES_MYEVENTS.indexOf("," + actionTypeName) >= 0) {
			this.myEvents = true;
			this.actionType = IoiActionType.EVENT_LIST;
			parseEventListData(requestChunks);
		} else if (URI_PART_WELCOME.equals(actionTypeName)) {
			this.actionType = IoiActionType.WELCOME;
		} else if (city.equals(CITY_ADMIN)
				&& URI_PART_TOOLS.equals(actionTypeName)) {
			// later we restrict with basic auth
			this.actionType = IoiActionType.TOOLS;
			this.command = requestChunks.length > 4 ? requestChunks[4] : null;
		} else if (URI_PART_ADMIN.equals(actionTypeName)) {
			this.actionType = device != null
					&& device.getLevel().getLevel() >= DeviceLevel.ADMIN
							.getLevel() ? IoiActionType.ADMIN
					: IoiActionType.E_403;
			this.command = requestChunks.length > 4 ? requestChunks[4] : null;
		} else {
			// If we cannot recognize the action type then we bork!
			this.actionType = IoiActionType.E_400;
		}
		return;
	}

	private void setDefaultCity() {
		city = CITY_DEFAULT;
		actionType = IoiActionType.EVENT_LIST;
		destination = getPublicRoute(this.actionType);
		doRedirect = true;
	}

	private void parseEventListData(String[] requestChunks) {
		if (requestChunks.length >= 4) {
			locationFilter = requestChunks[3];
		}
		if (requestChunks.length >= 5) {
			dateFilter = requestChunks[4];
		}
		if (requestChunks.length >= 6) {
			categoryFilter = requestChunks[5];
		}
		if (requestChunks.length >= 7) {
			subCategoryFilter = requestChunks[6];
		}
		if (requestChunks.length >= 8) {
			offsetFilter = requestChunks[7];
		}
	}

	private void parseEventInvitationData(String[] requestChunks) {
		String eventIdAndInvitation = requestChunks[4];
		if (requestChunks.length != 5 || "".equals(eventIdAndInvitation)) {
			this.actionType = IoiActionType.E_400;
		} else {
			String[] eaiChunks = eventIdAndInvitation.split("\\.");
			if (eaiChunks.length != 2 || "".equals(eaiChunks[0])
					|| "".equals(eaiChunks[1])) {
				this.actionType = IoiActionType.E_400;
			} else {
				this.eventId = eaiChunks[0];
				this.inviterId = eaiChunks[1];
				this.actionType = IoiActionType.EVENT_INVITATION;
			}
		}
	}

	private void parseEventInfoData(String[] requestChunks) {
		String eventId = requestChunks[4];
		if (requestChunks.length != 5 || "".equals(eventId)) {
			this.actionType = IoiActionType.E_400;
		} else if ("add".equals(eventId)) {
			this.actionType = IoiActionType.EVENT_NEW;
		} else {
			this.eventId = eventId;
			this.actionType = IoiActionType.EVENT_INFORMATION;
		}
	}

	private void setLocaleByBrowser(String localeHeader) {
		if (localeHeader != null && localeHeader.length() >= 2) {
			String tmpLocale = localeHeader.substring(0, 2).toLowerCase();
			if (LOCALE_ALLOWED.indexOf("," + tmpLocale) >= 0) {
				locale = tmpLocale;
			}
		}
		if (locale == null) {
			locale = LOCALE_DEFAULT;
		}
	}

	public String getInternalServlet() {
		switch (actionType) {
		case EVENT_INFORMATION:
			return "/EventInformationServlet";
		case EVENT_INVITATION:
			return "/EventInvitationServlet";
		case EVENT_NEW:
			return "/EventNewServlet";
		case EVENT_LIST:
			return "/EventListServlet";
		case TOOLS:
			return "/AdminToolsServlet";
		default:
			return "/DefaultServlet";
		}
	}

	public String getInternalRoute() {
		String locale = this.locale != null ? this.locale : LOCALE_DEFAULT;
		String uri;
		switch (actionType) {
		case EVENT_NEW:
			uri = "/dynamic/" + locale + "/city/event/new.jsp";
			break;
		case EVENT_INFORMATION:
			uri = "/dynamic/" + locale + "/city/event/info.jsp";
			break;
		case EVENT_INVITATION:
			uri = "/dynamic/" + locale + "/city/event/invitation.jsp";
			break;
		case EVENT_LIST:
			uri = "/dynamic/" + locale + "/city/event/list.jsp";
			break;
		case WELCOME:
			uri = "/dynamic/" + locale + "/city/welcome.jsp";
			break;
		case TOOLS:
			uri = "/dynamic/" + locale + "/admin/tools/index.jsp";
			break;
		case CITY_NOT_FOUND:
			uri = "/dynamic/" + locale + "/city/cityNotFound.jsp";
			break;
		case ADMIN:
			uri = "/dynamic/" + locale + "/city/admin/index.jsp";
			break;
		case E_400:
			uri = "/dynamic/" + locale + "/e_400.jsp";
			break;
		case E_403:
			uri = "/dynamic/" + locale + "/e_403.jsp";
			break;
		case E_404:
		default:
			uri = "/dynamic/" + locale + "/e_404.jsp";
			break;
		}
		return uri;
	}

	public String getPublicRoute() {
		return getPublicRoute(actionType);
	}

	public String getPublicRoute(IoiActionType actionType) {
		String locale = this.locale != null ? this.locale : LOCALE_DEFAULT;
		String city = this.city != null ? this.city : CITY_DEFAULT;
		String uri = "/";
		switch (actionType) {
		case EVENT_LIST:
			uri = "/" + locale + "/" + encodeValue(city) + "/Events";
			break;
		case WELCOME:
			uri = "/" + locale + "/" + encodeValue(city) + "/Welcome";
			break;
		case CITY_NOT_FOUND:
			uri = "/" + locale + "/" + encodeValue(city) + "/CityNotFound";
			break;
		case E_403:
			uri = "/" + locale + "/Forbidden";
			break;
		case E_404:
			uri = "/" + locale + "/FileNotFound";
			break;
		case E_400:
		default:
			uri = "/" + locale + "/InvalidRequest";
			break;
		}

		return uri;
	}

	private String encodeValue(String source) {
		try {
			return URLEncoder.encode(source, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return source;
	}

	private String decode(String source) {
		try {
			return URLDecoder.decode(source, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return source;
	}

	public boolean citySupported() {
		return citySupported;
	}

	public IoiActionType getActionType() {
		return actionType;
	}

	public String getDestination() {
		return destination;
	}

	public boolean doRedirect() {
		return doRedirect;
	}

}
