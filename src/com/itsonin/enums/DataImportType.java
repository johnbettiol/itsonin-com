package com.itsonin.enums;

/**
 * @author nkislitsin
 *
 */
public enum DataImportType {
	AUTO(0), LOCATION(100), UNKNOWN(1000);
	
	private final int value;

    private DataImportType(int value) {
        this.value = value;
    }
    
    public int getLevel() {
    	return value;
    }

}