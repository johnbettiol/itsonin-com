package com.itsonin.dao;

import com.itsonin.entity.Guest;

/**
 * @author nkislitsin
 *
 */
public class GuestDao extends ObjectifyGenericDao<Guest>{

	public GuestDao() {
		super(Guest.class);
	}
	
}
