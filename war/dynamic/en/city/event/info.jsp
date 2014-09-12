<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.itsonin.utils.TimeUtil"%>
<%@ page import="com.itsonin.entity.*"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ page session="false" %>

<jsp:useBean id="timeUtil" class="com.itsonin.utils.TimeUtil"/>
<%
	String shareUrl = request.getScheme() + "://" + request.getServerName() + request.getAttribute("javax.servlet.forward.servlet_path");
%>
<!DOCTYPE html>
<html>
<head>
	<title>itsonin - Events in ${ioiContext.city}</title>
	<%@ include file="../../head.jsp" %>
	<script type="text/javascript">
		var eventJson = ${eventJson};
		var guestJson = ${guestJson};
		var commentsJson = ${commentsJson};
	</script>
	<script type="text/javascript" src="/static/js/modules/info.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			EventInfoModule.init();
		});
	</script>
	<script id="commentTemplate" type="text/x-jsrender">
		<div class="list-group-item comment-item" id="{{:commentId}}">
			<div class="media">
				<div class="media-body">
					<small class="pull-right">{{:~formatTime(created)}}</small>
					<strong><small>{{:guestName}}</small></strong><br>
					<small class="text-muted">{{:comment}}</small>
				</div>
			</div>
		</div>
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
							<i class="fa fa-2x fa-users pull-right pointer" id="scrollto-guests-btn"></i>
							<i class="fa fa-2x fa-comment-o pull-right pointer" id="scrollto-comments-btn"></i>
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
										<fmt:formatDate type="time" pattern="hh:mm a yyyy/MM/dd" value="${event.startTime}"/>
										<c:if test="${not empty event.endTime}"> - </c:if>
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
				<div class="row">
					<div style="height: 200px; width: 100%; padding-bottom: 10px">
						<div id="map-canvas" style="height: 100%; width: 100%"></div>
					</div>
				</div>
				<div>
					<c:out value="${event.locationAddress}"/>
					<i class="fa fa-2x fa-location-arrow pull-right pointer" id="open-navigation-btn"></i>
				</div>
				<hr/>
				<p><c:out value="${event.description}"/></p>
				<c:if test="${not empty event.description}"><hr/></c:if>
				<div class="attend">
					<label>Are you attending this events?</label>
					<input type="text" class="form-control" id="guest-name-field" placeholder="Your name" value="${guest.name}">
					<div class="btn-container text-center clearfix">
						<button class="btn btn-default pull-left" id="decline-btn">No</button>
						<button class="btn btn-default" id="maybe-attend-btn">Maybe</button>
						<button class="btn btn-default pull-right" id="attend-btn">Yes</button>
					</div>
				</div>
				<hr/>
				<c:if test="${(guest.status == 'YES' || guest.status == 'NO') && event.sharability != 'NOSHARE'}">
					<div class="share">
						<ul>
							<li><a href="javascript:void(0)" id="share-link-btn"><i class="fa fa-2x fa-share-alt"></i><span>Share link</span></a></li>
							<li><a href="javascript:void(0)" id="share-by-email-btn"><i class="fa fa-2x fa-envelope-o"></i><span>Email</span></a></li>
							<li><a href="javascript:void(0)" id="share-on-facebook-btn"><i class="fa fa-2x fa-facebook"></i><span>Facebook</span></a></li>
							<li><a href="javascript:void(0)" id="share-on-google-btn"><i class="fa fa-2x fa-google-plus"></i><span>Google+</span></a></li>
						</ul>
					</div>
					<hr/>
				</c:if>
				<div class="row guests">
					<label style="margin-left: 15px">Guests</label> <span class="badge ng-binding" style="font-size:10px"><c:out value="${fn:length(guests)}"/></span>
					<div class="panel panel-default">
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
				</div>
				<hr/>
				<div class="row comments">
					<label style="margin-left: 15px">Comments</label> <span class="badge ng-binding" style="font-size:10px"><c:out value="${fn:length(comments)}"/></span>
					<div class="panel panel-default">
						<div class="list-group">
							<c:forEach var="comment" items="${comments}">
								<div class="list-group-item comment-item" id="${comment.commentId}">
									<div class="media">
										<div class="media-body ">
											<small class="pull-right">
											<%= TimeUtil.prettyFormat(((Comment)(pageContext.findAttribute("comment"))).getCreated()) %>
											</small>
											<strong><small><c:out value="${comment.guestName}"/></small></strong><br>
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
					<input type="text" class="form-control" id="share-link-field" value="<%=shareUrl%>">
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
					<input type="text" class="form-control" id="share-by-email-field">
				</div>
				<div class="modal-footer"><!-- TODO -->
					<button class="btn btn-primary" id="send-by-email-btn">Send</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>