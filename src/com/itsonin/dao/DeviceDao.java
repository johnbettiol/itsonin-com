package com.itsonin.dao;

import static com.itsonin.ofy.OfyService.ofy;

import java.util.Date;
import java.util.List;

import com.itsonin.entity.Device;
import com.itsonin.enums.SortOrder;
import com.itsonin.ofy.ObjectifyGenericDao;

/**
 * @author nkislitsin
 *
 */
public class DeviceDao extends ObjectifyGenericDao<Device>{

	public DeviceDao() {
		super(Device.class);
	}
	
	public List<Device> list(String name, Date created, Date lastLogin, String sortField,
			 SortOrder sortOrder, Long offset, Long limit){
		return list();
	}
	
	public Device getDeviceByToken(String token){
		return ofy().load().type(Device.class).filter("token", token).first().now();
	}
	
}
