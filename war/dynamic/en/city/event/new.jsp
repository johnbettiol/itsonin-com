<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date"/>
<!DOCTYPE html>
<html>
<head>
<title>itsonin - Events in ${ioiContext.city}</title>
<%@ include file="/dynamic/en/head.jsp"%>
<script type="text/javascript">
	var baseUrl = '${baseUrl}';
</script>
<script type="text/javascript" src="/static/js/modules/new.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		EventNewModule.init();
	});
</script>
</head>
<body>
	<div class="header-container">
		<div class="container" id="events_header">
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
							<a href="javascript:history.back()">
								<span>${ioiContext.city}</span>
							</a>
							<i class="fa fa-angle-right"></i>
							<span>Add Event</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="container" id="event">
		<div class="row">
			<div class="col-sm-offset-3 col-sm-6 event-container">
				<div class="row">
					<div class="col-xs-12">
						<h4>ADD AN EVENT</h4>
						<label>Your name</label>
						<input type="text" class="form-control" id="guest-name-field" value="${cookie['name'].value}">
						<label>Event title</label>
						<input type="text" class="form-control" id="title-field" name="title-field">
						<label>Event description</label>
						<textarea class="form-control" id="description-field" name="description-field"></textarea>
						<label id="promote-event-btn" class="pointer">Promote Event (+)</label>
						<div id="promote-fields" style="display:none">
							<label>Offer</label>
							<input type="text" class="form-control" id="offer-field" name="offer-field">
							<label>Offer email</label>
							<input type="text" class="form-control" id="offer-email-field" name="offer-email-field">
						</div>
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-xs-12">
						<label>Event category</label>
						<div class="btn-group btn-group-justified" id="categories">
							<div class="btn-group">
								<button class="btn mob-btn" id="NIGHTLIFE">
									<span class="fa fa-glass fa-2x"></span>
									<span class="block">NIGHTLIFE</span>
								</button>
							</div>
							<div class="btn-group">
								<button class="btn mob-btn" id="SOCIAL">
									<span class="fa fa-users fa-2x"></span>
									<span class="block">SOCIAL</span>
								</button>
							</div>
							<div class="btn-group">
								<button class="btn mob-btn" id="CULTURAL">
									<span class="fa fa-university fa-2x"></span>
									<span class="block">CULTURAL</span>
								</button>
							</div>
							<div class="btn-group">
								<button class="btn mob-btn" id="FESTIVAL">
									<span class="fa fa-ticket fa-2x"></span>
									<span class="block">FESTIVAL</span>
								</button>
							</div>
							<div class="btn-group">
								<button class="btn mob-btn" id="SPORT">
									<span class="fa fa-futbol-o fa-2x"></span>
									<span class="block">SPORT</span>
								</button>
							</div>
						</div>
						<c:forEach var="eventCategory" items="${eventCategories}" varStatus="loop">
							<div class="subcategories" id="category-${eventCategory}" style="display: none;">
								<div style="margin-bottom: 5px; font-size:12px;">
									<i>Please select one of the following</i>
								</div>
								<div class="btn-group btn-group-justified">
									<c:forEach var="eventSubCategory" items="${eventSubCategories}" varStatus="loop">
										<c:if test="${eventSubCategory.parent == eventCategory}">
											<div class="btn-group">
												<button class="btn mob-btn" id="${eventSubCategory}">
													<span class="fa fa-glass fa-2x"></span>
													<span class="block">${eventSubCategory}</span>
												</button>
											</div>
										</c:if>
									</c:forEach>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-xs-12">
						<label>Event location</label>
						<input type="text" class="form-control" id="location-title-field" placeholder="Enter a location">
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12" style="height: 200px; width: 100%; padding-top: 10px;padding-bottom: 10px">
						<div id="map-canvas" style="height: 100%; width: 100%"></div>
					</div>
				</div>
				<div class="row" style="display:none">
					<div class="col-xs-12">
						<label>Location description</label>
						<textarea class="form-control" id="location-description-field" name="loction-description"></textarea>
						<div class="btn-container">
							<button class="btn btn-default" id="upload-photos-btn">Upload photos</button>
						</div>
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-xs-12">
						<label>Event time and date</label>
						<div>
							<label>Start date & time</label>
							<div class="row">
								<div class="col-xs-6">
									<div class="input-group">
										<input type="text" class="form-control" id="date-from-field"/> 
										<span class="input-group-btn">
											<button class="btn btn-default" type="button" id="date-from-button">
												<span class="glyphicon glyphicon-calendar"></span>
											</button>
										</span>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="input-group">
										<input type="text" class="form-control" id="time-from-field"/> 
										<span class="input-group-btn">
											<button class="btn btn-default" type="button" id="time-from-button">
												<span class="glyphicon glyphicon-time"></span>
											</button>
										</span>
									</div>
								</div>
							</div>
						</div>
						<div>
							<label>End date & time</label>
							<div class="row">
								<div class="col-xs-6">
									<div class="input-group">
										<input type="text" class="form-control" id="date-to-field"/> 
										<span class="input-group-btn">
											<button class="btn btn-default" type="button" id="date-to-button">
												<span class="glyphicon glyphicon-calendar"></span>
											</button>
										</span>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="input-group">
										<input type="text" class="form-control" id="time-to-field"/> 
										<span class="input-group-btn">
											<button class="btn btn-default" type="button" id="time-to-button">
												<span class="glyphicon glyphicon-time"></span>
											</button>
										</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-xs-12">
						<label>Event details</label>
						<div class="row">
							<div class="col-xs-6 visibility">
								<i style="font-size: 12px">Visibility</i>
								<div class="btn-group btn-group-vertical" style="width: 100%">
									<div class="btn-group">
										<button class="btn mob-btn" id="visibility-PUBLIC">
											<span class="fa fa-unlock fa-fw"></span> 
											<span>Public</span>
										</button>
									</div>
									<div class="btn-group">
									    <button class="btn mob-btn" id="visibility-PRIVATE">
											<span class="fa fa-lock fa-fw"></span> 
											<span>Private</span>
										</button>
									</div>
								</div>
							</div>
							<div class="col-xs-6 sharing">
								<i style="font-size: 12px">Sharing</i>
								<div class="btn-group btn-group-vertical" style="width: 100%">
									<div class="btn-group">
										<button class="btn mob-btn" id="sharability-NOSHARE">
											<span class="fa fa-ban fa-fw"></span> 
											<span>No share</span>
										</button>
									</div>
									<div class="btn-group">
									    <button class="btn mob-btn" id="sharability-NORMAL">
											<span class="fa fa-share-alt fa-fw"></span> 
											<span>Normal</span>
										</button>
									</div>
									<div class="btn-group">
									    <button class="btn mob-btn" id="sharability-PYRAMID">
											<span class="fa fa-wifi fa-flip-vertical fa-fw"></span> 
											<span>Pyramid</span>
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row" id="success-alert" style="display:none">
					<div class="col-xs-12">
				        <div class="alert alert-success alert-dismissable">
			            	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
							<span id="success-text"></span>
	            		</div>
	            	</div>
	            </div>
	            <div class="row" id="error-alert" style="display:none">
					<div class="col-xs-12">
			            <div class="alert alert-danger alert-dismissable">
			              <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			              <span id="error-text"></span>
			            </div>
	            	</div>
	            </div>
	            <div class="share" style="display: none">
					<%@ include file="parts/share-bar.jsp" %>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<div class="btn-container pull-right">
							<a href="javascript:history.back()" class="btn btn-default" id="cancel-btn">Cancel</a>
							<button class="btn btn-default" id="save-btn">Save</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>