<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
	<title>itsonin - Events in ${ioiContext.city}</title>
	<%@ include file="../../head.jsp" %>
	<script type="text/javascript" src="/static/js/modules/view.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
        	EventInvitationModule.init();
        });
    </script>
</head>
<body>
  <div id="wrap">
    <div class="container">
      <div class="row row-main">
        <div class="col-sm-offset-3 col-sm-6 col-md-offset-3 col-md-6 col-lg-offset-3 col-lg-6">
		    <div class="row text-center">
				<h2>
					<a href="/">It's On In</a>
				</h2>
			</div>
			<div class="row text-center">
				<h4><c:out value="${ioiContext.city}"/></h4>
			</div>
			<div class="text-center event-title">
				<span><c:out value="${event.title}"/></span>
				<div>
					<a href="/e/${event.eventId}/edit" class="btn btn-default btn-big"> 
						<span class="glyphicon glyphicon-pencil"></span>
					</a>
				</div>
			</div>
		    <div class="panel panel-default">
		     	<div class="panel-body">
		       		<span class="glyphicon glyphicon-tree-conifer pull-left"></span>
		       		<div class="fields pull-left">
		       			<span class="field"><fmt:formatDate pattern="yyyy-MM-dd" value="${event.startTime}" /></span>
		       			<span class="field"><c:out value="${event.title}"/></span>
		       			<span class="field"><c:out value="${event.locationTitle}"/></span>
		       			<span class="field"><c:out value="${event.locationAddress}"/></span>
		       			<span class="field"><c:out value="${event.description}"/></span>
		       			<span class="field"><c:out value="${event.sharability}"/></span>
		       			<span class="field"><c:out value="${event.visibility}"/></span>
		       		</div>
		       	</div>
		    </div>
		    <c:if test="${event.sharability == 'PYRAMID' && guest.status == 'ATTENDING'}">
			    <div class="panel panel-default">
					<div class="panel-body">
						<h5>You are attending this event</h5>
						<h5>NOTE. This is a pyramid event you are asked to share this event with your friends</h5>
					</div>
				</div>
			</c:if>
			<c:if test="${(guest.status == 'ATTENDING' || guest.status == 'DECLINED') && event.sharability != 'NOSHARE'}">
			    <div class="panel panel-default">
					<div class="panel-body">
						<div class="toolbar">
							<ul>
								<li><a href="javascript:void(0)" id="share-link"><span class="glyphicon glyphicon-link"></span><span>Share link</span></a></li>
								<li><a href="javascript:void(0)" id="share-by-email"><span class="glyphicon glyphicon-envelope"></span><span>Email</span></a></li>
								<li><a href="javascript:void(0)" id="share-on-facebook"><span class="glyphicon glyphicon-gbp"></span><span>Facebook</span></a></li>
								<li><a href="javascript:void(0)" id="share-on-google"><span class="glyphicon glyphicon-usd"></span><span>Google+</span></a></li>
							</ul>
						</div>
					</div>
				</div>
			</c:if>
		    <div class="panel panel-default">
				<div class="panel-body">
					<form id="attend-event-form" method="post">
						<label>Your name</label>
						<c:if test="${guest.status != 'ATTENDING'}">
							<input type="text" class="form-control" id="name" name="name" value="${guest.name}">
						</c:if>
						<c:if test="${guest.status == 'ATTENDING'}">
							<span class="field"><c:out value="${guest.name}"/></span>
						</c:if>
					</form>
				</div>
			</div>
		    <div class="alert alert-success hide" id="success-message"></div>
		    <div class="alert alert-danger hide" id="error-message"></div>
		    
		    <c:if test="${guest.status == 'PENDING'}">
			    <div class="panel panel-default">
					<div class="panel-body">
						<button class="btn btn-default pull-left">Attend</button>
						<button class="btn btn-default pull-right">Decline</button>
					</div>
				</div>
			</c:if>
			<c:if test="${viewonly == true && guest.status != 'PENDING'}"><!-- TODO: viewonly -->
				<div class="panel panel-default">
					<div class="panel-body">
						<c:if test="${guest.status=='VIEWED' || guest.status=='DECLINED'}">
							<button class="btn btn-default pull-right" id="attend-event-button">
								<span class="glyphicon glyphicon-share"></span><br>Attend
							</button>
						</c:if>
						<c:if test="${guest.status=='ATTENDING'}">
							<button class="btn btn-default pull-right" id="decline-event-button">
								<span class="glyphicon glyphicon-remove"></span><br>Decline
							</button>
						</c:if>
					</div>
				</div>
			</c:if>
			
			<c:forEach var="guest" items="${guests}">
				<div class="panel panel-default">
			    	<div class="panel-body">
			    	   <span class="glyphicon glyphicon-user pull-left"></span>
			    	   <div class="fields pull-left">
			    	       <span class="field"><c:out value="${guest.name}"/></span>
			    	       <span class="field"><c:out value="${guest.status}"/></span>
			    	   </div>
			    	</div>
			    </div>
		    </c:forEach>
		    <c:forEach var="comment" items="${comments}">
			    <div class="panel panel-default">
			    	<div class="panel-body">
			    	   <span class="glyphicon glyphicon-user pull-left"></span>
			    	   <div class="fields pull-left">
			    	       <span class="field bold"><c:out value="${comment.created}"/></span>
			               <span class="field"><c:out value="${comment.comment}"/></span>
			    	   </div>
			    	</div>
			    </div>
		    </c:forEach>
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