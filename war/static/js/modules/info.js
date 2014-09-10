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

			$('#send-by-email-btn').on('click', function() {
				window.location.href = "mailto:" + $('#share-by-email-field').val() + '?subject=Invitation&body=' + window.location.href;
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

			$('#attend-btn').on('click', function() {console.log(eventJson)
				self.attendEvent(eventJson.eventId);
			});

			$('#decline-btn').on('click', function() {
				self.declineEvent();
			});

			$('#maybe-attend-btn').on('click', function() {
				self.maybeAttendEvent();
			});

			$('#scrollto-comments-btn').on('click', function() {
				$('body,html').stop().animate({
					scrollTop: $('.comments').position().top
				});
			});

			$('#scrollto-guests-btn').on('click', function() {
				$('body,html').stop().animate({
					scrollTop: $('.guests').position().top
				});
			});

			$.views.helpers({
				formatTime: function (val) {
					if(val) {
						var str = '';
						var duration = moment.duration(moment().diff(moment(val)));

						if(duration.days() > 0) {
							str = str + Math.floor(duration.days()) + " days ago";
						} else if(duration.hours() > 0) {
							str = str + Math.floor(duration.hours()) + " hours ago";
						} else if(duration.minutes() > 0) {
							str = str + Math.floor(duration.minutes()) + " min ago";
						} else if(duration.seconds() > 0) {
							str = str + Math.floor(duration.seconds()) + " s ago";
						} else {
							str = 'moment ago';
						}
						return str;
						//moment.duration(-moment().diff(moment(val), 'seconds'), 'seconds').humanize(true);
					} else {
						return '';
					}
				}
			});

			self.loadScript();
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
			var latlng = new google.maps.LatLng(eventJson.gpsLat, eventJson.gpsLong);
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
			};
			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

			var marker = new google.maps.Marker({
				position: latlng,
				map: map
			});
		},

		renderComments: function() {
			$('.comments .list-group').html($('#commentTemplate').render(commentsJson));
		},

		//ajax requests
		attendEvent: function(eventId) {
			var guestName = $('#guest-name-field').val();
			if (guestName.length == 0) {
				alert('Guest name is required');
			} else {
				var self = this;
				self.showSpinner();

				$.ajax({
					type: 'POST',
					url: '/api/event/' + eventId + '/attend/' + guestName,
					contentType: "application/json",
					dataType: 'json'
				}).done(function() {
					self.hideSpinner();
					//TODO
				}).fail(function(jqXHR, textStatus, errorThrown) {
					self.hideSpinner();
					//TODO
				});
			}
		},

		declineEvent: function() {
			if ($('#guest-name-field').val().length == 0) {
				alert('Guest name is required');
			} else {
				//
				self.showSpinner();
			}
		},

		maybeAttendEvent: function() {
			if ($('#guest-name-field').val().length == 0) {
				alert('Guest name is required');
			} else {
				//
				self.showSpinner();
			}
		},

		addComment: function(eventId, guestId, comment) {
			if(comment == '') {//TODO: validation
				return;
			}
			var self = this;
			self.showSpinner();

			$.ajax({
				type: 'POST',
				url: '/api/event/' + eventId + '/' + guestId + '/comment/create',
				data: JSON.stringify({eventId: eventId, guestId: guestId, comment: comment}),
				contentType: "application/json",
				dataType: 'json'
			}).done(function(comment) {
				comment.guestName = guestJson.name;
				commentsJson.unshift(comment);
				self.renderComments();
				self.hideSpinner();
				//TODO
			}).fail(function(jqXHR, textStatus, errorThrown) {
				var json = $.parseJSON(jqXHR.responseText);
				if(json.message){
					$('#error-text').text(json.message);
				} else {
					$('#error-text').text('Unknown server error. Please try again.');
				}
				self.hideSpinner();
				$('#error-alert').show();
			});
		},

		showSpinner: function() {
			$('body').append('<div class="overlay"><div class="spinner"><i class="fa fa-spinner fa-spin"></i></div></div>');
		},

		hideSpinner: function() {
			$('.overlay').remove();
		}
	}

}());