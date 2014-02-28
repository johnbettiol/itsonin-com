package com.itsonin.response;

/**
 * @author nkislitsin
 *
 */
public class SuccessResponse {
	private String status = "success";
	private String message;
	
	public SuccessResponse(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
