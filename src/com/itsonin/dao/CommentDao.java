package com.itsonin.dao;

import com.itsonin.entity.Comment;
import com.itsonin.ofy.ObjectifyGenericDao;

/**
 * @author nkislitsin
 *
 */
public class CommentDao extends ObjectifyGenericDao<Comment>{

	public CommentDao() {
		super(Comment.class);
	}
	
}
