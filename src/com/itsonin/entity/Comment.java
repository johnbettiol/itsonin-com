package com.itsonin.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.itsonin.enums.CommentStatus;

/**
 * @author nkislitsin
 *
 */
@Entity
@Cache
public class Comment {

	@Id
	private Long id;
	private Long eventId;
	private Long parentCommentId;
	private String comment;
	private Long votesUp;
	private Long votesDown;
	private CommentStatus status;
	private Date created;
	private Date update;
	
	@SuppressWarnings("unused")
	private Comment(){}

	public Comment(Long id, Long eventId, Long parentCommentId, String comment,
			Long votesUp, Long votesDown, CommentStatus status, Date created,
			Date update) {
		this.id = id;
		this.eventId = eventId;
		this.parentCommentId = parentCommentId;
		this.comment = comment;
		this.votesUp = votesUp;
		this.votesDown = votesDown;
		this.status = status;
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

	public Long getVotesUp() {
		return votesUp;
	}

	public void setVotesUp(Long votesUp) {
		this.votesUp = votesUp;
	}

	public Long getVotesDown() {
		return votesDown;
	}

	public void setVotesDown(Long votesDown) {
		this.votesDown = votesDown;
	}

	public CommentStatus getStatus() {
		return status;
	}

	public void setStatus(CommentStatus status) {
		this.status = status;
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