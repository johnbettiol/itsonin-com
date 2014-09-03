package com.itsonin.service;

import java.util.List;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.itsonin.dao.CommentDao;
import com.itsonin.dao.EventDao;
import com.itsonin.dao.GuestDao;
import com.itsonin.entity.Comment;
import com.itsonin.entity.Device;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.EventVisibility;
import com.itsonin.enums.GuestStatus;
import com.itsonin.exception.ForbiddenException;
import com.itsonin.exception.NotFoundException;
import com.itsonin.security.AuthContextService;

/**
 * @author nkislitsin
 * 
 */
public class CommentService {

	private CommentDao commentDao;
	private GuestDao guestDao;
	private EventDao eventDao;
	private AuthContextService authContextService;

	@Inject
	public CommentService(CommentDao commentDao, GuestDao guestDao,
			EventDao eventDao, AuthContextService authContextService) {
		this.commentDao = commentDao;
		this.guestDao = guestDao;
		this.eventDao = eventDao;
		this.authContextService = authContextService;
	}

	public Comment create(Comment comment) {
		if (!isAllowed(comment.getEventId(), comment.getGuestId())) {
			throw new ForbiddenException("Not allowed");
		}

		Key<Comment> key = commentDao.save(comment);
		return commentDao.get(key);
	}

	public void update(Long eventId, Long guestId, Long commentId,
			Comment comment) {

		if (!isAllowed(eventId, guestId)) {
			throw new ForbiddenException("Not allowed");
		}

		Comment toUpdate = get(commentId);

		if (toUpdate == null) {
			throw new NotFoundException("Comment with id=" + commentId
					+ " doesn't exist");
		}

		if (comment.getComment() != null)
			toUpdate.setComment(comment.getComment());
		if (comment.getParentCommentId() != null)
			toUpdate.setParentCommentId(comment.getParentCommentId());

		commentDao.save(comment);
	}

	public void delete(Long eventId, Long guestId, Long commentId) {

		if (!isAllowed(eventId, guestId)) {
			throw new ForbiddenException("Not allowed");
		}

		Comment toDelete = commentDao.get(commentId);
		if (toDelete == null) {
			throw new NotFoundException("Comment with id=" + commentId
					+ " is not exists");
		}

		commentDao.delete(commentId);
	}

	public List<Comment> list(Long eventId) {
		return list(eventId, null);
	}

	public List<Comment> list(Long eventId, Long guestId) {
		Event event = eventDao.get(eventId);
		if (event == null) {
			throw new NotFoundException("Event with id=" + eventId + " does not exist");
		}

		if (EventVisibility.PRIVATE.equals(event.getVisibility())
				&& !isAllowed(eventId, guestId)) {
			throw new ForbiddenException("Not allowed");
		}

		return commentDao.list(eventId, guestId);
	}

	public Comment get(Long id) {
		Comment comment = commentDao.get(id);
		if (comment == null) {
			throw new NotFoundException("Comment with id=" + id	+ " does not exist");
		} else {
			return comment;
		}
	}

	boolean isAllowed(Long eventId, Long guestId) {
		Device device = authContextService.get().getDevice();
		if (DeviceLevel.SUPER.equals(device.getLevel())) {
			return true;
		}

		Guest guest = guestDao.get(eventId + "_" + guestId + "_" + device.getDeviceId());
		if (guest != null && guest.getStatus() != GuestStatus.VIEWED) {//TODO: check attending, declined etc
			return true;
		}
		return false;
	}

}
