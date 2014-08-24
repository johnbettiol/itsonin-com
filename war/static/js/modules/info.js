var EventInfoModule = (function() {
	
	return {
	    init: function() {
	    	var self = this;

	        $('#share-link').on('click', function() {
	        	self.shareLink();
	        });

	        $('#share-by-email').on('click', function() {
	        	self.shareByEmail();
	        });

	        $('#share-on-facebook').on('click', function() {
	        	self.shareOnFacebook();
	        });

	        $('#share-on-google').on('click', function() {
	        	self.shareOnGoogle();
	        });

	        self.initMap();
	    },
	    
	    attendEvent: function() {
	    	if ($('#name').val().length == 0) {
	    		$('#error-message').html('Guest name is required');
	    		$('#error-message').show();
	    	} else {
	    		$('#attend-event-form').submit();
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
				  }
			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
			
			var marker = new google.maps.Marker({
				position: latlng,
				map: map
				//title: 'Hello World!'
			});
		}
	}

}());