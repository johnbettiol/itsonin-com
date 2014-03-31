package com.itsonin.service;

import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.itsonin.dao.CounterDao;
import com.itsonin.dao.EventDao;
import com.itsonin.dao.GuestDao;
import com.itsonin.dao.GuestDeviceDao;
import com.itsonin.dto.EventWithGuest;
import com.itsonin.entity.Device;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.entity.GuestDevice;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.GuestStatus;
import com.itsonin.enums.GuestType;
import com.itsonin.enums.SortOrder;
import com.itsonin.exception.NotFoundException;
import com.itsonin.security.AuthContextService;

/**
 * @author nkislitsin
 *
 */
public class EventService {

	private EventDao eventDao;
	private GuestDao guestDao;
	private CounterDao counterDao;
	private GuestDeviceDao guestDeviceDao;
	private AuthContextService authContextService;
	
	@Inject
	public EventService(EventDao eventDao, GuestDao guestDao, GuestDeviceDao guestDeviceDao,
			CounterDao counterDao, AuthContextService authContextService){
		this.eventDao = eventDao;
		this.guestDao = guestDao;
		this.counterDao = counterDao;
		this.guestDeviceDao = guestDeviceDao;
		this.authContextService = authContextService;
	}

	public EventWithGuest create(Event event, Guest guest) {
		Device device = authContextService.getDevice();
		
		event.setEventId(counterDao.next("EVENT"));
		Key<Event> eventKey = eventDao.save(event);
		event = eventDao.get(eventKey);
		
		guest.setGuestId(counterDao.next("EVENT_" + event.getEventId() + "_GUEST"));
		guest.setEventId(event.getEventId());
		guest.setId(guest.getEventId() + "_" + guest.getGuestId());
		guest.setType(GuestType.HOST);
		guest.setCreated(new Date());
		Key<Guest> guestKey = guestDao.save(guest);
		guest = guestDao.get(guestKey);

		guestDeviceDao.save(new GuestDevice(device.getDeviceId(), guest.getGuestId()));
		return new EventWithGuest(event, guest);
	}
	
	public void update(Long id, Event event) {
		Event toUpdate = eventDao.get(id);
		if(toUpdate == null)
			throw new NotFoundException("Event with id=" + id + " is not exists");
		
		if(event.getType() != null)
			toUpdate.setType(event.getType());
		if(event.getVisibility() != null)
			toUpdate.setVisibility(event.getVisibility());
		if(event.getStatus() != null)
			toUpdate.setStatus(event.getStatus());
		if(event.getFlexibility() != null)
			toUpdate.setFlexibility(event.getFlexibility());
		if(event.getTitle() != null)
			toUpdate.setTitle(event.getTitle());
		if(event.getDescription() != null)
			toUpdate.setDescription(event.getDescription());
		if(event.getNotes() != null)
			toUpdate.setNotes(event.getNotes());
		if(event.getStartTime() != null)
			toUpdate.setStartTime(event.getStartTime());
		if(event.getEndTime() != null)
			toUpdate.setEndTime(event.getEndTime());
		if(event.getGpsLat() != null)
			toUpdate.setGpsLat(event.getGpsLat());
		if(event.getGpsLong() != null)
			toUpdate.setGpsLong(event.getGpsLong());
		if(event.getLocationUrl() != null)
			toUpdate.setLocationUrl(event.getLocationUrl());
		if(event.getLocationTitle() != null)
			toUpdate.setLocationTitle(event.getLocationTitle());
		if(event.getLocationAddress() != null)
			toUpdate.setLocationAddress(event.getLocationAddress());

		eventDao.save(toUpdate);
	}
	
	public Event get(Long id) {
		return eventDao.get(id);
	}
	
	public Guest attend(Long eventId) {
		Device device = authContextService.getDevice();
		
		Guest guest = new Guest(counterDao.next("EVENT_" + eventId + "_GUEST"), eventId, 
				"name", GuestType.GUEST, GuestStatus.ATTENDING, new Date());//TODO:where get a name?
		
		guest.setParentGuestId(guestDao.getHostGuestForEvent(eventId));
		Key<Guest> guestKey = guestDao.save(guest);
		guest = guestDao.get(guestKey);
		
		guestDeviceDao.save(new GuestDevice(device.getDeviceId(), guest.getGuestId()));
	
		return guest;
	}
	
	public List<Event> list(String name, Date created, String comment, String sortField,
			SortOrder sortOrder, Integer numberOfLevels, Integer offset, Integer limit) {
		Device device = authContextService.getDevice();
		Long deviceId = null;
		if(DeviceLevel.NORMAL.equals(device.getLevel()))
			deviceId = device.getDeviceId();
		
		return eventDao.list(deviceId, name, created, comment, sortField, sortOrder, numberOfLevels, offset, limit);
	}
	
	public void cancel(Long eventId, Long guestId) {
		Event event = eventDao.get(eventId);
		event.setStatus(EventStatus.CANCELLED);
		eventDao.save(event);
	}

	public void decline(Long eventId) {
		Device device = authContextService.get().getDevice();
		List<GuestDevice> guestDeviceList = guestDeviceDao.listByProperty("deviceId", device.getDeviceId());
		
		Long guestId = null;
		for(GuestDevice gd : guestDeviceList){
			Guest guest = guestDao.get(eventId + "_" + gd.getGuestId());
			if(guest != null && guest.getEventId().equals(eventId)){
				guestId = guest.getGuestId();
			}
		}
		
		if(guestId != null){
			Guest guest = guestDao.get(eventId + "_" + guestId);
			guestDao.delete(guest);
		}
	}
	
	boolean isAllowed(Long eventId, Long guestId){
		Device device = authContextService.get().getDevice();
		if(DeviceLevel.SUPER.equals(device.getLevel()))
			return true;
		
		Guest guest = guestDao.get(eventId + "_" + guestId);
		if(guest != null && GuestType.HOST.equals(guest.getType()))
			return true;
		
		GuestDevice guestDevice = guestDeviceDao.get(device.getDeviceId() + "_" + guestId);
		if(guestDevice != null)
			return true;
		
		return false;
	}
	
}
