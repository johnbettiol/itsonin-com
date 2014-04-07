package com.itsonin.entity;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.itsonin.enums.GuestStatus;
import com.itsonin.enums.GuestType;
import com.itsonin.resteasy.CustomDateTimeSerializer;

/**
 * @author nkislitsin
 *
 */
@Entity
@Cache
@Index
public class Guest {

	@Id
	private String id;
	private Long deviceId;
	private Long guestId;
	private Long eventId;
	private Long parentGuestId;
	private String name;
	private GuestType type;
	private GuestStatus status;
	private Date created;
	
	@SuppressWarnings("unused")
	private Guest(){}
	
	public Guest(String name) {
		this.name = name;
	}

	public Guest(Long deviceId, Long guestId, Long eventId, String name, GuestType type, 
			GuestStatus status, Date created) {
		this.id = eventId + "_" + guestId + "_" + deviceId;
		this.deviceId = deviceId;
		this.guestId = guestId;
		this.eventId = eventId;
		this.name = name;
		this.type = type;
		this.setStatus(status);
		this.created = created;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getGuestId() {
		return guestId;
	}

	public void setGuestId(Long guestId) {
		this.guestId = guestId;
	}
	
	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getParentGuestId() {
		return parentGuestId;
	}

	public void setParentGuestId(Long parentGuestId) {
		this.parentGuestId = parentGuestId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GuestType getType() {
		return type;
	}

	public void setType(GuestType type) {
		this.type = type;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public GuestStatus getStatus() {
		return status;
	}

	public void setStatus(GuestStatus status) {
		this.status = status;
	}

}