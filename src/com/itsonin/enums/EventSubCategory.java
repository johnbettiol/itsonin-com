package com.itsonin.enums;

/**
 * @author nkislitsin
 *
 */
public enum EventSubCategory {
	
	PARTY(EventCategory.MEET),
	SOCIAL(EventCategory.MEET),
	ACADEMIC(EventCategory.MEET),
	HOBBIES(EventCategory.MEET),
	SINGLES(EventCategory.MEET),
	
	CULTURAL(EventCategory.GOTO),
	CONCERTS(EventCategory.GOTO),
	SPORTS(EventCategory.GOTO),
	FESTIVAL(EventCategory.GOTO),
	CONVENTIONS(EventCategory.GOTO);
	
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
