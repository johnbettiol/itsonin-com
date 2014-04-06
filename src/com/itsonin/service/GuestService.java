package com.itsonin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.itsonin.dao.CounterDao;
import com.itsonin.dao.EventDao;
import com.itsonin.dao.GuestDao;
import com.itsonin.dao.GuestDeviceDao;
import com.itsonin.entity.Device;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.entity.GuestDevice;
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
	private EventDao eventDao;
	private CounterDao counterDao;
	private GuestDeviceDao guestDeviceDao;
	private AuthContextService authContextService;
	
	@Inject
	public GuestService(GuestDao guestDao, EventDao eventDao, CounterDao counterDao, 
			GuestDeviceDao guestDeviceDao, AuthContextService authContextService){
		this.guestDao = guestDao;
		this.eventDao = eventDao;
		this.counterDao = counterDao;
		this.guestDeviceDao = guestDeviceDao;
		this.authContextService = authContextService;
	}
	
	public List<Guest> getPeviousGuests(Long deviceId, String name, String sortField,
			 SortOrder sortOrder, Integer offset, Integer limit, Integer numberOfLevels) {
		Device device = authContextService.getDevice();
		if(DeviceLevel.NORMAL.equals(device.getLevel()) && !deviceId.equals(device.getDeviceId()))
			throw new ForbiddenException("Not allowed");
		
		List<GuestDevice> guestDeviceList = guestDeviceDao.listByProperty("deviceId", device.getDeviceId());
		
		List<Guest> hosts = new ArrayList<Guest>();
		for (int i = 0; i < guestDeviceList.size(); i += 30) {//30 - limit for 'in' query
			List<GuestDevice> sublist = guestDeviceList.subList(i, Math.min(i + 30, guestDeviceList.size()));
			List<Long> guestIds = new ArrayList<Long>();

			for(GuestDevice guestDevice : sublist){
				guestIds.add(guestDevice.getGuestId());
			}
			
			hosts.addAll(guestDao.listByProperty("guestId in", guestIds));
		}
		
		List<Guest> previousGuests = new ArrayList<Guest>();
		for (int i = 0; i < hosts.size(); i += 30) {//30 - limit for 'in' query
			List<Guest> sublist = hosts.subList(i, Math.min(i + 30, guestDeviceList.size()));
			List<Long> hostIds = new ArrayList<Long>();

			for(Guest host : sublist){
				hostIds.add(host.getGuestId());
			}
			
			previousGuests.addAll(guestDao.listByProperty("parentGuestId in", hostIds));
		}
		
		return previousGuests;
	}
	
	public void update(Long eventId, Long guestId, Guest guest){
		Guest toUpdate = guestDao.get(eventId + "_" + guestId);
		if(toUpdate == null)
			throw new NotFoundException("Guest with id= " + guestId + " doesn't not exist");
		if(guest.getType() != null)
			toUpdate.setType(guest.getType());
		
		guestDao.save(toUpdate);
	}
	
	public Guest create(Long eventId, Guest guest){
		Event event = eventDao.get(eventId);
		
		guest.setGuestId(counterDao.next("EVENT_" + event.getEventId() + "_GUEST"));
		guest.setEventId(eventId);
		guest.setId(guest.getEventId() + "_" + guest.getGuestId());
		guest.setCreated(new Date());
		
		return guest;
	}
	
	public List<Guest> list(Long eventId, String name, Date created, String comment, String sortField,
			SortOrder sortOrder, Integer numberOfLevels, Integer offset, Integer limit) {
		return guestDao.list(eventId, name, created, sortField, sortOrder, numberOfLevels, offset, limit);
	}
	
}
