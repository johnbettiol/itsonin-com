package com.itsonin.crawl;

import java.util.ArrayList;

import com.itsonin.entity.Event;
import com.itsonin.entity.Guest;

public interface EventSeeder {
	
	public  ArrayList<Event> getNewEvents() throws SeederParserException;
	
	public Guest getHostGuest();
	
	abstract String getEventHostName();
}