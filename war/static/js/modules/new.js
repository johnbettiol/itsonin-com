var EventNewModule = (function() {
		
	var dateTypeState = 0;
	var dateTypes = ["date-type-sometime", "date-type-now", "date-type-custom"];
	var locationTypeState = 0;
	var locationTypes = ["location-type-sometime", "date-type-now", "date-type-custom"];
	var locationMarker = null;
	var event = {//default values
		status: 'ACTIVE',
		visibility: 'PRIVATE',
		sharability: 'NORMAL'
	};
	var guest = {};

	return {
		init: function() {
			var self = this;

			$('#upload-photos-btn').on('click', function() {
				alert('upload photos...');
			});

			$('#save-btn').on('click', function() {
				guest['name'] = $('#guest-name-field').val();
				
				event['title'] = $('#title-field').val();
				event['description'] = $('#description-field').val();
				event['offer'] = $('#offer-field').val();
				event['visibility'] = $('#visibility-field').val();
				event['sharability'] = $('#sharability-field').val();
				event['startTime'] = $('#dateFrom-field').val() + 'T' + $('#timeFrom-field').val() + ':00'; //"yyyy-MM-dd'T'HH:mm:ss"
				event['endTime'] = $('#dateTo-field').val() + 'T' + $('#timeTo-field').val() + ':00';
				
				self.saveEvent();
				console.log(event);
			});

			var datepickerOptions = {
				format: 'yyyy-mm-dd',
				formatSubmit: 'yyyy-mm-dd'
			}
			var timepickerOptions = {
				format: 'HH:i',
				formatSubmit: 'HH:i'
			}
			$('#dateFrom-field').pickadate(datepickerOptions);
			$('#dateTo-field').pickadate(datepickerOptions);
			$('#timeFrom-field').pickatime(timepickerOptions);
			$('#timeTo-field').pickatime(timepickerOptions);

			$('.categories li').on('click', function() {
				$('.categories li').each(function (i) {
					$(this).removeClass('active');
					$(this).find('.icon').removeClass('active');
				});
				event['subCategory'] = $(this).attr('id');
				alert($(this).attr('id'));
				$(this).addClass('active');
				$(this).find('.icon').addClass('active');
			});

			$('input:radio[name=category]').on('change', function() {
				$(".subCategoryList").hide();
				$("#category-" + $(this).val()).show();
				event['category'] = $(this).val();
			});

			self.initLocationAutocomplete();

			self.initMap();
		},

		saveEvent: function() {
			//event.preventDefault();
			
			$.ajax({
				type: 'POST',
				url: '/api/event/create',
				data: JSON.stringify({event:event, guest:guest}),
				success: this.saveEventCallback,
				contentType: "application/json",
				dataType: 'json'
			});
		},

		validateEvent: function(event, guest) {
			if ($('#name').val().length == 0) {
				$('#error-message').html('Guest name is required');
				$('#error-message').show();
			}
		},
		
		saveEventCallback: function(response) {
			console.log(response);
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
			var autocomplete = new google.maps.places.Autocomplete($("#location-title-field")[0], {});

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
				
				event['locationAddress'] = place.name;
				event['gpsLat'] = place.geometry.location.lat();
				event['gpsLong'] = place.geometry.location.lng();
				
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