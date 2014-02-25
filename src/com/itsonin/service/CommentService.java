package com.itsonin.service;

import java.util.List;

import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.dao.CommentDao;
import com.itsonin.entity.Comment;

/**
 * @author nkislitsin
 *
 */
public class CommentService {
	
	private CommentDao commentDao;
	
	@Inject
	public CommentService(CommentDao commentDao){
		this.commentDao = commentDao;
	}
	
	public Comment create(String eventId, String guestId) {
		return null;
	}
	
	public Comment update(String eventId, String guestId, String commentId) {
		return null;
	}
	
	public Response delete(String eventId, String guestId, String commentId) {
		return Response.ok().build();
	}
	
	public List<Comment> list(String eventId, String guestId) {
		return null;
	}

}
