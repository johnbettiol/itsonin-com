package com.itsonin.dao;

import static com.itsonin.ofy.OfyService.ofy;

import java.util.List;

import com.googlecode.objectify.cmd.Query;
import com.itsonin.entity.Comment;
import com.itsonin.ofy.ObjectifyGenericDao;

/**
 * @author nkislitsin
 * 
 */
public class CommentDao extends ObjectifyGenericDao<Comment> {

	public CommentDao() {
		super(Comment.class);
	}

	public List<Comment> list(Long eventId, Long guestId) {
		Query<Comment> q = ofy().load().type(clazz);

		if (eventId != null) {
			q = q.filter("eventId", eventId);
		}

		if (guestId != null) {
			q = q.filter("guestId", guestId);
		}
		List<Comment> list = q.list();
		return list;
	}

}
