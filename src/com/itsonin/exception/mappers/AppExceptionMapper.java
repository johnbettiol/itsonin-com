package com.itsonin.exception.mappers;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.itsonin.exception.AppException;

public class AppExceptionMapper
{
	public ResponseBuilder getResponseBuilder(AppException exception, Status status)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("message", exception.getMessage());
		map.put("internalError", exception.getInternalErrorMessage());
		return Response.status(status).entity(map).type(MediaType.APPLICATION_JSON);
	}
}
