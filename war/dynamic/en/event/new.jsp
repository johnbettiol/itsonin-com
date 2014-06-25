<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date"/>
<!DOCTYPE html>
<html>
<head>
	<title>itsonin - Events in ${ioiContext.city}</title>
	<%@ include file="../../head.jsp" %>
	<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=places"></script>
	<script type="text/javascript" src="/static/js/modules/new.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
        	EventNewModule.init();
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
					<a href="/${ioiDestination}">It's On In</a>
				</h2>
			</div>
			<div class="row text-center">
				<h4>${ioiContext.city}</h4>
			</div>
			<div class="row">
				<form id="event-form" method="post">
				
				<%--HIDDEN FIELDS --%>
				<input type="hidden" id="eventCategory" name="eventCategory" value="${eventCategory}">
				
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="toolbar toolbar-event">
							<ul class="event-categories">
								<c:forEach var="eventCategory" items="${eventCategories}" varStatus="loop">
									<li>
										<a href="javascript:void(0)" class="event-category" id="${eventCategory}">
											<span class="glyphicon glyphicon-picture"></span>
											<span><c:out value="${eventCategory}"/></span>
										</a>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
				<c:forEach var="eventCategory" items="${eventCategories}" varStatus="loop">
					<div class="panel panel-default" style="display:none" id="event-subcategories-${eventCategory}">
						<div class="panel-body">
							<div class="toolbar toolbar-event">
								<ul class="event-categories">
									<c:forEach var="eventSubCategory" items="${eventSubCategories}">
										<c:if test="${eventSubCategory.parent == eventCategory}">
											<li <c:if test="${event.subCategory == eventSubCategory}">class="active"</c:if>>
												<a href="javascript:void(0)" class="event-subcategory" id="${eventSubCategory}">
													<span class="glyphicon glyphicon-picture"></span>
													<span><c:out value="${eventSubCategory}"/></span>
												</a>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</div>
					</div>
				</c:forEach>
			    <div class="panel panel-default">
		            <div class="panel-body">
		                <label>Your name</label>
		                <input type="text" class="form-control" id="guest-name">
		                <label>Event title</label>
		                <input type="text" class="form-control" id="title" name="title">
		                <label>Event description</label>
		                <input type="text" class="form-control" id="description" name="description">
		            </div>
		        </div>
				<div class="panel panel-default">
					<div class="panel-body">
						<span class="glyphicon glyphicon-calendar pull-left" id="date-type-button"></span>
						<div class="text-center">
							<span id="date-type-sometime">Happening sometime</span>
							<span style="display:none" id="date-type-now">Happening NOW</span>
							<div style="display:none" id="date-type-custom">
								<input size="16" type="text" id="startTime" name="startTime" readonly class="form_datetime">
								<span>to</span>
								<input size="16" type="text" id="endTime" name="endTime" readonly class="form_datetime">
							</div>
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-body">
						<span class="glyphicon glyphicon-map-marker pull-left" id="location-type-button"></span>
						<c:if test="${event.locationAddress != null}">
							<span class="glyphicon glyphicon-globe pull-right" id="show-map-button"></span>
						</c:if>
						<div class="text-center">
							<c:if test="${event.locationAddress == null}">
								<span>Happening somewhere</span>
							</c:if>
							<c:if test="${event.locationAddress != null}">
								<div style="margin-left:50px;margin-right:50px">
									<input type="text" id="locationAddress" name="locationAddress" class="input-block-level form-control" value="${event.locationAddress}"/>
								</div>
							</c:if>
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-body">
					    <div class="row">
			        		<div class="col-md-6">
								<label class="radio inline"><input type="radio" id="visibility" name="visibility" value="PUBLIC" ${event.visibility=='PUBLIC'?'checked':''}>Public</label>
								<label class="radio inline"><input type="radio" id="visibility" name="visibility" value="PRIVATE" ${event.visibility=='PRIVATE'?'checked':''}>Private</label>
								<label class="radio inline"><input type="radio" id="visibility" name="visibility" value="FRIENDSONLY" ${event.visibility=='FRIENDSONLY'?'checked':''}>Friends only</label>
							</div>
			        		<div class="col-md-6">
								<label class="radio inline"><input type="radio" id="sharability" name="sharability" value="NOSHARE" ${event.sharability=='NOSHARE'?'checked':''}>No share</label>
								<label class="radio inline"><input type="radio" id="sharability" name="sharability" value="NORMAL" ${event.sharability=='NORMAL'?'checked':''}>Normal</label>
								<label class="radio inline"><input type="radio" id="sharability" name="sharability" value="PYRAMID" ${event.sharability=='PYRAMID'?'checked':''}>Pyramid</label>
							</div>
						</div>
					</div>
				</div>
				<div class="alert alert-success hide" id="success-message"></div>
				<div class="alert alert-danger hide" id="error-message"></div>
				<div class="btn-group pull-right">
					<a href="/" class="btn btn-default">
						<span class="glyphicon glyphicon-arrow-left"></span><br>Cancel
					</a>
					<c:if test="${event.eventId != null}">
						<button type="submit" class="btn btn-default">
							<span class="glyphicon glyphicon-share"></span><br>Share event
						</button>
					</c:if>
					<c:if test="${event.eventId == null}">
						<button type="submit" class="btn btn-default">
							<span class="glyphicon glyphicon-save"></span><br>Save event
						</button>
					</c:if>
				</div>
				</form>
			</div>
        </div>
      </div>
    </div>
  </div>
  	<%-- MAP MODAL --%>
	<div class="modal" id="map-modal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title">Map</h4>
	      </div>
	      <div class="modal-body">
    		<input id="pac-input" class="form-control" style="margin-bottom:5px" type="text">
    		<div id="map-canvas" style="height: 400px;"></div>
	      </div>
	      <div class="modal-footer">
			<button type="button" class="btn btn-default" id="ok-button">Ok</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	      </div>
	    </div>
	  </div>
	</div>
</body>
</html>