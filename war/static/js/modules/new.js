var EventNewModule = (function() {

	var locationMarker = null;
	var event = {//default values
		status: 'ACTIVE',
		visibility: 'PRIVATE',
		sharability: 'NORMAL'
	};
	var guest = {};
	var shareUrl = '';

	return {
		init: function() {
			var self = this;
			
			$('.mob-btn, header-logo a, .header-title a').on('touchstart', function(e) {
				$(this).addClass('hover');
			}).on('touchmove', function(e) {
				$(this).removeClass('hover');
			}).mouseenter( function(e) {
				$(this).addClass('hover');
			}).mouseleave( function(e) {
				$(this).removeClass('hover');
			}).click( function(e) {
				$(this).removeClass('hover');
			});

			$('#upload-photos-btn').on('click', function() {
				alert('upload photos...');
			});

			$('#save-btn').on('click', function() {
				guest['name'] = $('#guest-name-field').val();
				
				event['title'] = $('#title-field').val();
				event['description'] = $('#description-field').val();
				event['offer'] = $('#offer-field').val();

				//"yyyy-MM-dd'T'HH:mm:ss"
				if($('#date-from-field').val().length != 0) {
					var startTime = $('#time-to-field').val();
					event['startTime'] = $('#date-from-field').val() + 'T' + (startTime!=''?(startTime + ':00'):'00:00:00');
					console.log(event['startTime']);
				}

				if($('#date-to-field').val().length != 0 && $('#time-to-field').val().length != 0) {
					event['endTime'] = $('#date-to-field').val() + 'T' + $('#time-to-field').val() + ':00';
				}
				self.saveEvent();
			});

			var datepickerOptions = {
				format: 'yyyy-mm-dd',
				formatSubmit: 'yyyy-mm-dd',
				onClose: function() {
					$('#date-from-field, #date-to-field').blur();
				}
			}
			var timepickerOptions = {
				format: 'HH:i',
				formatSubmit: 'HH:i',
				onClose: function() {
					$('#time-from-field, #time-to-field').blur();
				}
			}

			var $dateFromPicker = $('#date-from-field').pickadate(datepickerOptions);
			var $dateToPicker = $('#date-to-field').pickadate(datepickerOptions);
			var $timeFromPicker = $('#time-from-field').pickatime(timepickerOptions);
			var $timeToPicker = $('#time-to-field').pickatime(timepickerOptions);

			$('#date-from-button').on('click', function(e) {
				$dateFromPicker.pickadate('picker').open();
				e.stopPropagation();
			});

			$('#date-to-button').on('click', function(e) {
				$dateToPicker.pickadate('picker').open();
				e.stopPropagation();
			});

			$('#time-from-button').on('click', function(e) {
				$timeFromPicker.pickatime('picker').open();
				e.stopPropagation();
			});

			$('#time-to-button').on('click', function(e) {
				$timeToPicker.pickatime('picker').open();
				e.stopPropagation();
			});

			$('#categories button').on('click', function() {
				var isActive = $(this).hasClass('selected');
				$('#categories button').each(function (i) {
					$(this).removeClass('selected');
				});

				if(isActive == false) {
					$(this).addClass('selected');
				}

				$(".subcategories").hide();
				$("#category-" + $(this).attr('id')).show();
				event['category'] = $(this).attr('id');
			});

			$('.subcategories button').on('click', function() {
				var isActive = $(this).hasClass('selected');
				$('.subcategories button').each(function (i) {
					$(this).removeClass('selected');
				});

				if(isActive == false) {
					$(this).addClass('selected');
				}

				event['subCategory'] = $(this).attr('id');
			});
			
			$('.visibility button').on('click', function() {
				var isActive = $(this).hasClass('selected');
				$('.visibility button').each(function (i) {
					$(this).removeClass('selected');
				});

				if(isActive == false) {
					$(this).addClass('selected');
				}

				event.visibility = $(this).attr('id').split('-')[1];
			});

			$('.sharing button').on('click', function() {
				var isActive = $(this).hasClass('selected');
				$('.sharing button').each(function (i) {
					$(this).removeClass('selected');
				});

				if(isActive == false) {
					$(this).addClass('selected');
				}

				event.sharability = $(this).attr('id').split('-')[1];
			});

			$('#share-link-btn').on('click', function() {
				window.prompt("Copy to clipboard: Ctrl+C", shareUrl);
			});

			$('#share-by-email-btn').on('click', function() {
				self.shareByEmail();
			});

			$('#share-on-facebook-btn').on('click', function() {
				self.shareOnFacebook();
			});

			$('#share-on-google-btn').on('click', function() {
				self.shareOnGoogle();
			});

			self.loadScript();
		},

		saveEvent: function() {
			var self = this;
			if(self.isEventValid() == false) {
				return;
			}

			$.cookie('name', guest['name'], {path: '/'});
			self.showSpinner();

			$.ajax({
				type: 'POST',
				url: '/api/event/create',
				data: JSON.stringify({event:event, guest:guest}),
				contentType: "application/json",
				dataType: 'json'
			}).done(function(data) {
				shareUrl = baseUrl + '/i/' + data.event.eventId + "." + data.guest.guestId; 
				$('#error-alert').hide();
				$('#success-text').text('New event created successfully');
				$('#success-alert').show();
				if(data.event.sharability != 'NOSHARE' && data.event.visibility == 'PUBLIC') {
					$('.share').show();
					var $target = $('html,body'); 
					$target.animate({scrollTop: $target.height()});
				}
				
				$('#save-btn').hide();
				$('#cancel-btn').html('Go back');
				self.hideSpinner();
			}).fail(function(jqXHR, textStatus, errorThrown) {
				var json = $.parseJSON(jqXHR.responseText);
				if(json.message){
					$('#error-text').text(json.message);
				} else {
					$('#error-text').text('Unknown server error. Please try again.');
				}
				$('#error-alert').show();
				self.hideSpinner();
			});
		},

		shareByEmail: function() {
			window.location.href = 'mailto:?subject=Invitation&body=' + encodeURIComponent(shareUrl);
		},

		shareOnGoogle: function() {
			var url = 'https://plus.google.com/share?url=' + encodeURIComponent(shareUrl);
			window.open(url, 'Share', ',personalbar=0,toolbar=0,scrollbars=1,resizable=1');
		},

		shareOnFacebook: function() {
			var url = 'https://www.facebook.com/sharer/sharer.php?u=' + encodeURIComponent(shareUrl);
			window.open(url, 'Share', 'personalbar=0,toolbar=0,scrollbars=1,resizable=1');
		},

		isEventValid: function() {
			var error = '';
			if(!event.locationAddress) {
				error = 'Location is required';
			} else if (!event.startTime) {
				error = 'Start date is required';
			}

			if(error != '') {
				$('#error-text').text(error);
				$('#error-alert').show();
				return false;
			} else {
				return true;
			}
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

		loadScript: function() {
			var script = document.createElement('script');
			script.type = 'text/javascript';
			script.src = 'https://maps.googleapis.com/maps/api/js?sensor=true' +
				'&libraries=places,visualization&language=en-US&v=3.2&callback=EventNewModule.initMapAndAutocomplete';
			document.body.appendChild(script);
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
				disableDoubleClickZoom: true,
				panControl: false,
				zoomControl: true,
				zoomControlOptions: {
					position: google.maps.ControlPosition.LEFT_CENTER
				},
				scaleControl: false,
				draggable: false,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			}
			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
		},

		initMapAndAutocomplete: function(){
			this.initLocationAutocomplete();
			this.initMap();
		},

		showSpinner: function() {
			$('body').append('<div class="overlay"><div class="spinner"><i class="fa fa-spinner fa-spin"></i></div></div>');
		},

		hideSpinner: function() {
			$('.overlay').remove();
		}
	}
}());