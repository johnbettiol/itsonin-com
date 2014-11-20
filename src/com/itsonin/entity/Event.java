package com.itsonin.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
import com.itsonin.enums.EventCategory;
import com.itsonin.enums.EventFlexibility;
import com.itsonin.enums.EventSharability;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.EventSubCategory;
import com.itsonin.enums.EventVisibility;
import com.itsonin.resteasy.CustomDateTimeSerializer;

/**
 * @author nkislitsin
 *
 */
@Entity
@Cache
@Index
public class Event implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private Long eventId;
	private EventCategory category;
	private EventSubCategory subCategory;
	private EventSharability sharability;
	private EventVisibility visibility;
	private EventStatus status;
	private EventFlexibility flexibility;
	private String summary;
	private String title;
	private String offer;
	private String offerEmail;
	private String offerRef;
	@Unindex
	private String description;
	private String localTitle;
	private String localOffer;
	@Unindex
	private String localDescription;
	private String notes;
	private Date startTime;
	private Date endTime;
	private List<Date> days;
	private Double gpsLat;
	private Double gpsLong;
	private String locationUrl;
	private String locationTitle;
	private String locationAddress;
	private String source;
	private Integer hotScore;
	private Date created;
	
	@SuppressWarnings("unused")
	private Event(){}

	public Event(EventSubCategory subCategory, EventSharability sharability, EventVisibility visibility, 
			EventStatus status,	EventFlexibility flexibility, String title, String summary, String description,
			String notes, Date startTime, Date endTime, List<Date> days, Double gpsLat,Double gpsLong, 
			String locationUrl, String locationTitle, String locationAddress, Date created, String source) {
		
		this.category = subCategory.getParent();
		this.subCategory = subCategory;
		this.sharability = sharability;
		this.visibility = visibility;
		this.status = status;
		this.flexibility = flexibility;
		this.title = title;
		this.summary = summary;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.setDays(days);
		this.gpsLat = gpsLat;
		this.gpsLong = gpsLong;
		this.locationUrl = locationUrl;
		this.locationTitle = locationTitle;
		this.locationAddress = locationAddress;
		this.created = created;
		this.source = source;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public EventSharability getSharability() {
		return sharability;
	}

	public void setSharability(EventSharability sharability) {
		this.sharability = sharability;
	}

	public EventVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(EventVisibility visibility) {
		this.visibility = visibility;
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
	
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getStartTime() {
		return startTime;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getEndTime() {
		return endTime;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
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

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getCreated() {
		return created;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public void setCreated(Date created) {
		this.created = created;
	}

	public EventCategory getCategory() {
		return category;
	}

	public EventSubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(EventSubCategory subCategory) {
		this.subCategory = subCategory;
		this.category = subCategory.getParent();
	}

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}

	public void setIconImage(String val) {
		// TODO Auto-generated method stub
		
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<Date> getDays() {
		return days;
	}

	public void setDays(List<Date> days) {
		this.days = days;
	}

	public String getLocalTitle() {
		return localTitle;
	}

	public void setLocalTitle(String localTitle) {
		this.localTitle = localTitle;
	}

	public String getLocalOffer() {
		return localOffer;
	}

	public void setLocalOffer(String localOffer) {
		this.localOffer = localOffer;
	}

	public String getLocalDescription() {
		return localDescription;
	}

	public void setLocalDescription(String localDescription) {
		this.localDescription = localDescription;
	}

	public String getOfferEmail() {
		return offerEmail;
	}

	public void setOfferEmail(String offerEmail) {
		this.offerEmail = offerEmail;
	}

	public String getOfferRef() {
		return offerRef;
	}

	public void setOfferRef(String offerRef) {
		this.offerRef = offerRef;
	}

	public void setCategory(EventCategory category) {
		this.category = category;
	}

	public Integer getHotScore() {
		return hotScore;
	}

	public void setHotScore(Integer hotScore) {
		this.hotScore = hotScore;
	}

}