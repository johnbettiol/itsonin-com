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
import com.itsonin.enums.GuestType;
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
		if(event.getPrivacy() != null)
			toUpdate.setPrivacy(event.getPrivacy());
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
	
	public Guest attend(Long eventId, Guest guest) {
		Device device = authContextService.getDevice();
		
		guest.setGuestId(counterDao.next("EVENT_" + eventId + "_GUEST"));
		guest.setEventId(eventId);
		guest.setType(GuestType.HOST);
		guest.setCreated(new Date());
		Key<Guest> guestKey = guestDao.save(guest);
		guest = guestDao.get(guestKey);
		
		guestDeviceDao.save(new GuestDevice(device.getDeviceId(), guest.getGuestId()));
		
		return guest;
	}
	
	public List<Event> list() {
		return eventDao.list();
	}
	
	public void cancel(Long eventId, Long guestId) {
		Event event = eventDao.get(eventId);
		event.setStatus(EventStatus.CANCELLED);
		eventDao.save(event);
	}

	public void decline(Long eventId, Long guestId) {
		//TODO: remove the guest?
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
