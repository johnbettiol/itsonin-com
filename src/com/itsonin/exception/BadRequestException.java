package com.itsonin.exception;

/**
 * @author nkislitsin
 *
 */
@SuppressWarnings("serial")
public class BadRequestException extends AppException {
	
    public BadRequestException(String message) {
        super(message);
    }
}