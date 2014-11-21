package com.itsonin.crawl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

public class DusTourismoSeeder extends EventSeederBase {

	private static final String HTML_SPAN_STRONG_BR = "</span></strong><br />";
	private static final String SOURCE = "dustourismo";
	private static final String PARAM_DAY = "PARAM_DAY";
	private static final String PARAM_MONTH = "PARAM_MONTH";
	private static final String PARAM_YEAR = "PARAM_YEAR";
	private static final String PARAM_OFFSET = "PARAM_OFFSET";
	private static final String PARAM_ITEMS = "PARAM_ITEMS";
	private static final String BASE_URL = "http://www.duesseldorf-tourismus.de/";
	private static final String START_URL = BASE_URL
			+ "/nc/veranstaltungskalender-ddorf/?"
			+ "tx_atmopro_pi1%5Bshowpage%5D=suche&"
			+ "tx_atmopro_pi1%5Btpage%5D=suche&" + "tx_atmopro_pi1%5BX%5D=1&"
			+ "tx_atmopro_pi1%5BItems%5D=" + PARAM_ITEMS + "&"
			+ "tx_atmopro_pi1%5Boffset%5D=" + PARAM_OFFSET + "&"
			+ "tx_atmopro_pi1%5BFirstSelectMonth%5D=" + PARAM_MONTH + "&"
			+ "tx_atmopro_pi1%5BFirstSelectDay%5D=" + PARAM_DAY + "&"
			+ "tx_atmopro_pi1%5BFirstSelectYear%5D=" + PARAM_YEAR + "&"
			+ "tx_atmopro_pi1%5BSecondSelectMonth%5D=" + PARAM_MONTH + "&"
			+ "tx_atmopro_pi1%5BSecondSelectDay%5D=" + PARAM_DAY + "&"
			+ "tx_atmopro_pi1%5BSecondSelectYear%5D=" + PARAM_YEAR;

	// http://www.duesseldorf-tourismus.de//nc/en/veranstaltungskalender-ddorf/?tx_atmopro_pi1%5Bshowpage%5D=suche&tx_atmopro_pi1%5Boffset%5D=0no_cache=1&L=1&id=0&tx_atmopro_pi1%5BFirstSelectDay%5D=19&tx_atmopro_pi1%5BFirstSelectMonth%5D=11&tx_atmopro_pi1%5BFirstSelectYear%5D=2014&tx_atmopro_pi1%5BSecondSelectDay%5D=
	// 19&tx_atmopro_pi1%5BSecondSelectMonth%5D=11&tx_atmopro_pi1%5BSecondSelectYear%5D=2014&tx_atmopro_pi1%5BEventOrt%5D=k.A.&tx_atmopro_pi1%5Bshowpage%5D=suche&tx_atmopro_pi1%5BItems%5D=32&Submit=search%21&tx_atmopro_pi1%5BX%5D=1
	private static final String SAFARI_USERAGENT = "Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5355d Safari/8536.25";

	// private static final SimpleDateFormat parserSDF = new
	// SimpleDateFormat("MM/dd/yy / h:mm a", new Locale("de"));
	private static final SimpleDateFormat parserSDF = new SimpleDateFormat(
			"dd.MM.yy");

	private static final Date now = new Date();

	public static void main(String[] args) {

		new DusTourismoSeeder().getNewEvents();
	}

	public ArrayList<Event> getNewEvents() {
		String startPage = START_URL;
		startPage = startPage.replaceAll(PARAM_DAY, "19")
				.replaceAll(PARAM_MONTH, "11").replaceAll(PARAM_YEAR, "2014")
				.replaceAll(PARAM_OFFSET, "0").replaceAll(PARAM_ITEMS, "32");

		Document doc;
		Geocoder geocoder = new Geocoder();
		ArrayList<Event> eventsList = new ArrayList<Event>();
		while (startPage != null) {
			try {
				doc = Jsoup.connect(startPage).userAgent(SAFARI_USERAGENT)
						.get();
				System.out.println(startPage);
				String title = doc.title();
				System.out.println(title);

				String a = "21";

				Elements theList = doc.getElementsByAttributeValue("height",
						"5");
				Element listChildren = theList.get(1);
				Elements events = listChildren.children();
				for (Element event : events) {
					if ("moproNavi".equals(event.attr("class"))) {
						Elements nextButton = event.getElementsByTag("tr")
								.get(1).getElementsByTag("a");
						if (nextButton.size() > 0
								&& !"DarkF".equals(nextButton.get(
										nextButton.size() - 1).attr("class"))) {
							startPage = nextButton.get(nextButton.size() - 1)
									.attr("href");
						} else {
							startPage = null;
						}
					} else {
						Event newEvent;
						try {
							newEvent = getEventData(startPage, event);
							if (newEvent != null) {
								addGeoCodeData(newEvent, "en");
								eventsList.add(newEvent);
							}
						} catch (SeederParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				startPage = null;
			}
		}
		return eventsList;
	}

	private static Event getEventData(String url, Element event)
			throws SeederParserException {
		String step = "start";

		try {

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
			String eventCategoryStr = null;
			EventSubCategory eventSubCategory = null;

			step = "Getting dateStr";

			eventDateStr = event
					.getElementsByAttributeValue("class", "ariHelv 3").get(0)
					.text();

			step = "Getting categoryStr";

			eventCategoryStr = event
					.getElementsByAttributeValue("class", "ariHelv").get(0)
					.getElementsByAttributeValue("class", "blau").get(0).text();

			step = "Getting daysActive";

			Elements daysActive = event.getElementsByAttributeValue("class",
					"WochentagAktiv");

			step = "Getting event summary";

			String eventStr = event.toString();
			eventStr = eventStr.substring(eventStr.indexOf(HTML_SPAN_STRONG_BR)
					+ HTML_SPAN_STRONG_BR.length() + 1);
			eventSummary = eventStr.substring(0, eventStr.indexOf("<br "));

			step = "Getting locationAddress2";

			Elements searchAddressElems = event.getElementsByAttributeValue(
					"class", "bl1n");

			eventLocationAddress = searchAddressElems.size() > 0 ? searchAddressElems
					.get(0).text().trim()
					: null;
			if (eventLocationAddress == null || "".equals(eventLocationAddress)
					|| eventLocationAddress.length() == 1) {
				Elements searchAddressHrefElems = event
						.getElementsByAttributeValue("class", "DarkF");
				if (searchAddressHrefElems.size() > 0) {
					String locationHref = searchAddressHrefElems.get(0).attr(
							"href");
					eventLocationTitle = searchAddressHrefElems.get(0).text();
					eventLocationAddress = getLocationAddressFromLocationPage(locationHref);
				}
			} else {
				if (eventLocationAddress.indexOf(",") > 0) {
					eventLocationTitle = eventLocationAddress.substring(0,
							eventLocationAddress.indexOf(",")).trim();
					eventLocationAddress = eventLocationAddress.substring(
							eventLocationAddress.indexOf(",") + 1).trim();
					eventLocationAddress = eventLocationAddress
							+ "\nDüsseldorf";
				}
			}

			Elements eventDescriptionE = event
					.getElementsByAttributeValue("class", "ariHelv").get(2)
					.children();

			step = "Getting eventTitle";

			eventTitle = event.getElementsByAttributeValue("class", "ariHelv")
					.get(2).children().get(0).text();
			System.out.println("Title: " + eventTitle);

			step = "Getting eventDescription";

			eventDescription = event
					.getElementsByAttributeValue("class", "ariHelv").get(2)
					.children().get(2).text();

			if (eventDateStr != null) {
				try {
					int middle = eventDateStr.indexOf(" - ");
					String dateStart = eventDateStr.substring(
							eventDateStr.indexOf(" ") + 1, middle);
					String dateEndTmp = eventDateStr.substring(middle + 3);
					int start = dateEndTmp.indexOf(" ") + 1;
					int end = dateEndTmp.indexOf(" ", start);
					String dateEnd = dateEndTmp.substring(start, end);
					eventDateStart = parserSDF.parse(dateStart);
					eventDateEnd = parserSDF.parse(dateEnd);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			eventSubCategory = findSubCategoryFromCategoryStr(eventCategoryStr);

			if (eventLocationAddress != null && eventSubCategory != null) {

				System.out.println("eventIcon:       " + eventIconImage);
				System.out.println("categoryStr:     " + eventCategoryStr);
				System.out.println("subCategory:     " + eventSubCategory);
				System.out.println("href:            " + eventHref);
				System.out.println("title:           " + eventTitle);
				System.out.println("location:        " + eventLocationTitle);
				System.out.println("locationAddress: " + eventLocationAddress);
				System.out.println("dateStr:         " + eventDateStr);
				System.out.println("start date:      " + eventDateStart);
				System.out.println("end date:        " + eventDateEnd);
				System.out.println("-----------------------------------");

				Event result = new Event(eventSubCategory,
						EventSharability.NORMAL, EventVisibility.PUBLIC,
						EventStatus.ACTIVE, EventFlexibility.FIXED, eventTitle,
						eventSummary, eventDescription, eventNotes,
						eventDateStart, eventDateEnd,
						DateTimeUtil.getDaysBetweenDates(eventDateStart,
								eventDateEnd), eventGpsLat, eventGpsLong,
						eventLocationUrl, eventLocationTitle,
						eventLocationAddress, now, SOURCE);
				return result;

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw new SeederParserException(url, event.toString(), step,
					"Failure to parse: " + e.getMessage(), e);
		}
		return null;
	}

	private static EventSubCategory findSubCategoryFromCategoryStr(
			String eventCategoryStr) {
		EventSubCategory subCategory;
		switch (eventCategoryStr.toLowerCase()) {
		case "Führungen":
			subCategory = EventSubCategory.TOWN;
		case "Show, Musical, Varieté":
			subCategory = EventSubCategory.CONCERT;
		case "Zirkusgastspiele":
			subCategory = EventSubCategory.OTHERCUL;
		case "Ausstellungen":
			subCategory = EventSubCategory.ART;
		case "theater, kabarett":
			subCategory = EventSubCategory.CONCERT;
		default:
			subCategory = EventSubCategory.OTHERCUL;
		}
		return subCategory;
	}

	private static String getLocationAddressFromLocationPage(String locationHref)
			throws IOException {
		Document doc = Jsoup.connect(locationHref).userAgent(SAFARI_USERAGENT)
				.get();
		Elements firstTable = doc.getElementsByTag("table");

		Elements secondTable = firstTable.get(2).getElementsByTag("table");
		String locationTitle = secondTable.get(0).getElementsByTag("strong")
				.get(1).text().trim();
		int start = secondTable.get(1).getElementsByTag("tr").get(3)
				.getElementsByTag("img").size() > 0 ? 4 : 3;
		String addressLine1 = secondTable.get(1).getElementsByTag("tr")
				.get(start).getElementsByTag("td").get(0).text().trim();
		String addressLine2 = secondTable.get(1).getElementsByTag("tr")
				.get(start + 1).getElementsByTag("td").get(0).text().trim();

		return addressLine1 + "\n" + addressLine2;
	}
}
