package com.itsonin.service;

import java.util.List;

import com.google.inject.Inject;
import com.itsonin.dao.GuestDao;
import com.itsonin.entity.Guest;
import com.itsonin.enums.SortOrder;

/**
 * @author nkislitsin
 *
 */
public class GuestService {

	private GuestDao guestDao;
	
	@Inject
	public GuestService(GuestDao guestDao){
		this.guestDao = guestDao;
	}
	
	public List<Guest> getPeviousGuests(String name, String sortField,
			 SortOrder sortOrder, Long offset, Long limit, Integer numberOfLevels) {
		return null;
	}
	
}
