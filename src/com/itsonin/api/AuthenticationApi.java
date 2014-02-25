package com.itsonin.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.dao.UserDao;

/**
 * @author nkislitsin
 *
 */
@Path("/api")
public class AuthenticationApi {
	
	private UserDao userDao;
	
	@Inject
	public AuthenticationApi(UserDao userDao){
		this.userDao = userDao;
	}
	
	@GET
	@Path("/user/authenticate")
	@Produces("application/json")
	public Response authenticate(@QueryParam("login")String login, @QueryParam("password")String password) {
		return Response.ok().build();
	}
	
}
