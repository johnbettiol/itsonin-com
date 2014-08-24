package com.itsonin.enums;

/**
 * @author nkislitsin
 *
 */
public enum EventSubCategory {
	
	// Taken from:
	// https://docs.google.com/spreadsheets/d/1eFwAu_Ll4B_AYAGhUgIMm2WvethTq3IWsLiFumcnlzo/edit#gid=627971514
	PARTY(EventCategory.NIGHTLIFE),
	DJNIGHT(EventCategory.NIGHTLIFE),
	OTHERNGT(EventCategory.NIGHTLIFE),
	MEETUP(EventCategory.SOCIAL),
	SINGLES(EventCategory.SOCIAL),
	OTHERSOC(EventCategory.SOCIAL),
	FOOTBALL(EventCategory.SPORT),
	ICEHOCKEY(EventCategory.SPORT),
	MOTORSPORT(EventCategory.SPORT),
	OTHERSPR(EventCategory.SPORT),
	CONCERT(EventCategory.FESTIVAL),
	TOWN(EventCategory.FESTIVAL),
	OTHERFST(EventCategory.FESTIVAL),
	CONVENTION(EventCategory.CULTURAL),
	ACADEMIC(EventCategory.CULTURAL),
	MUSEUM(EventCategory.CULTURAL),
	ART(EventCategory.CULTURAL),
	OTHERCLT(EventCategory.CULTURAL);
	
	private EventCategory parent;
	
	private EventSubCategory(EventCategory parent) {
		this.parent = parent;
	}

	public EventCategory getParent() {
		return parent;
	}

	public void setParent(EventCategory parent) {
		this.parent = parent;
	}
}
