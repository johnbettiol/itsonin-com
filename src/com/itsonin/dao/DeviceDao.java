package com.itsonin.dao;

import static com.itsonin.ofy.OfyService.ofy;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.cmd.Query;
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
			 SortOrder sortOrder, Integer offset, Integer limit){
		Query<Device> q = ofy().load().type(clazz);
		
		if(name != null){
			q = q.filter("name >=", name).filter("name <=", name);
		}
		
		if(created != null)
			q = q.filter("created", created);
		
		if(lastLogin != null)
			q = q.filter("lastLogin", lastLogin);
		
		if(sortOrder != null && sortField != null && !sortField.isEmpty()){
			if(sortOrder.equals(SortOrder.ASC))
				q.order(sortField);
			else
				q.order("-" + sortField);
		}
		
		if(offset != null)
			q.offset(offset);
		
		if(offset != null)
			q.limit(limit);
		

		return q.list();
	}
	
	public Device getDeviceByToken(String token){
		return ofy().load().type(Device.class).filter("token", token).first().now();
	}
	
}
