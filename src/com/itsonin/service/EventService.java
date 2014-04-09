package com.itsonin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.itsonin.dao.CounterDao;
import com.itsonin.dao.EventDao;
import com.itsonin.dao.GuestDao;
import com.itsonin.entity.Device;
import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;
import com.itsonin.enums.DeviceLevel;
import com.itsonin.enums.EventStatus;
import com.itsonin.enums.EventType;
import com.itsonin.enums.EventVisibility;
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

	private static final String HOST = "http://itsonin-com.appspot.com";

	private EventDao eventDao;
	private GuestDao guestDao;
	private CounterDao counterDao;
	private AuthContextService authContextService;

	@Inject
	public EventService(EventDao eventDao, GuestDao guestDao,
			CounterDao counterDao, AuthContextService authContextService) {
		this.eventDao = eventDao;
		this.guestDao = guestDao;
		this.counterDao = counterDao;
		this.authContextService = authContextService;
	}

	public Map<String, Object> create(Event event, Guest guest) {
		Device device = authContextService.getDevice();

		event.setEventId(counterDao.nextEventId());
		event.setCreated(new Date());
		Key<Event> eventKey = eventDao.save(event);
		event = eventDao.get(eventKey);
		guest.setGuestId(counterDao.nextGuestId(event.getEventId()));
		guest.setEventId(event.getEventId());
		guest.setId(guest.getEventId() + "_" + guest.getGuestId() + "_"
				+ event.getEventId());
		guest.setType(GuestType.HOST);
		guest.setStatus(GuestStatus.ATTENDING);
		guest.setCreated(new Date());
		guest.setDeviceId(device.getDeviceId());
		Key<Guest> guestKey = guestDao.save(guest);
		guest = guestDao.get(guestKey);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("event", event);
		result.put("guest", guest);
		result.put("shareUrl", HOST + "/#/i/" + event.getEventId() + '.'
				+ guest.getGuestId());
		return result;
	}

	public void update(Long id, Event event) {
		Event toUpdate = eventDao.get(id);
		if (toUpdate == null)
			throw new NotFoundException("Event with id=" + id
					+ " is not exists");

		if (event.getType() != null)
			toUpdate.setType(event.getType());
		if (event.getSharability() != null)
			toUpdate.setSharability(event.getSharability());
		if (event.getVisibility() != null)
			toUpdate.setVisibility(event.getVisibility());
		if (event.getStatus() != null)
			toUpdate.setStatus(event.getStatus());
		if (event.getFlexibility() != null)
			toUpdate.setFlexibility(event.getFlexibility());
		if (event.getTitle() != null)
			toUpdate.setTitle(event.getTitle());
		if (event.getDescription() != null)
			toUpdate.setDescription(event.getDescription());
		if (event.getNotes() != null)
			toUpdate.setNotes(event.getNotes());
		if (event.getStartTime() != null)
			toUpdate.setStartTime(event.getStartTime());
		if (event.getEndTime() != null)
			toUpdate.setEndTime(event.getEndTime());
		if (event.getGpsLat() != null)
			toUpdate.setGpsLat(event.getGpsLat());
		if (event.getGpsLong() != null)
			toUpdate.setGpsLong(event.getGpsLong());
		if (event.getLocationUrl() != null)
			toUpdate.setLocationUrl(event.getLocationUrl());
		if (event.getLocationTitle() != null)
			toUpdate.setLocationTitle(event.getLocationTitle());
		if (event.getLocationAddress() != null)
			toUpdate.setLocationAddress(event.getLocationAddress());

		eventDao.save(toUpdate);
	}

	public Event get(Long eventId) {
		Event event = eventDao.get(eventId);
		storeGuestEntry(
				eventId,
				event.getVisibility() == EventVisibility.PUBLIC ? GuestStatus.VIEWED
						: GuestStatus.PENDING);
		return event;
	}

	public Guest attend(Long eventId) {
		return storeGuestEntry(eventId, GuestStatus.ATTENDING);
	}

	public Guest decline(Long eventId) {
		return storeGuestEntry(eventId, GuestStatus.DECLINED);
	}

	private Guest storeGuestEntry(Long eventId, GuestStatus guestStatus) {
		Device device = authContextService.getDevice();
		Guest guest = guestDao.getGuestForEvent(eventId, device.getDeviceId());
		if (guest == null) {
			guest = new Guest(device.getDeviceId(),
					counterDao.nextGuestId(eventId), eventId, null,
					GuestType.GUEST, guestStatus, new Date());
			guest.setParentGuestId(guestDao.getHostGuestForEvent(eventId));
			Key<Guest> guestKey = guestDao.save(guest);
			guest = guestDao.get(guestKey);
		} else {
			if (!guestStatus.equals(guest.getStatus())
					&& guest.getType() != GuestType.HOST) {
				guest.setStatus(guestStatus);
				guestDao.save(guest);
			}
		}
		return guest;
	}

	public List<Event> list(Boolean allEvents, List<EventType> types,
			String name, Date startTime, String comment, String sortField,
			SortOrder sortOrder, Integer numberOfLevels, Integer offset,
			Integer limit) {
		Device device = authContextService.getDevice();
		List<Event> eventList = eventDao.list(types, name, startTime, comment,
				sortField, sortOrder, numberOfLevels, offset, limit);
		List<Guest> guestList = guestDao.listByDeviceId(device.getDeviceId());
		List<Event> filteredList = new ArrayList<Event>();

		if (DeviceLevel.NORMAL.equals(device.getLevel())) {
			for (Event event : eventList) {
				if (event.getVisibility() == EventVisibility.PUBLIC) {
					if (allEvents.equals(true)) {
						filteredList.add(event);
					} else {
						int guestCounter = -1;
						for (int grIndex = 0; grIndex < guestList.size(); grIndex++) {
							Guest guestRecord = guestList.get(grIndex);
							if (guestRecord.getEventId() == event.getEventId()
									&& guestRecord.getStatus() != GuestStatus.VIEWED) {
								filteredList.add(event);
								guestCounter = grIndex;
							}
						}
						if (guestCounter > 0) {
							guestList.remove(guestCounter);
						}
					}
				} else if (event.getVisibility() == EventVisibility.PRIVATE
						&& guestList.size() > 0) {
					int guestCounter = -1;
					for (int grIndex = 0; grIndex < guestList.size(); grIndex++) {
						Guest guestRecord = guestList.get(grIndex);
						if (guestRecord.getEventId() == event.getEventId()) {
							filteredList.add(event);
							guestCounter = grIndex;
						}
					}
					if (guestCounter > 0) {
						guestList.remove(guestCounter);
					}
				} else {
					// We do this one for FriendsOnly
				}
			}
		}
		return filteredList;

	}

	public void cancel(Long eventId, Long guestId) {
		Event event = eventDao.get(eventId);
		event.setStatus(EventStatus.CANCELLED);
		eventDao.save(event);
	}

	public boolean isAllowed(Long eventId, Long guestId) {
		Device device = authContextService.get().getDevice();
		if (DeviceLevel.SUPER.equals(device.getLevel()))
			return true;

		Guest guest = guestDao.get(eventId + "_" + guestId);
		if (guest != null && GuestType.HOST.equals(guest.getType()))
			return true;

		return false;
	}

}
