package com.itsonin.exception;

/**
 * @author nkislitsin
 *
 */
@SuppressWarnings("serial")
public class NotFoundException extends AppException {
	
    public NotFoundException(String message) {
        super(message);
    }
}
