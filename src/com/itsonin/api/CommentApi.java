package com.itsonin.api;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.dao.CommentDao;
import com.itsonin.entity.Comment;

/**
 * @author nkislitsin
 *
 */
@Path("/api")
public class CommentApi {
	
	private CommentDao commentDao;
	
	@Inject
	public CommentApi(CommentDao commentDao){
		this.commentDao = commentDao;
	}
	
	@POST
	@Path("/event/{eventId}/{guestId}/comment/create")
	@Produces("application/json")
	public Comment create(@PathParam("eventId")String eventId,
						@PathParam("guestId")String guestId) {
		return null;
	}
	
	@PUT
	@Path("/event/{eventId}/{guestId}/comment/{commentId}/update")
	@Produces("application/json")
	public Comment update(@PathParam("eventId")String eventId,
							@PathParam("guestId")String guestId,
							@PathParam("commentId")String commentId) {
		return null;
	}
	
	@DELETE
	@Path("/event/{eventId}/{guestId}/comment/{commentId}/delete")
	@Produces("application/json")
	public Response delete(@PathParam("eventId")String eventId,
							@PathParam("guestId")String guestId,
							@PathParam("commentId")String commentId) {
		return Response.ok().build();
	}
	
	@GET
	@Path("/event/{eventId}/{guestId}/comment/list")
	@Produces("application/json")
	public List<Comment> list(@PathParam("eventId")String eventId,
								@PathParam("guestId")String guestId) {
		return null;
	}

}
