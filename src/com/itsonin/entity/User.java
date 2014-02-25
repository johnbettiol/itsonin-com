package com.itsonin.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.itsonin.enums.UserStatus;
import com.itsonin.enums.UserType;

/**
 * @author nkislitsin
 *
 */
@Entity
@Cache
public class User {

	@Id
	private Long id;
	private UserType type;
	private UserStatus status;
	private String msisdn;
	private String email;
	private String name;
	private String salt;
	private String hash;
	private Date created;
	private Date update;
	
	@SuppressWarnings("unused")
	private User(){}

	public User(Long id, UserType type, UserStatus status, String msisdn,
			String email, String name, String salt, String hash, Date created,
			Date update) {
		this.id = id;
		this.type = type;
		this.status = status;
		this.msisdn = msisdn;
		this.email = email;
		this.name = name;
		this.salt = salt;
		this.hash = hash;
		this.created = created;
		this.update = update;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
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