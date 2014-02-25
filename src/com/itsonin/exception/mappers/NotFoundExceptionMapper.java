package com.itsonin.exception.mappers;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.itsonin.exception.NotFoundException;

@Provider
@Produces("application/json; charset=UTF-8;")
public class NotFoundExceptionMapper extends AppExceptionMapper implements ExceptionMapper<NotFoundException>
{
	@Override
	public Response toResponse(NotFoundException exception)
	{
		return getResponseBuilder(exception, Response.Status.NOT_FOUND).build();
	}
}
