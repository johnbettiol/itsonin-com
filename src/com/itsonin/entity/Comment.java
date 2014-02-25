package com.itsonin.entity;

import java.util.Date;

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
public class Comment {

	@Id
	private Long commentId;
	private Long eventId;
	private Long guestId;
	private Long parentCommentId;
	private String comment;
	private Date created;
	
	@SuppressWarnings("unused")
	private Comment(){}

	public Comment(Long commentId, Long eventId, Long guestId,
			Long parentCommentId, String comment, Date created) {
		this.commentId = commentId;
		this.eventId = eventId;
		this.guestId = guestId;
		this.parentCommentId = parentCommentId;
		this.comment = comment;
		this.created = created;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getGuestId() {
		return guestId;
	}

	public void setGuestId(Long guestId) {
		this.guestId = guestId;
	}

	public Long getParentCommentId() {
		return parentCommentId;
	}

	public void setParentCommentId(Long parentCommentId) {
		this.parentCommentId = parentCommentId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}