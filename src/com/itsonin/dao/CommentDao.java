package com.itsonin.dao;

import com.itsonin.entity.Comment;

/**
 * @author nkislitsin
 *
 */
public class CommentDao extends ObjectifyGenericDao<Comment>{

	public CommentDao() {
		super(Comment.class);
	}
	
}
