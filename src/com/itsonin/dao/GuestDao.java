package com.itsonin.dao;

import com.itsonin.entity.Guest;
import com.itsonin.ofy.ObjectifyGenericDao;

/**
 * @author nkislitsin
 *
 */
public class GuestDao extends ObjectifyGenericDao<Guest>{

	public GuestDao() {
		super(Guest.class);
	}
	
}
