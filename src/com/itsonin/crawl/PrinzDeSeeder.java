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

import com.itsonin.entity.Event;
import com.itsonin.enums.EventFlexibility;
import com.itsonin.enums.EventSharability;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.EventSubCategory;
import com.itsonin.enums.EventVisibility;

public class PrinzDeSeeder extends EventSeederBase {

	// @NOTE not working yet!

	private static final int MAX_DATES_TO_LOAD = 10;
	private static final String CATEGORY_STR = "kategorie/";
	private static final String STR_ORDER_TICKETS = " - Order tickets";
	private static final String BASE_WEBSITE = "http://prinz.de";
	private static final String START_URL = BASE_WEBSITE
			+ "/duesseldorf/events/"; // http://www.eventim.de/duesseldorf?language=en
	private static final String SAFARI_USERAGENT = "Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5355d Safari/8536.25";

	// 2014/09/02/
	private static final SimpleDateFormat parserSDF = new SimpleDateFormat(
			"yyyy/MM/dd");

	private static final SimpleDateFormat parserSDFWithTime = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm");

	public static void main(String[] args) {
		new PrinzDeSeeder().getNewEvents();
	}

	public ArrayList<Event> getNewEvents() {

		Date newDate = now;
//		newDate = new Date(now.getTime() + + 1000L * 60 * 60 * 24 * 2);
		String nowDateStr = parserSDF.format(newDate);
		String startPage = START_URL + nowDateStr;
		String startPageOrig = startPage;
		Document doc;
		ArrayList<Event> eventsList = new ArrayList<Event>();
		int count = 0;
		while (startPage != null) {
			count++;
			try {
				doc = Jsoup.connect(startPage).userAgent(SAFARI_USERAGENT)
						.get();

				String title = doc.title();
				System.out.println(title);

				Element content = doc.getElementsByTag("ul").get(4);
				Elements events = content.getElementsByTag("li");
				for (Element event : events) {
					try {
						Event newEvent = getEventData(event, nowDateStr);
						if (newEvent != null) {
							addGeoCodeData(newEvent, "en");
							eventsList.add(newEvent);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Elements nextElems = doc.getElementsByClass("next");
				if (nextElems.size() > 0 && !"".equals(nextElems.attr("href"))) {
					startPage = startPageOrig + nextElems.attr("href");
				} else if (count < MAX_DATES_TO_LOAD) {
					newDate = new Date(newDate.getTime() + 1000L * 60 * 60 * 24);
					nowDateStr = parserSDF.format(newDate);
					startPage = START_URL + parserSDF.format(newDate);
					startPageOrig = startPage;
				} else {
					startPage = null;
				}
				System.out.println("next: " + startPage);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				startPage = null;
			}
		}
		return eventsList;
	}

	private static Event getEventData(Element event, String eventDateStr) {
		String eventIconImage = null;
		String eventHref = null;
		String eventTitle = null;
		String eventSummary = null;
		String eventDescription = null;
		String eventNotes = null;
		String eventTimeStr = null;
		Date eventDateStart = null;
		Date eventDateEnd = null;
		Double eventGpsLat = null;
		Double eventGpsLong = null;
		String eventLocationTitle = null;
		String eventLocationUrl = null;
		String eventLocationAddress = null;
		EventSubCategory eventSubCategory = null;

		Element titleElement = event.getElementsByTag("h4").first();
		Element aElement = titleElement.getElementsByTag("a").first();
		eventTitle = titleElement.text();
		eventHref = BASE_WEBSITE + aElement.attr("href");
		Elements eventImageAvatar = event.getElementsByClass("avatar");
		if (eventImageAvatar.size() > 0) {
			Elements eventImageElems = eventImageAvatar.first()
					.getElementsByTag("img");
			if (eventImageElems.size() > 0) {
				eventIconImage = "http:" + eventImageElems.first().attr("src");
			}
		}

		Element infoDiv = event.getElementsByClass("info").get(0);
		String categoryString = infoDiv.getElementsByTag("a").attr("href");
		eventSubCategory = getEventSubCategoryFromUrl(categoryString);
		eventTimeStr = infoDiv.getElementsByClass("time").get(0).text().trim();
		eventSummary = infoDiv.getElementsByClass("intro").get(0).text().trim();
		Element locationElement = infoDiv.getElementsByClass("location")
				.first();
		eventLocationUrl = BASE_WEBSITE + locationElement.attr("href");
		eventLocationTitle = locationElement.text().trim();
		int lastIndex = eventLocationTitle.lastIndexOf('(');
		eventLocationTitle = lastIndex > 0 ? eventLocationTitle.substring(0,
				lastIndex) : eventLocationTitle;

		System.out.println(eventHref);
		System.out.println(eventTitle);
		System.out.println(eventIconImage);
		System.out.println(eventSummary);
		System.out.println(eventTimeStr);
		System.out.println(eventLocationTitle);

		if (eventDateStr != null) {
			try {
				eventDateStart = parserSDFWithTime.parse(eventDateStr + " "
						+ eventTimeStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (eventHref != null) {
			try {
				System.out.println("loading desc. url: " + eventHref);
				Document eventDetailsDoc = Jsoup.connect(eventHref)
						.userAgent(SAFARI_USERAGENT).get();
				eventDescription = eventDetailsDoc
						.getElementsByClass("description").first().text()
						.trim();

				if (eventLocationUrl != null) {
					Document eventLocationDoc = Jsoup.connect(eventLocationUrl)
							.userAgent(SAFARI_USERAGENT).get();

					Element locationInfoElem = eventLocationDoc
							.getElementsByClass("basicInfo").first();

					Elements locationElements = locationInfoElem
							.getElementsByTag("div");
					if (locationElements.size() > 2) {
						eventLocationAddress = locationElements.get(1).text()
								+ "\n" + locationElements.get(2).text();
						if (locationElements.size() > 3
								&& locationElements.get(3).text()
										.startsWith("http")) {
							eventLocationUrl = locationElements.get(3).text();
						}
					}

					System.out.println("eventIcon:       " + eventIconImage);
					System.out.println("href:            " + eventHref);
					System.out.println("title:           " + eventTitle);
					System.out.println("summary:           " + eventSummary);
					System.out.println("description:           " + eventDescription);
					System.out
							.println("location:        " + eventLocationTitle);
					System.out.println("locationAddress: "
							+ eventLocationAddress);
					System.out.println("dateStr:         " + eventDateStr);
					System.out.println("date:            " + eventDateStart);
					System.out.println("-----------------------------------");

					return new Event(eventSubCategory, EventSharability.NORMAL,
							EventVisibility.PUBLIC, EventStatus.ACTIVE,
							EventFlexibility.FIXED, eventTitle, eventSummary,
							eventDescription, eventNotes, eventDateStart,
							eventDateEnd, eventGpsLat, eventGpsLong,
							eventLocationUrl, eventLocationTitle,
							eventLocationAddress, now, "prinzde");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	private static EventSubCategory getEventSubCategoryFromUrl(
			String categoryString) {
		// http://prinz.de/duesseldorf/events/kategorie/stadtleben/freizeit/
		int categoryIndex = categoryString.indexOf(CATEGORY_STR);
		int categoryIndex2 = categoryString.indexOf("/", categoryIndex
				+ CATEGORY_STR.length() + 1);
		System.out.println("categoryUrl: " + categoryString);
		String searchStr = categoryString.substring(categoryIndex
				+ CATEGORY_STR.length(), categoryIndex2);
		switch (searchStr) {
		case "konzert":
			return EventSubCategory.CONCERT;
		case "freizeit":
			return EventSubCategory.FREE_TIME;
		case "party":
			return EventSubCategory.PARTY;
		case "stadtleben":
			return EventSubCategory.TOWN;
		case "familie-kinder":
			return EventSubCategory.FAMILY_KIDS;
		case "sport":
			return EventSubCategory.OTHERSPR;
		case "kultur":
			return EventSubCategory.OTHERCUL;
		case "None":
			
		default:
			throw new RuntimeException("shit: " + searchStr);

		}
		// return null;
	}
}
