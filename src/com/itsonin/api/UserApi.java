package com.itsonin.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.dao.UserDao;
import com.itsonin.entity.Guest;
import com.itsonin.entity.User;
import com.itsonin.exception.NotFoundException;
import com.itsonin.security.AuthContextService;

/**
 * @author nkislitsin
 *
 */
@Path("/api")
public class UserApi {
	
	private UserDao userDao;
	private AuthContextService authContextService;
	
	@Inject
	public UserApi(UserDao userDao, AuthContextService authContextService){
		this.userDao = userDao;
		this.authContextService = authContextService;
	}
	
	@POST
	@Path("/user/create")
	@Consumes("application/json")
	@Produces("application/json")
	public User create(User user) {
		userDao.save(user);
		return user;
	}
	
	@GET
	@Path("/user/{id}/validate")
	@Produces("application/json")
	public Response validate(@PathParam("id")Long id) {
		User user = getUser(id);
		return Response.ok().build();
	}
	
	@PUT
	@Path("/user/{id}/update")
	@Produces("application/json")
	public User update(@PathParam("id")Long id) {
		return null;
	}
	
	@GET
	@Path("/user/list")
	@Produces("application/json")
	public List<User> list() {
		return userDao.list();
	}
	
	@DELETE
	@Path("/user/{id}/delete")
	@Produces("application/json")
	public Response delete(@PathParam("id")Long id) {
		User user = getUser(id);
		userDao.delete(user);
		return Response.ok().build();
	}
	
	@GET
	@Path("/user/{id}/previousGuests")
	@Produces("application/json")
	public List<Guest> previousGuests(@PathParam("id")String id) {
		return null;
	}
	
	private User getUser(Long id){
		User user = userDao.get(id);
		if(user == null)
			throw new NotFoundException("User with id=" + id + " is not exists");
		else
			return user;
	}

}
