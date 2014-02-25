package com.itsonin.dao;

import com.itsonin.entity.Device;
import com.itsonin.ofy.ObjectifyGenericDao;

/**
 * @author nkislitsin
 *
 */
public class DeviceDao extends ObjectifyGenericDao<Device>{

	public DeviceDao() {
		super(Device.class);
	}
	
}
