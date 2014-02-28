package com.itsonin.exception.mappers;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.itsonin.exception.ForbiddenException;

@Provider
@Produces("application/json; charset=UTF-8;")
public class ForbiddenExceptionMapper extends AppExceptionMapper implements ExceptionMapper<ForbiddenException>
{
	@Override
	public Response toResponse(ForbiddenException exception)
	{
		return getResponseBuilder(exception, Response.Status.FORBIDDEN).build();
	}
}
