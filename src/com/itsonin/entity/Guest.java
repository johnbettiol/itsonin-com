package com.itsonin.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.itsonin.enums.GuestStatus;
import com.itsonin.enums.GuestType;

/**
 * @author nkislitsin
 *
 */
@Entity
@Cache
public class Guest {

	@Id
	private Long id;
	private Long userId;
	private Long eventId;
	private Long parentGuestId;
	private GuestType type;
	private GuestStatus status;
	private Long eventScheduleId;
	private Date created;
	private Date update;
	
	@SuppressWarnings("unused")
	private Guest(){}

	public Guest(Long id, Long userId, Long eventId, Long parentGuestId,
			GuestType type, GuestStatus status, Long eventScheduleId,
			Date created, Date update) {
		this.id = id;
		this.userId = userId;
		this.eventId = eventId;
		this.parentGuestId = parentGuestId;
		this.type = type;
		this.status = status;
		this.eventScheduleId = eventScheduleId;
		this.created = created;
		this.update = update;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public GuestType getType() {
		return type;
	}

	public void setType(GuestType type) {
		this.type = type;
	}

	public GuestStatus getStatus() {
		return status;
	}

	public void setStatus(GuestStatus status) {
		this.status = status;
	}

	public Long getEventScheduleId() {
		return eventScheduleId;
	}

	public void setEventScheduleId(Long eventScheduleId) {
		this.eventScheduleId = eventScheduleId;
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