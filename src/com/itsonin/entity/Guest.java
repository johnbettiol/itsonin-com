package com.itsonin.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.itsonin.enums.GuestType;

/**
 * @author nkislitsin
 *
 */
@Entity
@Cache
@Index
public class Guest {

	@Id
	private Long guestId;
	private Long parentGuestId;
	private String name;
	private GuestType type;
	private Date created;
	
	@SuppressWarnings("unused")
	private Guest(){}

	public Guest(Long guestId, Long parentGuestId, String name, GuestType type,
			Date created) {
		this.guestId = guestId;
		this.parentGuestId = parentGuestId;
		this.name = name;
		this.type = type;
		this.created = created;
	}

	public Long getGuestId() {
		return guestId;
	}

	public void setGuestId(Long guestId) {
		this.guestId = guestId;
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


}