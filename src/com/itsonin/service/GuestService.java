package com.itsonin.service;

import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.itsonin.dao.GuestDao;
import com.itsonin.entity.Device;
import com.itsonin.entity.Guest;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.SortOrder;
import com.itsonin.exception.ForbiddenException;
import com.itsonin.exception.NotFoundException;
import com.itsonin.security.AuthContextService;

/**
 * @author nkislitsin
 *
 */
public class GuestService {

	private GuestDao guestDao;
	private AuthContextService authContextService;
	
	@Inject
	public GuestService(GuestDao guestDao, AuthContextService authContextService){
		this.guestDao = guestDao;
		this.authContextService = authContextService;
	}
	
	public List<Guest> getPeviousGuests(Long deviceId, String name, String sortField,
			 SortOrder sortOrder, Integer offset, Integer limit, Integer numberOfLevels) {
		Device device = authContextService.getDevice();
		if(DeviceLevel.NORMAL.equals(device.getLevel()) && !deviceId.equals(device.getDeviceId()))
			throw new ForbiddenException("Not allowed");
		
		return guestDao.getPreviousGuests(deviceId, name, sortField, 
				sortOrder, offset, limit, numberOfLevels);
	}
	
	public void update(Long eventId, Long guestId, Guest guest){
		Guest toUpdate = guestDao.get(eventId + "_" + guestId);
		if(toUpdate == null)
			throw new NotFoundException("Guest with id= " + guestId + " is not exists");
		if(guest.getType() != null)
			toUpdate.setType(guest.getType());
		
		guestDao.save(toUpdate);
	}
	
	public List<Guest> list(String name, Date created, String comment, String sortField,
			SortOrder sortOrder, Integer numberOfLevels, Integer offset, Integer limit) {
		return guestDao.list(name, created, sortField, sortOrder, numberOfLevels, offset, limit);
	}
	
}
