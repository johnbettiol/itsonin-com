package com.itsonin.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.itsonin.enums.EventScheduleStatus;

/**
 * @author nkislitsin
 *
 */
@Entity
@Cache
public class EventSchedule {

	@Id
	private Long id;
	private Long eventId;
	private EventScheduleStatus status;
	private Date startTime;
	private Date endTime;
	private Double gpsLong;
	private Double gpsLat;
	private String locationUrl;
	private String locationTitle;
	private String locationAddress;
	private Date created;
	private Date update;
	
	@SuppressWarnings("unused")
	private EventSchedule(){}

	public EventSchedule(Long id, Long eventId, EventScheduleStatus status,
			Date startTime, Date endTime, Double gpsLong, Double gpsLat,
			String locationUrl, String locationTitle, String locationAddress,
			Date created, Date update) {
		this.id = id;
		this.eventId = eventId;
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;
		this.gpsLong = gpsLong;
		this.gpsLat = gpsLat;
		this.locationUrl = locationUrl;
		this.locationTitle = locationTitle;
		this.locationAddress = locationAddress;
		this.created = created;
		this.update = update;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public EventScheduleStatus getStatus() {
		return status;
	}

	public void setStatus(EventScheduleStatus status) {
		this.status = status;
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

	public Double getGpsLong() {
		return gpsLong;
	}

	public void setGpsLong(Double gpsLong) {
		this.gpsLong = gpsLong;
	}

	public Double getGpsLat() {
		return gpsLat;
	}

	public void setGpsLat(Double gpsLat) {
		this.gpsLat = gpsLat;
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

	public Date getUpdate() {
		return update;
	}

	public void setUpdate(Date update) {
		this.update = update;
	}


}