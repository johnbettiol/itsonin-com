package com.itsonin.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.itsonin.entity.Comment;
import com.itsonin.response.SuccessResponse;
import com.itsonin.service.CommentService;

/**
 * @author nkislitsin
 * 
 */
@Path("/api")
public class CommentApi {

	private CommentService commentService;

	@Inject
	public CommentApi(CommentService commentService) {
		this.commentService = commentService;
	}

	@POST
	@Path("/event/{eventId}/{guestId}/comment/create")
	@Produces("application/json")
	public Comment create(@PathParam("eventId") Long eventId,
			@PathParam("guestId") Long guestId, Comment comment) {
		comment.setEventId(eventId);
		comment.setGuestId(guestId);
		return commentService.create(comment);
	}

	@POST
	@Path("/event/{eventId}/comment/create")
	@Produces("application/json")
	public Comment create(@PathParam("eventId") Long eventId, Comment comment) {
		return create(eventId, null, comment);
	}

	@POST
	@Path("/event/{eventId}/{guestId}/comment/{parentCommentId}/create")
	@Produces("application/json")
	public Comment createChild(@PathParam("eventId") Long eventId,
			@PathParam("guestId") Long guestId,
			@PathParam("parentCommentId") Long parentCommentId, Comment comment) {
		comment.setGuestId(guestId);
		comment.setParentCommentId(parentCommentId);
		return commentService.create(comment);
	}

	@POST
	@Path("/event/{eventId}/comment/{parentCommentId}/create")
	@Produces("application/json")
	public Comment createChild(@PathParam("eventId") Long eventId,
			@PathParam("parentCommentId") Long parentCommentId, Comment comment) {
		comment.setEventId(eventId);
		comment.setParentCommentId(parentCommentId);
		return commentService.create(comment);
	}

	@POST
	@Path("/event/{eventId}/{guestId}/comment/{commentId}/update")
	@Produces("application/json")
	public Response update(@PathParam("eventId") Long eventId,
			@PathParam("guestId") Long guestId,
			@PathParam("commentId") Long commentId, Comment comment) {
		commentService.update(eventId, guestId, commentId, comment);
		return Response.ok().entity(new SuccessResponse("Comment updated successfully"))
				.build();
	}

	@POST
	@Path("/event/{eventId}/comment/{commentId}/update")
	@Produces("application/json")
	public Response update(@PathParam("eventId") Long eventId,
			@PathParam("commentId") Long commentId, Comment comment) {
		return update(eventId, null, commentId, comment);
	}

	@POST
	@Path("/event/{eventId}/{guestId}/comment/{commentId}/delete")
	@Produces("application/json")
	public Response delete(@PathParam("eventId") Long eventId,
			@PathParam("guestId") Long guestId,
			@PathParam("commentId") Long commentId) {
		commentService.delete(eventId, guestId, commentId);
		return Response.ok()
				.entity(new SuccessResponse("Comment deleted successfully"))
				.build();
	}

	@POST
	@Path("/event/{eventId}/comment/{commentId}/delete")
	@Produces("application/json")
	public Response delete(@PathParam("eventId") Long eventId,
			@PathParam("commentId") Long commentId) {
		return delete(eventId, null, commentId);
	}

	@GET
	@Path("/event/{eventId}/{guestId}/comment/list")
	@Produces("application/json")
	public List<Comment> list(@PathParam("eventId") Long eventId,
			@PathParam("guestId") Long guestId) {
		return commentService.list(eventId, guestId);
	}

	@GET
	@Path("/event/{eventId}/comment/list")
	@Produces("application/json")
	public List<Comment> list(@PathParam("eventId") Long eventId) {
		return list(eventId, null);
	}

}
