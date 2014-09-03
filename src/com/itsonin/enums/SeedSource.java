package com.itsonin.enums;

/**
 * @author nkislitsin
 *
 */
public enum SeedSource {
	EVENTIM("http://www.eventim.de");
	
	private final String url;

    private SeedSource(String url) {
        this.url = url;
    }
    
    public String getUrl() {
    	return url;
    }
}