package com.itsonin.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.DeviceType;

/**
 * @author nkislitsin
 *
 */
@Entity
@Cache
@Index
public class Device {

	@Id
	private Long deviceId;
	private DeviceType type;
	private String token;
	private DeviceLevel level;
	private Date created;
	private Date update;
	
	@SuppressWarnings("unused")
	private Device(){}

	public Device(Long deviceId, DeviceType type,
			String token, DeviceLevel level, Date created, Date update) {
		this.deviceId = deviceId;
		this.type = type;
		this.token = token;
		this.level = level;
		this.created = created;
		this.update = update;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public DeviceLevel getLevel() {
		return level;
	}

	public void setLevel(DeviceLevel level) {
		this.level = level;
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