package com.itsonin.dao;

import com.itsonin.entity.User;

/**
 * @author nkislitsin
 *
 */
public class UserDao extends ObjectifyGenericDao<User>{

	public UserDao() {
		super(User.class);
	}
	
}
