var EventNewModule = (function() {
		
	var dateTypeState = 0;
	var dateTypes = ["date-type-sometime", "date-type-now", "date-type-custom"];
	var locationTypeState = 0;
	var locationTypes = ["location-type-sometime", "date-type-now", "date-type-custom"];
	var locationMarker = null;

	return {
		init: function() {
			var self = this;

			$('#upload-photos-btn').on('click', function() {
				alert('upload photos...');
			});

			$('#cancel-btn').on('click', function() {
				alert('cancel...');
			});

			$('#event-form').submit(self.saveEvent);

			$('#dateFrom').pickadate();
			$('#dateTo').pickadate();
			$('#timeFrom').pickatime();
			$('#timeTo').pickatime();

			$('.categories li').on('click', function() {
				$('.categories li').each(function (i) {
					$(this).removeClass('active');
					$(this).find('.icon').removeClass('active');
				});
				$(this).addClass('active');
				$(this).find('.icon').addClass('active');
			});

			$('input:radio[name=category]').on('change', function() {
				if($(this).val() == 'MEET') {
					$("#goto-categories").hide();
					$("#meet-categories").show();
				} else {
					$("#goto-categories").show();
					$("#meet-categories").hide();
				}
			});

			self.initLocationAutocomplete();

			self.initMap();
		},

		saveEvent: function(event) {
			if ($('#name').val().length == 0) {
				$('#error-message').html('Guest name is required');
				$('#error-message').show();
				event.preventDefault();
			}
		},

		createEvent: function(event, guest, callback) {
			$.ajax({
				type: 'POST',
				url: '/api/event/create',
				data: JSON.stringify({event:event, guest:guest}),
				success: callback,
				contentType: "application/json",
				dataType: 'json'
			});
		},

		updateEvent: function(event, guest, callback) {
			$.ajax({
				type: 'POST',
				url: '/api/event/' + event.eventId + '/update',
				data: JSON.stringify({event:event, guest:guest}),
				success: callback,
				contentType: "application/json",
				dataType: 'json'
			});
		},

		initLocationAutocomplete: function() {
			var autocomplete = new google.maps.places.Autocomplete($("#event-location")[0], {});
			/*google.maps.event.addListener(autocomplete, 'place_changed', function() {
				var place = autocomplete.getPlace();
				//TODO
				$scope.event['locationAddress'] = place.name;
				$scope.event['gpsLat'] = place.geometry.location.lat();
				$scope.event['gpsLong'] = place.geometry.location.lng();
			});*/

			google.maps.event.addListener(autocomplete, 'place_changed', function() {
				if(!locationMarker) {
					locationMarker = new google.maps.Marker({
				    map: map
				  });
				}
				locationMarker.setVisible(false);
				var place = autocomplete.getPlace();
				if (!place.geometry) {
					return;
				}
				// If the place has a geometry, then present it on a map.
				if (place.geometry.viewport) {
					map.fitBounds(place.geometry.viewport);
				} else {
					map.setCenter(place.geometry.location);
					map.setZoom(17);  // Why 17? Because it looks good.
				}
				locationMarker.setPosition(place.geometry.location);
				locationMarker.setVisible(true);
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
		}
	}
}());