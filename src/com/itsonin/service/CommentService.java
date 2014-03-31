package com.itsonin.service;

import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.itsonin.dao.CommentDao;
import com.itsonin.dao.EventDao;
import com.itsonin.dao.GuestDao;
import com.itsonin.dao.GuestDeviceDao;
import com.itsonin.entity.Comment;
import com.itsonin.entity.Device;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.entity.GuestDevice;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.EventVisibility;
import com.itsonin.enums.GuestType;
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
	private GuestDeviceDao guestDeviceDao;
	private AuthContextService authContextService;
	
	@Inject
	public CommentService(CommentDao commentDao, GuestDao guestDao, EventDao eventDao, 
			GuestDeviceDao guestDeviceDao, AuthContextService authContextService){
		this.commentDao = commentDao;
		this.guestDao = guestDao;
		this.eventDao = eventDao;
		this.guestDeviceDao = guestDeviceDao;
		this.authContextService = authContextService;
	}
	
	public Comment create(Long eventId, Long guestId, Long parentCommentId, Comment comment) {
		if(guestId == null){
			guestId = getGuestIdForEvent(eventId);
		}
		
		if(!isAllowed(eventId, guestId))
			throw new ForbiddenException("Not allowed");
		
		comment.setEventId(eventId);
		comment.setGuestId(guestId);
		comment.setParentCommentId(parentCommentId);
		comment.setCreated(new Date());
		Key<Comment> key = commentDao.save(comment);
		
		return commentDao.get(key);
	}
	
	public void update(Long eventId, Long guestId, Long commentId, Comment comment) {
		if(guestId == null){
			guestId = getGuestIdForEvent(eventId);
		}
		
		if(!isAllowed(eventId, guestId))
			throw new ForbiddenException("Not allowed");
		
		Comment toUpdate = get(commentId);
		
		if(toUpdate == null)
			throw new NotFoundException("Comment with id=" + commentId + " doesn't exist");
		
		if(comment.getComment() != null)
			toUpdate.setComment(comment.getComment());
		if(comment.getParentCommentId() != null)
			toUpdate.setParentCommentId(comment.getParentCommentId());
		
		commentDao.save(comment);
	}
	
	public void delete(Long eventId, Long guestId, Long commentId) {
		if(guestId == null){
			guestId = getGuestIdForEvent(eventId);
		}
		
		if(!isAllowed(eventId, guestId))
			throw new ForbiddenException("Not allowed");
		
		Comment toDelete = commentDao.get(commentId);
		if(toDelete == null)
			throw new NotFoundException("Comment with id=" + commentId + " is not exists");
		
		commentDao.delete(commentId);
	}
	
	public List<Comment> list(Long eventId, Long guestId) {
		if(guestId == null){
			guestId = getGuestIdForEvent(eventId);
		}
		
		Event event = eventDao.get(eventId);
		if(event == null)
			throw new NotFoundException("Event with id=" + eventId + " does not exist");
		
		if(EventVisibility.PRIVATE.equals(event.getVisibility()) && !isAllowed(eventId, guestId))
			throw new ForbiddenException("Not allowed");
			
		return commentDao.list();
	}
	
	public Comment get(Long id){
		Comment comment = commentDao.get(id);
		if(comment == null)
			throw new NotFoundException("Comment with id=" + id + " does not exist");
		else
			return comment;
	}
	
	boolean isAllowed(Long eventId, Long guestId){
		Device device = authContextService.get().getDevice();
		if(DeviceLevel.SUPER.equals(device.getLevel()))
			return true;
		
		Guest guest = guestDao.get(eventId + "_" + guestId);
		if(guest != null && GuestType.HOST.equals(guest.getType()))
			return true;

		GuestDevice guestDevice = guestDeviceDao.get(device.getDeviceId() + "_" + guestId);
		if(guestDevice != null)
			return true;
		
		return false;
	}
	
	private Long getGuestIdForEvent(Long eventId){
		Device device = authContextService.get().getDevice();
		List<GuestDevice> guestDeviceList = guestDeviceDao.listByProperty("deviceId", device.getDeviceId());
		
		for(GuestDevice gd : guestDeviceList){
			Guest guest = guestDao.get(eventId + "_" + gd.getGuestId());
			if(guest != null && guest.getEventId().equals(eventId)){
				return guest.getGuestId();
			}
		}
		return null;
	}
	
	private String validate(Comment comment){
		
		return "";
	}

}
