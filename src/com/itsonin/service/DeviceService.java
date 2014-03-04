package com.itsonin.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.itsonin.dao.CounterDao;
import com.itsonin.dao.DeviceDao;
import com.itsonin.entity.Device;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.SortOrder;
import com.itsonin.exception.ForbiddenException;
import com.itsonin.exception.NotFoundException;
import com.itsonin.exception.UnauthorizedException;
import com.itsonin.security.AuthContext;
import com.itsonin.security.AuthContextService;

/**
 * @author nkislitsin
 *
 */
public class DeviceService {

	private DeviceDao deviceDao;
	private CounterDao counterDao;
	private AuthContextService authContextService;
	
	@Inject
	public DeviceService(DeviceDao deviceDao, CounterDao counterDao,
			AuthContextService authContextService){
		this.deviceDao = deviceDao;
		this.counterDao = counterDao;
		this.authContextService = authContextService;
	}
	
	public Device create(Device device) {
		device.setDeviceId(counterDao.next("DEVICE"));
		device.setCreated(new Date());
		device.setToken(UUID.randomUUID().toString());//temporary TODO: replace
		Key<Device> key = deviceDao.save(device);
		return deviceDao.get(key);
	}
	
	public List<Device> list(String name, Date created, Date lastLogin, String sortField,
			 SortOrder sortOrder, Integer offset, Integer limit) {
		if(!isSuper())
			throw new ForbiddenException("Not allowed");
		
		return deviceDao.list(name, created, lastLogin, sortField, sortOrder, offset, limit);
	}
	
	public void delete(Long id) {
		if(!isSuper() && !isOwner(id))
			throw new ForbiddenException("Not allowed");
		
		Device device = get(id);
		deviceDao.delete(device);
	}
	
	public Device getDeviceByToken(String token){
		return deviceDao.getDeviceByToken(token);
	}
	
	public Device authenticate(Long id, String token){
		Device device = deviceDao.get(id);
		authContextService.set(new AuthContext(device));
		String validateToken = device.getToken().toString();
		if (device == null || !token.equals(validateToken)) {
			throw new UnauthorizedException("Not Authenticated");
		} else {
				return device;
		}
	}
	
	public Device get(Long id){
		Device device = deviceDao.get(id);
		if(device == null)
			throw new NotFoundException("Device with id=" + id + " does not exist");
		else
			return device;
	}
	
	boolean isSuper(){
		Device device = authContextService.get().getDevice();
		if(DeviceLevel.SUPER.equals(device.getLevel()))
			return true;
		else
			return false;
	}
	
	boolean isOwner(Long deviceId){
		Device device = authContextService.get().getDevice();
		if(device.getDeviceId().equals(deviceId)){
			return true;
		}else{
			return false;
		}
	}
	
}
