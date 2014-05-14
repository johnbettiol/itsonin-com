package com.itsonin.enums;

/**
 * @author nkislitsin
 *
 */
public enum DeviceLevel {
	NORMAL(0), ADMIN(500), SUPER(1000);
	
	private final int value;

    private DeviceLevel(int value) {
        this.value = value;
    }
    
    public int getLevel() {
    	return value;
    }
}