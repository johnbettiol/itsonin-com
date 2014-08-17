var EventListModule = (function() {

	var filteredEvents = [];
	var filter = {
			types: [],
			startTime: moment().set('hour', 0).set('minute', 0).set('second', 0).set('millisecond', 0)
	};
	var map = {};
	var markers = [];
	var isMapShown = false;
	var isMapShownOnTop = false;
	var highlightedIcon = new google.maps.MarkerImage('/static/img/marker/icon_9.png', new google.maps.Size(32,32));
	var oldIcon;

	return {
		init: function() {
			var self = this;

			//preload sprite
			$('<img/>').attr('src', '/static/img/sprites.png').appendTo('body').css('display','none');
			//init map
			$('#map-canvas').width($('#list-container').width());
			self.initMap();
	
			$('#map-btn').on('click', function() {
				filteredEvents = events;
				$(".filters").hide();

				if(isMapShown == true) {
					$('.map').css('visibility', 'hidden');
					$('.map').css('position', 'absolute');
					$('.map').css('left', '-10000');
					$('.events-container').css('margin-top', '');
				} else {
					$('.map').css('visibility', 'inherit');
					$('.map').css('position', '');
					$('.map').css('left', '');
					$('.events-container').css('margin-top', '230px');
				}
				//self.initMap();
				//google.maps.event.trigger(self.map, 'resize');
				//self.map.setCenter(new google.maps.LatLng(51.227741, 6.773456));
				isMapShown = !isMapShown;
			});

			$('#filter0-btn').on('click', function() {
				isMapShown = false;
				$('.map').css("visibility", "hidden");
				$('.map').css("position", "absolute");
				$('.filters').toggle();

				if($('.filters').is(':hidden') == true) {
					$('.events-container').css('margin-top', '');
				} else {
					$('.events-container').css('margin-top', '230px');
				}
				self.renderEvents(events);
			});

			$('#search-btn').on('click', function() {
				$('.list-search-bar').removeClass('hidden');
				$('.list-filters').addClass('hidden');

				$('#search-input').val('');
				self.renderEvents(events);
			});

			$('#search-events-btn').on('click', function() {
				self.filterEventsByText($('#search-input').val());
			});
	
			$('#happening-now-btn').on('click', function() {
				self.filterNowEvents();
			});

			$('.categories li').on('click', function() {
				$('.categories li').each(function (i) {
					$(this).removeClass('active');
				});
				$(this).addClass('active');
				self.filterEvents('subCategory', $(this).attr('id'));
			});

			$('.event-item').on('mouseover', function() {
				oldIcon = self.setMarkerIcon($(this).attr('id'), highlightedIcon);
			});

			$('.event-item').on('mouseout', function() {
				self.setMarkerIcon($(this).attr('id'), oldIcon);
			});

			$('#date-field').pickadate();
			//TODO: use this one https://github.com/xdan/datetimepicker or this https://github.com/dbushell/Pikaday

			//$(document).on('touchmove', self.handleScroll);
			//$(document).on('scroll', self.handleScroll);
			
			/*$(window).resize(function() {
				$('.filters').width($('#list-container').width());
			});*/

			$.views.helpers({
				formatDate: function (val) {
					return moment(val).format('h:mm')
				}
			});
		},

		filterEvents: function(field, value) {
			filteredEvents = $.grep(events, function(e, i) {
				return value === e[field];
			});
			this.renderEvents(filteredEvents);
		},

		filterEventsByText: function(text) {
			filteredEvents = $.grep(events, function(e, i) {
				return  e.title.toLowerCase().indexOf(text.toLowerCase()) > -1
			});
			this.renderEvents(filteredEvents);
		},

		filterNowEvents: function() {
			var now = new Date();
			filteredEvents = $.grep(events, function(e, i) {
				return e.startTime < now && e.endTime > now;
			});
			this.renderEvents(filteredEvents);
		},

		loadEvents: function(params, callback) {
			$.get('/api/event/list', params, callback);
		},

		renderEvents: function(events) {
			var self = this;
			var prev;
			$.each(events, function(index, event) {
				if(prev && event.startTime == prev.startTime && event.endTime == prev.endTime){
					event['hideTime'] = true;
				}
				prev = event;
			});
			$('#list-container .col-sm-12').html($('#eventTemplate').render(events));

			$('.event-item').on('mouseover', function() {
				oldIcon = self.setMarkerIcon($(this).attr('id'), highlightedIcon);
			});

			$('.event-item').on('mouseout', function() {
				self.setMarkerIcon($(this).attr('id'), oldIcon);
			});
		},

		initMap: function(){
			var self = this;
			var mapOptions = {
					zoom: 10,
					center: new google.maps.LatLng(51.227741, 6.773456),
					disableDefaultUI: true,
					panControl: false,
					zoomControl: true,
					zoomControlOptions: {
						position: google.maps.ControlPosition.LEFT_CENTER
					},
					scaleControl: false,
					mapTypeId: google.maps.MapTypeId.ROADMAP
				  }
			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

			for(var i=0; i<events.length; i++) {
				var marker = self.addMarker(events[i], i);
				markers.push(marker);
				events[i]['__gm_id'] = marker['__gm_id']
			}

			zoomChangeListener = google.maps.event.addListener(map, 'zoom_changed', function (event) {
	
			});

			zoomChangeListener = google.maps.event.addListener(map, 'dragend', function (event) {

			});

			var timeout;
			zoomChangeBoundsListener = google.maps.event.addListener(map,'bounds_changed',function (event) {  
				//google.maps.event.removeListener(zoomChangeBoundsListener);
				/*window.clearTimeout(timeout);
				timeout = window.setTimeout(function () {
					...
				}, 500);*/
			});

			google.maps.event.addListener(map, 'idle', function() {
				//http://stackoverflow.com/questions/4338490/google-map-event-bounds-changed-triggered-multiple-times-when-dragging
				if(isMapShown == true) {
					var bounds = map.getBounds();
	
					filteredEvents = [];
					for(var i = 0; i < markers.length; i++){
						var marker = markers[i];
						if(bounds.contains(marker.position)) {	
							for(var j = 0; j < events.length; j++) {
								if(marker['__gm_id'] == events[j]['__gm_id']) {
									filteredEvents.push(events[j]);
								}
							}
						}
					}
				   	self.renderEvents(filteredEvents);
				}
				
				//highlight marker: http://www.geocodezip.com/v3_MW_example_hoverchange.html
			});

		},

		addMarker: function (event, index) {
			var self = this;
			var icon = new google.maps.MarkerImage('/static/img/marker/icon_' + index + '.png', new google.maps.Size(32,32))
			var marker= new google.maps.Marker({
				position:  new google.maps.LatLng(event.gpsLat, event.gpsLong),
				icon: icon,
				map: map,
				title: event.title});

			google.maps.event.addListener(marker, "click", function() {
				//self.map.panTo(marker.getPosition());
				self.scroll(event.eventId);
			});
			
			google.maps.event.addListener(marker, "mouseover", function() {
				this.setZIndex(google.maps.Marker.MAX_ZINDEX + 1);
	        });

			return marker;
		},

		setMarkerIcon: function (eventId, icon) {//TODO:.split('-')[1]; 
			var oldIcon;
			if(isMapShown == true) {
				$.each(events, function(index, event) {
					if(eventId == event.eventId){	
						$.each(markers, function(index, marker) {
							if(marker['__gm_id'] == event['__gm_id']) {
								oldIcon = marker.getIcon();
								marker.setIcon(icon);
								marker.setZIndex(google.maps.Marker.MAX_ZINDEX + 1);
							}
						});
					}
				});
			}
			return oldIcon;
		},

		handleScroll: function() {return;
			if($(window).width() <= 640){
				if($(window).scrollTop() > 0) {
					if(isMapShown == true && isMapShownOnTop == false) {
						$('.map').css({
							'width': '100%',
							'position': 'fixed',
							'top': '50px',
							'z-index': 10
						});
						/*$('.list-container').css({
							'margin-top': '200px'//180
						});*/
					}
					isMapShownOnTop = true;
				} else {
					if(isMapShown == true) {
						/*$('.list-container').css({
							'margin-top': ''
						});*/
						$('.map').css({
							'width': '',
							'position': '',
							'top': ''
						});

					}
					isMapShownOnTop = false;
				}
			}
		},

		scroll: function (id) {
			$('body,html').stop().animate({
				scrollTop: $("#"+id).position().top
			});
		}
	}
}());