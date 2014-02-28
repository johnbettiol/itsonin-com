package com.itsonin.exception.mappers;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.itsonin.exception.UnauthorizedException;

@Provider
@Produces("application/json; charset=UTF-8;")
public class UnauthorizedExceptionMapper extends AppExceptionMapper implements ExceptionMapper<UnauthorizedException>
{
	@Override
	public Response toResponse(UnauthorizedException exception)
	{
		return getResponseBuilder(exception, Response.Status.UNAUTHORIZED).build();
	}
}
