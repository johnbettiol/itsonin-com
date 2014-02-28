package com.itsonin.exception;

/**
 * @author nkislitsin
 *
 */
@SuppressWarnings("serial")
public class ForbiddenException extends AppException {
	
    public ForbiddenException(String message) {
        super(message);
    }
}