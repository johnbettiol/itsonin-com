package com.itsonin.exception.mappers;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.itsonin.exception.BadRequestException;

@Provider
@Produces("application/json; charset=UTF-8;")
public class BadRequestExceptionMapper extends AppExceptionMapper implements ExceptionMapper<BadRequestException>
{
	@Override
	public Response toResponse(BadRequestException exception)
	{
		return getResponseBuilder(exception, Response.Status.BAD_REQUEST).build();
	}
}
