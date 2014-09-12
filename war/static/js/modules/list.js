var EventListModule = (function() {

	var RED_MARKER = 'http://labs.google.com/ridefinder/images/mm_20_red.png';

	var filteredEvents = events;
	var filteredByMapEvents = [];
	var filter = {
		favorites: false,
		promo: false,
		hot: false,
		category: undefined,
		date: moment()
	};
	var map = {};
	var markers = [];
	var isMapShown = false;
	var highlightMarkerOnScroll = true;

	return {
		init: function() {
			var self = this;

			$('#map-btn').on('click', function() {
				filteredByMapEvents = filteredEvents;
				$(".filters").hide();
				$(this).toggleClass('active');
				if($('#filter-btn').hasClass('active')){
					$('#filter-btn').toggleClass('active');
				}

				if(isMapShown == true) {
					$('.map').css('visibility', 'hidden');
					$('.map').css('position', 'absolute');
					$('.map').css('left', '-10000');
					$('.events-container').css('margin-top', '');
				} else {
					$('.map').css('visibility', 'inherit');
					$('.map').css('position', 'relative');
					$('.map').css('left', '');
					$('.events-container').css('margin-top', '200px');
					map.panTo(new google.maps.LatLng(51.227741, 6.773456));
				}

				$(window).trigger('resize');
				
				isMapShown = !isMapShown;
			});

			$('#filter-btn').on('click', function() {
				isMapShown = false;
				$('.map').css("visibility", "hidden");
				$('.map').css("position", "absolute");
				$('.filters').toggle();
				$(this).toggleClass('active');
				if($('#map-btn').hasClass('active')){
					$('#map-btn').toggleClass('active');
				}

				if($('.filters').is(':hidden') == true) {
					$('.events-container').css('margin-top', '');
				} else {
					$('.events-container').css('margin-top', '200px');
				}
				self.renderEvents(filteredEvents);
				$(window).trigger('resize');
			});

			$('#favourites-button').on('click', function() {
				filter.favourites = !filter.favourites;
				if(filter.favourites == true) {
					$('#filter-date').text('favourites');
					$('#prev-day-button').hide();
					$('#next-day-button').hide();
					self.filterFavourites();
				} else {
					$('#filter-date').text(filter.date.calendar());
					$('#prev-day-button').show();
					$('#next-day-button').show();
					//TODO: render events
				}
			});

			$('#promo-button').on('click', function() {

			});

			$('#prev-day-button').on('click', function() {
				filter.date = filter.date.subtract(1, 'days');
				$('#filter-date').text(filter.date.calendar());

				self.loadEvents({date: filter.date.format('YYYY-MM-DD')});
			});

			$('#next-day-button').on('click', function() {
				filter.date = filter.date.add('days', 1);
				$('#filter-date').text(filter.date.calendar());

				self.loadEvents({date: filter.date.format('YYYY-MM-DD')});
			});

			$('#filter-categories a').on('click', function() {
				var isActive = $(this).hasClass('active');
				$('#filter-categories a').each(function (i) {
					$(this).removeClass('active');
				});

				if(isActive == false) {
					$(this).addClass('active');
					filter.category = $(this).attr('id');
				} else {
					filter.category = undefined;
				}

				self.filterEvents();
			});

			$('#filter-buttons a').on('click', function() {
				var isActive = $(this).hasClass('active');
				$('#filter-buttons a').each(function (i) {
					$(this).removeClass('active');
				});
				if(isActive == false) {
					$(this).addClass('active');
				}

				self.filterEvents();
			});

			$('.event-item').on('click', function() {
				var eventId = $(this).attr('id');
				self.highlightMarker(eventId);
			});

			$('#filter-date').pickadate({
				onSet: function(context) {
					filter.date = moment(context.select)
					$('#filter-date').text(filter.date.calendar());
					self.loadEvents({date: filter.date.format('YYYY-MM-DD')});
				}
			});

			$.views.helpers({
				formatTime: function (val) {
					if(val) {
						return moment(val).format('hh:mm A')
					} else {
						return '';
					}
				},
				formatDate: function (val) {
					if(val) {
						return moment(val).format('MMM D, hh:mm A')
					} else {
						return '';
					}
				}
			});

			moment.locale('en', {
				calendar : {
					lastDay : '[Yesterday ] dddd MMMM D',
					sameDay : '[Today ] dddd MMMM D',
					nextDay : '[Tomorrow ] dddd MMMM D',
					lastWeek : '[last] dddd MMMM D',
					nextWeek : 'dddd MMMM D',
					sameElse : 'dddd MMMM D'
				}
			});

			self.loadScript();

			$(window).on('resize', function(){
				var $lastEvent = $('#list-container').children().last();
				if($lastEvent) {
					$('#free-space').height($(this).height() - ($lastEvent.height() + 215));
				}
				$('#map-canvas').width($('#list-container').width());
			}).trigger('resize');

			$(window).scroll(function() {
				if(highlightMarkerOnScroll == true) {
					$('.event-item').each(function (i) {
						var isTop = self.isTopVisibleEvent($(this))
						if(isTop == true){
							self.highlightMarker($(this).attr('id'));
						}
					});
				}
			});
		},

		getFilter: function() {
			return filter;
		},

		filterEvents: function() {
			filteredEvents = $.grep(events, function(event, i) {
				var fits = true;
				if(filter.category && filter.category != event.category) {
					fits = false;
				}
				return fits;
			});
			this.renderEvents(filteredEvents);
			this.initMarkers(filteredEvents);
		},

		filterEventsByText: function(text) {
			filteredEvents = $.grep(events, function(e, i) {
				return  e.title.toLowerCase().indexOf(text.toLowerCase()) > -1
			});
			this.renderEvents(filteredEvents);
		},

		loadEvents: function(params) {
			var self = this;
			self.showSpinner();

			$.ajax({
				type: 'GET',
				url: '/api/event/list',
				data: params,
				contentType: "application/json",
				dataType: 'json'
			}).done(function(data, textStatus, jqXHR) {
				events = data;
				self.filterEvents();
				self.hideSpinner();
			}).fail(function(jqXHR, textStatus, errorThrown) {
				self.hideSpinner();
			});
		},

		renderEvents: function(events) {
			var self = this;
			$('#list-container').html($('#eventTemplate').render(events));

			$('.event-item').on('click', function() {
				var eventId = $(this).attr('id');
				self.highlightMarker(eventId);
			});
		},

		loadScript: function() {
			var script = document.createElement('script');
			script.type = 'text/javascript';
			script.src = 'https://maps.googleapis.com/maps/api/js?sensor=true' +
				'&libraries=places,visualization&language=en-US&v=3.2&callback=EventListModule.initMap';
			document.body.appendChild(script);
		},

		initMap: function(){
			var self = this;
			$('#map-canvas').width($('#list-container').width());

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

			google.maps.event.addListener(map, 'idle', function() {
				//http://stackoverflow.com/questions/4338490/google-map-event-bounds-changed-triggered-multiple-times-when-dragging
				if(isMapShown == true) {
					var bounds = map.getBounds();

					filteredByMapEvents = [];
					for(var i = 0; i < markers.length; i++){
						var marker = markers[i];
						if(bounds.contains(marker.position)) {	
							for(var j = 0; j < events.length; j++) {
								if(marker['__gm_id'] == events[j]['__gm_id']) {
									filteredByMapEvents.push(events[j]);
								}
							}
						}
					}
				   	self.renderEvents(filteredByMapEvents);
				}
			});

			google.maps.event.addDomListener(window, "resize", function() {
				 google.maps.event.trigger(map, "resize");
			});

			self.initMarkers(events);
		},

		initMarkers: function (events) {
			//remove old markers from map
			for(var i=0; i < markers.length; i++){
				markers[i].setMap(null);
			}

			for(var i=0; i < events.length; i++) {
				var marker = this.addMarker(events[i], i);
				markers.push(marker);
				events[i]['__gm_id'] = marker['__gm_id'];
			}
		},

		addMarker: function (event, index) {
			var self = this;
			var marker= new google.maps.Marker({
				position:  new google.maps.LatLng(event.gpsLat, event.gpsLong),
				icon: self.getBlueIcon(),
				map: map,
				title: event.title});

			google.maps.event.addListener(marker, "click", function() {
				$.each(markers, function(index, marker) {
					marker.setIcon(self.getBlueIcon());
				});
				marker.setIcon(RED_MARKER);
				marker.setZIndex(google.maps.Marker.MAX_ZINDEX + 1);
				self.scrollToEvent(event.eventId);
			});
			
			google.maps.event.addListener(marker, "mouseover", function() {
				this.setZIndex(google.maps.Marker.MAX_ZINDEX + 1);
			});

			return marker;
		},

		highlightMarker: function(eventId) {
			var self = this;

			$.each(markers, function(index, marker) {
				marker.setIcon(self.getBlueIcon());
			});

			if(isMapShown == true) {
				$.each(events, function(index, event) {
					if(eventId == event.eventId){	
						$.each(markers, function(index, marker) {
							if(marker['__gm_id'] == event['__gm_id']) {
								marker.setIcon(RED_MARKER);
								marker.setZIndex(google.maps.Marker.MAX_ZINDEX + 1);
							}
						});
					}
				});
			}
		},

		scrollToEvent: function(id) {
			highlightMarkerOnScroll = false;
			$('body,html').stop().animate({
					scrollTop: $("#"+id).position().top
				}, 400, 'swing',
				function(){
					highlightMarkerOnScroll = true;
			});
		},

		isTopVisibleEvent: function(elem) {
			var docViewTop = $(window).scrollTop();
			var elemTop = $(elem).offset().top;
			var elemBottom = elemTop + $(elem).height();
			return (elemTop+5 >= docViewTop+200 && elemTop <= docViewTop + 200 + $(elem).height());
		},
		
		getBlueIcon: function() {
			return {
				path: google.maps.SymbolPath.CIRCLE,
				fillOpacity: 0.5,
				fillColor: '#0066FF',
				strokeOpacity: 1.0,
				strokeColor: 'black',
				strokeWeight: 1.0, 
				scale: 4 //pixels
			};
		},

		showSpinner: function() {
			$('body').append('<div class="overlay"><div class="spinner"><i class="fa fa-spinner fa-spin"></i></div></div>');
		},

		hideSpinner: function() {
			$('.overlay').remove();
		}
	}
}());