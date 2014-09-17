package com.itsonin.crawl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.code.geocoder.Geocoder;
import com.itsonin.entity.Event;
import com.itsonin.enums.EventFlexibility;
import com.itsonin.enums.EventSharability;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.EventSubCategory;
import com.itsonin.enums.EventVisibility;
import com.itsonin.utils.DateTimeUtil;

public class EventimSeeder extends EventSeederBase {

	private static final String STR_ORDER_TICKETS = " - Order tickets";
	private static final String SOURCE = "eventim";
	private static final String BASE_URL = "http://www.eventim.de/";
	private static final String START_URL = BASE_URL
			+ "/duesseldorf?language=en&sort_by_event=event_datum"; // http://www.eventim.de/duesseldorf?language=en
	private static final String SAFARI_USERAGENT = "Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5355d Safari/8536.25";

	private static final SimpleDateFormat parserSDF = new SimpleDateFormat(
			"MM/dd/yy / h:mm a");
	private static final Date now = new Date();

	public static void main(String[] args) {
		new EventimSeeder().getNewEvents();

	}

	public ArrayList<Event> getNewEvents() {
		String startPage = START_URL;
		Document doc;
		Geocoder geocoder = new Geocoder();
		ArrayList<Event> eventsList = new ArrayList<Event>();
		while (startPage != null) {
			try {
				doc = Jsoup.connect(startPage).userAgent(SAFARI_USERAGENT)
						.get();
				String title = doc.title();
				System.out.println(title);

				Element content = doc.getElementsByTag("table").first();
				Elements events = content.getElementsByTag("tr");
				for (Element event : events) {
					Event newEvent = getEventData(event);
					if (newEvent != null) {
						addGeoCodeData(newEvent, "en");
						eventsList.add(newEvent);
					}
				}

				Element nextUrlElem = doc.getElementsByAttributeValue("name",
						"Pager_Next_Button").first();
				startPage = nextUrlElem != null ? BASE_URL + "/"
						+ nextUrlElem.attr("href") + "&language=en" : null;
				// temporary hack to only run once!
				startPage = null;
				System.out.println("next: " + startPage);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				startPage = null;
			}
		}
		return eventsList;
	}

	private static Event getEventData(Element event) {
		String eventIconImage = null;
		String eventHref = null;
		String eventTitle = null;
		String eventSummary = null;
		String eventDescription = null;
		String eventNotes = null;
		String eventDateStr = null;
		Date eventDateStart = null;
		Date eventDateEnd = null;
		Double eventGpsLat = null;
		Double eventGpsLong = null;
		String eventLocationTitle = null;
		String eventLocationUrl = null;
		String eventLocationAddress = null;
		Elements columns = event.getElementsByTag("td");
		for (Element column : columns) {
			if (column.hasClass("taImage")) {
				Elements imgElems = column.getElementsByTag("img");
				if (imgElems.size() > 0) {
					eventIconImage = BASE_URL + imgElems.first().attr("src");
				}
			}
			if (column.hasClass("taEvent")) {
				Element linkElem = column.getElementsByTag("h4").first()
						.getElementsByAttribute("href").first();
				eventHref = BASE_URL + "/" + linkElem.attr("href");
				eventTitle = linkElem.attr("title");
				Element plElem = column.getElementsByTag("dl").first();
				Element pldtElem = plElem.getElementsByTag("dt").first();
				eventLocationTitle = pldtElem.text();
				eventDateStr = column.getElementsByTag("abbr").first().text()
						.trim();
			}
		}
		if (eventTitle != null) {
			int oticksIndex = eventTitle.indexOf(STR_ORDER_TICKETS);
			if (oticksIndex > 0) {
				eventTitle = eventTitle.substring(0, oticksIndex).trim();
			}
		}
		if (eventDateStr != null) {
			try {
				eventDateStart = parserSDF.parse(eventDateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (eventHref != null) {
			try {
				Document eventDetailsDoc = Jsoup.connect(eventHref)
						.userAgent(SAFARI_USERAGENT).get();
				Element locationAddress = eventDetailsDoc.getElementsByClass(
						"location").first();

				if (locationAddress != null) {
					eventLocationAddress = org.apache.commons.lang3.StringEscapeUtils
							.unescapeHtml4(locationAddress.text());

					System.out.println("eventIcon:       " + eventIconImage);
					System.out.println("href:            " + eventHref);
					System.out.println("title:           " + eventTitle);
					System.out
							.println("location:        " + eventLocationTitle);
					System.out.println("locationAddress: "
							+ eventLocationAddress);
					System.out.println("dateStr:         " + eventDateStr);
					System.out.println("start date:      " + eventDateStart);
					System.out.println("end date:        " + eventDateEnd);
					System.out.println("-----------------------------------");

					Event result = new Event(EventSubCategory.CONCERT,
							EventSharability.NORMAL, EventVisibility.PUBLIC,
							EventStatus.ACTIVE, EventFlexibility.FIXED,
							eventTitle, eventSummary, eventDescription, eventNotes,
							eventDateStart, eventDateEnd,
							DateTimeUtil.getDaysBetweenDates(eventDateStart, eventDateEnd),
							eventGpsLat,
							eventGpsLong, eventLocationUrl, eventLocationTitle,
							eventLocationAddress, now, SOURCE);
					return result;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}
}
