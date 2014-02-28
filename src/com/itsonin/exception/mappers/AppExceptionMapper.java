package com.itsonin.exception.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.itsonin.exception.AppException;
import com.itsonin.response.ErrorResponse;

/**
 * @author nkislitsin
 *
 */
public class AppExceptionMapper
{
	public ResponseBuilder getResponseBuilder(AppException exception, Status status)
	{
		return Response.status(status)
					   .entity(new ErrorResponse(exception.getMessage(), exception.getInternalErrorMessage()))
					   .type(MediaType.APPLICATION_JSON);
	}
}