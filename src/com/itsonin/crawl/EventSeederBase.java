package com.itsonin.crawl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.GuestStatus;

abstract class EventSeederBase implements EventSeeder {
	
	protected static final Geocoder geocoder = new Geocoder();
	protected static final Date now = new Date();

	protected static void addGeoCodeData(Event newEvent, String language) throws IOException {
		String geoCodeSearch = newEvent.getLocationTitle() + "," + newEvent.getLocationAddress();
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(
				/*URLEncoder.encode(*/geoCodeSearch/*, "UTF-8")*/).setLanguage(language).getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		if(geocoderResponse.getStatus() == GeocoderStatus.OK){
			List<GeocoderResult> results = geocoderResponse.getResults();
			
			if(results.size() > 0) {
				GeocoderGeometry geometry = results.get(0).getGeometry();
				newEvent.setGpsLat(geometry.getLocation().getLat().doubleValue());
				newEvent.setGpsLong(geometry.getLocation().getLng().doubleValue());
			}
		}
	}
	
	public Guest getHostGuest() {
		Guest guest = new Guest(getEventHostName());
		guest.setStatus(GuestStatus.YES);
		return guest;
	}

}