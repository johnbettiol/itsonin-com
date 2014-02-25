package com.itsonin.dao;

import com.itsonin.entity.GuestDevice;
import com.itsonin.ofy.ObjectifyGenericDao;

/**
 * @author nkislitsin
 *
 */
public class GuestDeviceDao extends ObjectifyGenericDao<GuestDevice>{

	public GuestDeviceDao() {
		super(GuestDevice.class);
	}
	
}
