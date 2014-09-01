var EventInfoModule = (function() {

	return {
		init: function() {
			var self = this;

			$('#share-link-btn').on('click', function() {
				self.shareLink();
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

			$('#add-comment-btn').on('click', function() {
				self.addComment(1, 1, $('#comment-field').val());
			});

			$('#attend-btn').on('click', function() {
				self.attendEvent();
			});

			$('#decline-btn').on('click', function() {
				self.declineEvent();
			});

			$('#maybe-attend-btn').on('click', function() {
				self.maybeAttendEvent();
			});

			self.loadScript();
		},

		attendEvent: function(eventId, guestName) {
			if ($('#guest-name-field').val().length == 0) {
				$('#error-message').html('Guest name is required');
				$('#error-message').show();
			} else {
				$.ajax({
					type: 'GET',
					url: '/api/event/' + eventId + '/attend/' + guestName,
					data: JSON.stringify({eventId: eventId, guestId: guestId, comment: comment}),
					contentType: "application/json",
					dataType: 'json'
				}).done(function() {
					//TODO
				}).fail(function(jqXHR, textStatus, errorThrown) {
					//TODO
				});
			}
		},

		declineEvent: function() {
			if ($('#guest-name-field').val().length == 0) {
				$('#error-message').html('Guest name is required');
				$('#error-message').show();
			} else {
				//
			}
		},

		maybeAttendEvent: function() {
			if ($('#guest-name-field').val().length == 0) {
				$('#error-message').html('Guest name is required');
				$('#error-message').show();
			} else {
				//
			}
		},

		shareLink: function() {
			$('#share-link-modal').modal('show');
		},

		shareByEmail: function() {
			$('#share-by-email-modal').modal('show');
		},

		shareOnGoogle: function() {
			var url = 'https://plus.google.com/share?url=' + window.location.href;
			window.open(url, 'Share', ',personalbar=0,toolbar=0,scrollbars=1,resizable=1');
		},

		shareOnFacebook: function() {
			var url = 'https://www.facebook.com/sharer/sharer.php?u=' + window.location.href;
			window.open(url, 'Share', 'personalbar=0,toolbar=0,scrollbars=1,resizable=1');
		},

		loadScript: function() {
			var script = document.createElement('script');
			script.type = 'text/javascript';
			script.src = 'https://maps.googleapis.com/maps/api/js?sensor=true' +
				'&libraries=places,visualization&language=en-US&v=3.2&callback=EventInfoModule.initMap';
			document.body.appendChild(script);
		},

		initMap: function(){
			var self = this;
			var latlng = new google.maps.LatLng(event.gpsLat, event.gpsLong);
			var mapOptions = {
					zoom: 17,
					center: latlng,
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

			var marker = new google.maps.Marker({
				position: latlng,
				map: map
			});
		},

		addComment: function(eventId, guestId, comment) {
			if(comment == '') {//TODO: validation
				return;
			}

			$.ajax({
				type: 'POST',
				url: '/api/event/' + eventId + '/' + guestId + '/comment/create',
				data: JSON.stringify({eventId: eventId, guestId: guestId, comment: comment}),
				contentType: "application/json",
				dataType: 'json'
			}).done(function() {
				//TODO
			}).fail(function(jqXHR, textStatus, errorThrown) {
				var json = $.parseJSON(jqXHR.responseText);
				if(json.message){
					$('#error-text').text(json.message);
				} else {
					$('#error-text').text('Unknown server error. Please try again.');
				}
				$('#error-alert').show();
			});
		}
	}

}());