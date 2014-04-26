package com.itsonin.entity;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.resteasy.CustomDateTimeSerializer;

/**
 * @author nkislitsin
 *
 */
@Entity
@Cache
@Index
public class Device implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long deviceId;
	private String token;
	private DeviceLevel level;
	private Date created;
	private Date lastLogin=new Date();

	public Device() {
		this.level = DeviceLevel.NORMAL;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
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

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getCreated() {
		return created;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public void setCreated(Date created) {
		this.created = created;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getLastLogin() {
		return lastLogin;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

}