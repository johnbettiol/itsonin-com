<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
	<title>itsonin - Events in ${ioiContext.city}</title>
	<%@ include file="../../head.jsp" %>
	<script type="text/javascript">
		var event = ${eventJson};
    </script>
	<script type="text/javascript" src="/static/js/modules/info.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
        	EventInfoModule.init();
        });
    </script>
</head>
<body>
	<div class="header-container">
		<div class="container" id="info-header">
			<div class="row">
				<div class="col-sm-offset-3 col-sm-6">
					<div class="row">
						<div class="col-sm-12 header">
							<a href="/${ioiContext.locale}/${ioiContext.city}/Events">
								<img src="/static/img/itsonin-white.png" height="20" width="20">
								<span class="header-title v-a-m">${ioiContext.city}</span>
							</a>
						</div>
					</div>
					<div class="row">
						<div class="list-group-item main-info">
							<div class="media">
								<div class="pull-left">
									<span style="display: inline-block;height: 100%;vertical-align: middle;"></span>
									<img src="/static/img/party.png" height="40" width="40" style="vertical-align: middle;display:inline;">
								</div>
								<div class="media-body clearfix">
									<div class="media-heading event-title"><c:out value="${event.title}"/></div>
									<p class="event-offer">
										<c:out value="${event.offer}"/>
									</p>
									<div class="text-muted">
										<i class="fa fa-clock-o fs-11"></i>
										<fmt:formatDate type="time" pattern="hh:mm a" value="${event.startTime}"/>
										 -
										<fmt:formatDate type="time" pattern="hh:mm a" value="${event.endTime}"/>
									</div>
								</div>
							</div>
						</div>
					</div>
			   </div>
			</div>
		</div>
	</div>
	<div class="container" id="info">
		<div class="row">
			<div class="col-sm-offset-3 col-sm-6 event-container">
				<div style="height: 200px; width: 100%; padding-top: 10px;padding-bottom: 10px">
					<div id="map-canvas" style="height: 100%; width: 100%"></div>
				</div>
				<p><c:out value="${event.locationAddress}"/></p>
				<hr/>
				<p><c:out value="${event.description}"/></p>
				<hr/>
				<div class="share">
					<ul>
						<li><a href="javascript:void(0)" id="share-link-btn"><i class="fa fa-2x fa-share-alt"></i><span>Share link</span></a></li>
						<li><a href="javascript:void(0)" id="share-by-email-btn"><i class="fa fa-2x fa-envelope-o"></i><span>Email</span></a></li>
						<li><a href="javascript:void(0)" id="share-on-facebook-btn"><i class="fa fa-2x fa-facebook"></i><span>Facebook</span></a></li>
						<li><a href="javascript:void(0)" id="share-on-google-btn"><i class="fa fa-2x fa-google-plus"></i><span>Google+</span></a></li>
					</ul>
				</div>
				<hr/>
				<div class="attend">
					<label>Your name</label>
					<input type="text" class="form-control" id="guest-name-field">
					<div class="btn-container text-center clearfix">
						<button class="btn btn-default pull-left" id="attend-btn">Attend</button>
						<button class="btn btn-default" id="maybe-attend-btn">Maybe</button>
						<button class="btn btn-default pull-right" id="decline-btn">Decline</button>
					</div>
				</div>
				<hr/>
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title">
							<label>Guest list</label>
						</div>
					</div>
					<div class="list-group">
						<c:forEach var="guest" items="${guests}">
							<div class="list-group-item guest-item" id="${guest.guestId}">
								<div class="media">
									<div class="pull-left">
										<i class="fa fa-user"></i>									
									</div>
									<div class="media-body clearfix">
										<span class="pull-left"><c:out value="${guest.name}"/></span>
										<span class="pull-right"><c:out value="${guest.status}"/></span>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title">
							<label>Comments</label>
						</div>
					</div>
					<div class="list-group">
						<c:forEach var="comment" items="${comments}">
							<div class="list-group-item comment-item" id="${comment.commentId}">
								<div class="media">
									<div class="media-body ">
		                                <small class="pull-right">2h ago</small><%--<c:out value="${comment.created}"/> --%>
		                                <strong><small>John Bettiol</small></strong><br>
		                                <small class="text-muted"><c:out value="${comment.comment}"/></small>
		                            </div>
								</div>
							</div>
						</c:forEach>
					</div>
					<div class="panel-footer">
						<div class="input-group">
							<input type="text" class="form-control" placeholder="Type your message here ..." id="comment-field">
							<span class="input-group-btn">
								<button class="btn btn-default" id="add-comment-btn">Add</button>
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%-- SHARE LINK MODAL --%>
  	<div class="modal" id="share-link-modal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title">Share link</h4>
	      </div>
	      <div class="modal-body">
	        <input type="text" class="form-control" id="link">
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	  </div>
	</div>
	<%-- SHARE BY EMAIL MODAL --%>
	<div class="modal" id="share-by-email-modal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title">Share by email</h4>
	      </div>
	      <div class="modal-body">
	        <label>Email</label>
    		<input type="text" class="form-control" id="email">
	      </div>
	      <div class="modal-footer"><!-- TODO -->
	        <a href="mailto:{{email}}?subject=Invitation&amp;body={{link}}" class="btn btn-primary">Send</a>
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	  </div>
	</div>
</body>
</html>