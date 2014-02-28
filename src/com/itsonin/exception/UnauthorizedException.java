package com.itsonin.exception;

/**
 * @author nkislitsin
 *
 */
@SuppressWarnings("serial")
public class UnauthorizedException extends AppException {
	
    public UnauthorizedException(String message) {
        super(message);
    }
}