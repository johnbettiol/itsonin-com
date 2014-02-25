package com.itsonin.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.itsonin.enums.EventFlexibility;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.EventType;
import com.itsonin.enums.EventVisibility;
import com.itsonin.enums.PrivacyType;

/**
 * @author nkislitsin
 *
 */
@Entity
@Cache
@Index
public class Event {

	@Id
	private Long eventId;
	private EventType type;
	private EventVisibility visibility;
	private PrivacyType privacy;
	private EventStatus status;
	private EventFlexibility flexibility;
	private String title;
	private String description;
	private String notes;
	private Date startTime;
	private Date endTime;
	private Double gpsLat;
	private Double gpsLong;
	private String locationUrl;
	private String locationTitle;
	private String locationAddress;
	private Date created;
	
	@SuppressWarnings("unused")
	private Event(){}

	public Event(Long eventId, EventType type, EventVisibility visibility,
			PrivacyType privacy, EventStatus status,
			EventFlexibility flexibility, String title, String description,
			String notes, Date startTime, Date endTime, Double gpsLat,
			Double gpsLong, String locationUrl, String locationTitle,
			String locationAddress, Date created) {
		this.eventId = eventId;
		this.type = type;
		this.visibility = visibility;
		this.privacy = privacy;
		this.status = status;
		this.flexibility = flexibility;
		this.title = title;
		this.description = description;
		this.notes = notes;
		this.startTime = startTime;
		this.endTime = endTime;
		this.gpsLat = gpsLat;
		this.gpsLong = gpsLong;
		this.locationUrl = locationUrl;
		this.locationTitle = locationTitle;
		this.locationAddress = locationAddress;
		this.created = created;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public EventVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(EventVisibility visibility) {
		this.visibility = visibility;
	}

	public PrivacyType getPrivacy() {
		return privacy;
	}

	public void setPrivacy(PrivacyType privacy) {
		this.privacy = privacy;
	}

	public EventStatus getStatus() {
		return status;
	}

	public void setStatus(EventStatus status) {
		this.status = status;
	}

	public EventFlexibility getFlexibility() {
		return flexibility;
	}

	public void setFlexibility(EventFlexibility flexibility) {
		this.flexibility = flexibility;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Double getGpsLat() {
		return gpsLat;
	}

	public void setGpsLat(Double gpsLat) {
		this.gpsLat = gpsLat;
	}

	public Double getGpsLong() {
		return gpsLong;
	}

	public void setGpsLong(Double gpsLong) {
		this.gpsLong = gpsLong;
	}

	public String getLocationUrl() {
		return locationUrl;
	}

	public void setLocationUrl(String locationUrl) {
		this.locationUrl = locationUrl;
	}

	public String getLocationTitle() {
		return locationTitle;
	}

	public void setLocationTitle(String locationTitle) {
		this.locationTitle = locationTitle;
	}

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


}