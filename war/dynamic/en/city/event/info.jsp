<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.itsonin.utils.DateTimeUtil"%>
<%@ page import="com.itsonin.entity.*"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ page session="false" %>

<jsp:useBean id="timeUtil" class="com.itsonin.utils.DateTimeUtil"/>

<!DOCTYPE html>
<html>
<head>
	<title>itsonin - Events in ${ioiContext.city}</title>
	<%@ include file="../../head.jsp" %>
</head>
<body>
	<div id="view">
	<script type="text/javascript">
		var eventJson = ${eventJson};
		var guestJson = ${guestJson};
		var commentsJson = ${commentsJson};
		var guestsJson = ${guestsJson};
		var shareUrl = '${shareUrl}';
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
			EventInfoModule.init();
		});
	</script>
	<script id="commentTemplate" type="text/x-jsrender">
		<div class="list-group-item comment-item" id="{{:commentId}}">
			<div class="media">
				<div class="media-body">					
					<strong><small>{{:comment}}</small></strong>
					<br>
					<div class="text-muted">
						<small>
							{{:guestName}}
							{{if ~isYou(guestId)==true}}(you){{/if}}
						</small>
						<small class="pull-right">
							{{:~formatTime(created)}}
						</small>
					</div>
				</div>
			</div>
		</div>
	</script>
	<script id="guestTemplate" type="text/x-jsrender">
		<div class="list-group-item guest-item" id="{{:guestId}}">
			<div class="media">
				<div class="pull-left">
					<i class="fa fa-user"></i>
				</div>
				<div class="media-body clearfix">
					<span class="pull-left">{{:name}}
						{{if ~isYou(guestId)==true}}(you){{/if}}
					</span>
					<span class="pull-right">
						{{if type=='HOST'}}
							{{:type}}
						{{/if}}
						{{if type!='HOST'}}
							{{:status}}
						{{/if}}
					</span>
				</div>
			</div>
		</div>
	</script>
	<div class="header-container">
		<div class="container" id="info-header">
			<div class="row">
				<div class="col-sm-offset-3 col-sm-6">
					<div class="row header">
						<div class="header-logo pull-left">
							<a href="#">
								<img src="/static/img/itsonin-white.png" height="20" width="20">
							</a>
						</div>
						<div class="header-title pull-left">
							<i class="fa fa-angle-right"></i>
							<c:if test="${isInvitation == true}">
								<a href="/${ioiContext.locale}/${ioiContext.city}/Events">
									<span>${ioiContext.city}</span>
								</a>
							</c:if>
							<c:if test="${isInvitation == false}">
								<a href="javascript:history.back()">
									<span>${ioiContext.city}</span>
								</a>
							</c:if>
						</div>
						<div class="header-actions pull-right">
							<button id="scrollto-comments-btn">
								<i class="fa fa-2x fa-comment-o"></i>
							</button>
						</div>
					</div>
					<div class="row">
						<div class="list-group-item event-item" id="${event.eventId}">
							<div class="media">
								<div class="event-icon">
									<c:if test="${isInvitation == true}">
										<a href="/${ioiContext.locale}/${ioiContext.city}/Events" id="back-link">
											<i class="fa fa-angle-left" style="font-size:24px"></i>
										</a>
									</c:if>
									<c:if test="${isInvitation == false}">
										<a href="javascript:history.back()" id="back-link">
											<i class="fa fa-angle-left" style="font-size:24px"></i>
										</a>
									</c:if>&nbsp;
									<span class="fa fa-university fa-2x"></span>
								</div>
								<div class="media-body clearfix event-body" style="margin-left: 70px">
									<div class="media-heading event-title"><c:out value="${event.title}"/></div>
									<p class="event-offer">
										<c:if test="${not empty event.offerRef}">
											<c:out value="${event.offer}"/>
										</c:if>
									</p>
									<div class="event-time text-muted">
										<i class="fa fa-clock-o fs-11"></i>
										<fmt:formatDate type="time" pattern="HH:mm yyyy/MM/dd" value="${event.startTime}"/>
										<c:if test="${not empty event.endTime}"> - </c:if>
										<!-- TODO: change, it can be <1  e.g. 18.09 23:00 - 19.09 02:00-->
										<c:if test="${(event.endTime.time - event.startTime.time) / (1000*60*60*24) < 1 }">
											<fmt:formatDate type="time" pattern="HH:mm" value="${event.endTime}"/>
										</c:if>
										<c:if test="${(event.endTime.time - event.startTime.time) / (1000*60*60*24) >= 1 }">
											<fmt:formatDate type="time" pattern="HH:mm yyyy/MM/dd" value="${event.endTime}"/>
										</c:if>
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
					<div style="height: 200px; width: 100%;">
						<div id="map-canvas" style="height: 100%; width: 100%"></div>
					</div>
				</div>
				<div class="location">
					<div class="address">
						<c:out value="${event.locationAddress}"/>
					</div>
					<div class="navigation">
						<a href="#" id="open-navigation-link">
							<i class="fa fa-2x fa-location-arrow"></i>
						</a>
					</div>
				</div>
				<c:if test="${not empty event.description}">
					<div class="description">
						<p><c:out value="${event.description}"/></p>
					</div>
				</c:if>
				<c:if test="${guest.type != 'HOST'}">
					<div class="attend">
						<label>Would you like to attend this event?</label>
						<input type="text" class="form-control" id="guest-name-field" placeholder="Your name" value="${guest.name!=null ? guest.name: cookie['name'].value}">
						<div class="btn-container text-center clearfix">
							<button class="btn pull-left ${guest.status == 'NO' ? 'btn-primary' : 'btn-default'}" id="decline-btn">No</button>
							<button class="btn ${guest.status == 'MAYBE' ? 'btn-primary' : 'btn-default'}" id="maybe-attend-btn">Maybe</button>
							<button class="btn pull-right ${guest.status == 'YES' ? 'btn-primary' : 'btn-default'}" id="attend-btn">Yes</button>
						</div>
					</div>
					<div id="pyramid-alert" class="alert alert-warning" role="alert" style="display:none">Pyramid Event - You must share to others!</div>
					<hr/>
				</c:if>
				<div class="share"
					<c:if test="${guest.status == 'VIEWED' || guest.status == null || event.sharability == 'NOSHARE'}">style="display:none"</c:if>>
					<%@ include file="parts/share-bar.jsp" %>
				</div>
				<div class="row guests">
					<label style="margin-left: 15px">Guests</label> <span class="badge ng-binding" id="guests-counter"><c:out value="${fn:length(guests)}"/></span>
					<div class="panel panel-default">
						<div class="list-group">
							<c:forEach var="g" items="${guests}">
								<div class="list-group-item guest-item" id="${g.guestId}">
									<div class="media">
										<div class="pull-left">
											<i class="fa fa-user"></i>									
										</div>
										<div class="media-body clearfix">
											<span class="pull-left"><c:out value="${g.name}"/>
											<c:if test="${g.guestId==guest.guestId}">(you)</c:if>
											</span>
											<span class="pull-right">
												<c:if test="${g.type=='HOST'}">
													<c:out value="${g.type}"/>
												</c:if>
												<c:if test="${g.type!='HOST'}">
													<c:out value="${g.status}"/>
												</c:if>
											</span>
										</div>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
				<div class="row comments">
					<label style="margin-left: 15px">Comments</label> <span class="badge ng-binding" id="comments-counter"><c:out value="${fn:length(comments)}"/></span>
					<div class="panel panel-default">
						<div class="list-group">
							<c:forEach var="comment" items="${comments}">
								<div class="list-group-item comment-item" id="${comment.commentId}">
									<div class="media">
										<div class="media-body ">
											<strong><small><c:out value="${comment.comment}"/></small></strong>
											<br>
											<div class="text-muted">
												<small>
													<c:out value="${comment.guestName}"/>
													<c:if test="${comment.guestId==guest.guestId}">(you)</c:if>
												</small>
												<small class="pull-right">
												<%=DateTimeUtil.prettyFormat(((Comment)(pageContext.findAttribute("comment"))).getCreated())%>
												</small>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
						</div>
						<div class="panel-footer" <c:if test="${guest.status == 'VIEWED' || guest.status == null}">style="display:none"</c:if>>
							<textarea class="form-control" id="comment-field" style="display:inline-block;" placeholder="Type your message here ..." rows="2"></textarea>
							<div class="text-right">
							<button class="btn btn-default" id="add-comment-btn">Send</button>
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
					<input type="text" class="form-control" id="share-link-field" value="${shareUrl}">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	</div>
</body>
</html>