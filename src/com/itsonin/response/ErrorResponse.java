package com.itsonin.response;

/**
 * @author nkislitsin
 *
 */
public class ErrorResponse {
	private String status = "error";
	private String message;
	private String internalError;
	
	public ErrorResponse(String message) {
		this.message = message;
	}
	
	public ErrorResponse(String message, String internalError) {
		this.message = message;
		this.internalError = internalError;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public String getInternalError() {
		return internalError;
	}
}
