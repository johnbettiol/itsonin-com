package com.itsonin.service;

import java.util.List;

import com.google.inject.Inject;
import com.itsonin.dao.DeviceDao;
import com.itsonin.entity.Device;
import com.itsonin.exception.NotFoundException;
import com.itsonin.security.AuthContextService;

/**
 * @author nkislitsin
 *
 */
public class DeviceService {

	private DeviceDao deviceDao;
	private AuthContextService authContextService;
	
	@Inject
	public DeviceService(DeviceDao deviceDao, AuthContextService authContextService){
		this.deviceDao = deviceDao;
		this.authContextService = authContextService;
	}
	
	public Device create(Device device) {
		deviceDao.save(device);
		return device;
	}
	
	public List<Device> list() {
		return deviceDao.list();
	}
	
	public void delete(Long id) {
		Device device = getDevice(id);
		deviceDao.delete(device);
	}
	
	private Device getDevice(Long id){
		Device device = deviceDao.get(id);
		if(device == null)
			throw new NotFoundException("Device with id=" + id + " is not exists");
		else
			return device;
	}
	
}
