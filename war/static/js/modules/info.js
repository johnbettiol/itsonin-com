var EventInfoModule = (function() {
	var scrolledToComments = false;

	return {
		init: function() {
			var self = this;

			$('.location a, header-logo a, .header-title a, #back-link').on('touchstart', function(e) {
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

			$(window).on('resize', function(){
				$('.event-container').css('margin-top', $('.header-container').height());
			}).trigger('resize');

			$('#share-link-btn').on('click', function() {
				//self.shareLink();
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

			$('#add-comment-btn').on('click', function() {
				self.addComment(eventJson.eventId, guestJson.guestId, $('#comment-field').val());
				$('#comment-field').val('');
			});

			$('#attend-btn').on('click', function() {console.log(eventJson)
				self.attendEvent(eventJson.eventId);
			});

			$('#decline-btn').on('click', function() {console.log(eventJson)
				self.declineEvent(eventJson.eventId);
			});

			$('#maybe-attend-btn').on('click', function() {
				self.maybeAttendEvent(eventJson.eventId);
			});

			$('#scrollto-comments-btn').on('click', function() {
				var position = (scrolledToComments == false) ? $('.comments').position().top : 0;
				$('body,html').stop().animate({
					scrollTop: position
				});
				scrolledToComments = !scrolledToComments;
			});

			$('#back-link').on('click', function(event) {
				event.preventDefault();
				if(Path.routes.previous) {
					history.back();
				} else {
					location.href = '/en/Düsseldorf/Events';
				}
			});

			$('#city-link').on('click', function(event) {
				event.preventDefault();
				if(Path.routes.previous) {
					history.back();
				} else {
					location.href = '/en/Düsseldorf/Events';
				}
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
				},
				isYou: function(guestId) {
					return guestId == guestJson.guestId;
				}
			});

			self.loadScript();
			self.initNavigationLink();
		},

		initNavigationLink: function () {
			var url;
			if(navigator.userAgent.match(/iPhone|iPod/)){
                url = 'comgooglemaps://?center=' + eventJson.gpsLat + ',' + eventJson.gpsLong + '&zoom=14';
			} else if (navigator.userAgent.match(/Android/)) {
				url = 'geo://0,0?q=' + eventJson.locationAddress; 
				/*'geo://' + eventJson.gpsLat + ',' + eventJson.gpsLong + '?z=8';*/
			} else {
				url = 'http://maps.google.com/maps?q=' + eventJson.gpsLat + ',' + eventJson.gpsLong;
			}
            $("#open-navigation-link").attr("href", url);
		},

		shareLink: function() {
			$('#share-link-modal').modal('show');
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

		loadScript: function() {
			if (typeof google === 'object' && typeof google.maps === 'object') {
				this.initMap();
				return;
			}
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
					zoom: 20,
					center: latlng,
					disableDefaultUI: true,
					disableDoubleClickZoom: true,
					panControl: false,
					zoomControl: true,
					zoomControlOptions: {
						position: google.maps.ControlPosition.LEFT_CENTER
					},
					scaleControl: false,
					draggable: false,
					scrollwheel: false,
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

		renderGuests: function() {
			$('.guests .list-group').html($('#guestTemplate').render(guestsJson));
		},

		updateGuests: function(guest) {
			var exist = false;
			$.each(guestsJson, function(index, g) {
				if(g.guestId == guest.guestId){
					guestsJson[index] = guest;
					exist = true;
				}
			});
			if(exist == false) {
				guestsJson.push(guest);
			}
			this.renderGuests();
		},

		//ajax requests
		attendEvent: function(eventId) {
			var guestName = $('#guest-name-field').val();
			if (guestName.length == 0) {
				alert('Guest name is required');
			} else {
				var self = this;
				self.showSpinner();
				$.cookie('name', guestName, {path: '/'});

				$.ajax({
					type: 'POST',
					url: '/api/event/' + eventId + '/attend/' + guestName,
					contentType: "application/json",
					dataType: 'json'
				}).done(function(guest) {
					$('#maybe-attend-btn').removeClass('btn-primary').addClass('btn-default');
					$('#decline-btn').removeClass('btn-primary').addClass('btn-default');
					$('#attend-btn').removeClass('btn-default').addClass('btn-primary');
					$('#guests-counter').text(guestsJson.length);
					//guest['name'] = guestName;
					guestJson = guest;
					self.updateGuests(guest);
					if(eventJson.sharability == 'PYRAMID') {
						$('#pyramid-alert').show();
					}
					$('.share').show();
					$('.comments .panel-footer').show();
					self.hideSpinner();
					//TODO
				}).fail(function(jqXHR, textStatus, errorThrown) {
					self.hideSpinner();
					//TODO
				});
			}
		},

		declineEvent: function(eventId) {
			var guestName = $('#guest-name-field').val();
			if (guestName.length == 0) {
				alert('Guest name is required');
			} else {
				var self = this;
				self.showSpinner();
				$.cookie('name', guestName, {path: '/'});

				$.ajax({
					type: 'POST',
					url: '/api/event/' + eventId + '/decline',
					contentType: "application/json",
					dataType: 'json'
				}).done(function(guest) {
					$('#maybe-attend-btn').removeClass('btn-primary').addClass('btn-default');
					$('#attend-btn').removeClass('btn-primary').addClass('btn-default');
					$('#decline-btn').removeClass('btn-default').addClass('btn-primary');
					$('#guests-counter').text(guestsJson.length);
					//guest['name'] = guestName;
					guestJson = guest;
					self.updateGuests(guest);
					$('.share').show();
					$('.comments .panel-footer').show();
					self.hideSpinner();
					//TODO
				}).fail(function(jqXHR, textStatus, errorThrown) {
					self.hideSpinner();
					//TODO
				});
			}
		},

		maybeAttendEvent: function(eventId) {
			var guestName = $('#guest-name-field').val();
			if (guestName.length == 0) {
				alert('Guest name is required');
			} else {
				var self = this;
				self.showSpinner();
				$.cookie('name', guestName, {path: '/'});

				$.ajax({
					type: 'POST',
					url: '/api/event/' + eventId + '/maybeattend/' + guestName,
					contentType: "application/json",
					dataType: 'json'
				}).done(function(guest) {
					$('#attend-btn').removeClass('btn-primary').addClass('btn-default');
					$('#decline-btn').removeClass('btn-primary').addClass('btn-default');
					$('#maybe-attend-btn').removeClass('btn-default').addClass('btn-primary');
					$('#guests-counter').text(guestsJson.length);
					//guest['name'] = guestName;
					guestJson = guest;
					self.updateGuests(guest);
					$('.share').show();
					$('.comments .panel-footer').show();
					self.hideSpinner();
					//TODO
				}).fail(function(jqXHR, textStatus, errorThrown) {
					self.hideSpinner();
					//TODO
				});
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
				$('#comments-counter').text(commentsJson.length);
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