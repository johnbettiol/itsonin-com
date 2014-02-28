package com.itsonin.entity;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * @author nkislitsin
 *
 */
@Entity
@Cache
@Index
public class GuestDevice {

	@Id
	private String id;
	private Long deviceId;
	private Long guestId;
	
	@SuppressWarnings("unused")
	private GuestDevice(){}

	public GuestDevice(Long deviceId, Long guestId) {
		this.id = deviceId + "_" + guestId;
		this.deviceId = deviceId;
		this.guestId = guestId;
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


}