angular.module('itsonin', ['ngRoute', 'ngSanitize', 'ngCookies'])
.config(['$routeProvider', 'views', '$locationProvider', function($routeProvider, views, $locationProvider) {
  $routeProvider
  	  .when('/', {templateUrl: views.list, controller: 'ListController'})
      .when('/e/add', {templateUrl: views.editEvent, controller: 'EditEventController'})
      .when('/e/:eventId', {templateUrl: views.viewEvent, controller: 'ViewEventController'})
      .when('/e/:eventId/edit', {templateUrl: views.editEvent, controller: 'EditEventController'})
      .when('/e/:eventId/attend', {templateUrl: views.attendEvent, controller: 'AttendEventController'})
      .when('/i/:eventId.:hostId', {templateUrl: views.invitation, controller: 'InvitationController'})
      .when('/i/:invitationId/attend', {templateUrl: views.attendInvitation, controller: 'AttendInvitationController'})
      .when('/i/:invitationId/decline', {templateUrl: views.declineInvitation, controller: 'DeclineInvitationController'})
      .when('/about', {templateUrl: views.about, controller: 'AboutController'})
      .when('/welcome', {templateUrl: views.welcome, controller: 'WelcomeController'})
      .when('/me', {templateUrl: views.me, controller: 'MeController'})
      .when('/:location', {templateUrl: views.list, controller: 'ListController'})
      .otherwise({redirectTo: '/:location'});
  
  	  $locationProvider.html5Mode(true);
}])

.config(['$httpProvider', function($httpProvider) {
	$httpProvider.interceptors.push('interceptor'); 
}])

.run(['$rootScope', '$location', '$cookies',
    function($rootScope, $location, $cookies) {

	$rootScope.location = 'Düsseldorf'; //TODO: get location

	$rootScope.$on("$routeChangeStart", function (event, next, current) {
		if($location.path() == '/' || $location.path() == '' || $location.path() == '/Duesseldorf'
			|| $location.path() == '/Dusseldorf'){
			$location.path('/' + $rootScope.location);
		}
	});

	if (!$cookies.token){
		$location.path('/welcome');
	} else if($location.path() == '/' || $location.path() == '' || $location.path() == '/Duesseldorf'
		|| $location.path() == '/Dusseldorf'){
		$location.path('/' + $rootScope.location);
	}
		
}]);angular.module('itsonin').constant('constants', {
	EVENT_TYPES: [
        {id: 'VACATION', img: 'picture', background: ''},
        {id: 'BIRTHDAY', img: 'gift', background: ''},
        {id: 'CLUBBING', img: 'glass', background: ''},
        {id: 'PICNIC', img: 'tree-conifer', background: ''},
        {id: 'DINNER', img: 'cutlery', background: ''},
        {id: 'PARTY', img: 'glass', background: ''},
        {id: 'RALLY', img: 'road', background: ''},
        {id: 'EXCERCISE', img: 'book', background: ''},
        {id: 'FLASHMOB', img: 'bullhorn', background: ''},
        {id: 'PROTEST', img: 'fire', background: ''}
    ],
    EVENT_SHARABILITIES: [
        {id: 'NOSHARE', img: 'picture'},
        {id: 'NORMAL', img: 'gift'},
        {id: 'PYRAMID', img: 'cutlery'}
    ],
    EVENT_VISIBILITIES: [
        {id: 'PUBLIC', img: 'gift'},
    	{id: 'PRIVATE', img: 'picture'}
    ],
    DATE_TYPES: [
         {id: 'EMPTY'}, 
         {id: 'NOW'}, 
         {id: 'CUSTOM'}
    ],
    LOCATION_TYPES: [
         {id: 'EMPTY'}, 
         {id: 'MAP'}
    ]
});angular.module('itsonin').controller('AboutController',
  ['$scope', function ($scope) {
	  
}]);
angular.module('itsonin').controller('AttendEventController',
	['$scope', '$routeParams', 'eventService', 'shareService',
	 function ($scope, $routeParams, eventService, shareService) {

		$scope.loadEvent = function () {
			eventService.info($routeParams.eventId, function(response) {
				$scope.event = response.event;
			},
			function(error) {
			});
		}

		$scope.loadEvent();

	    $scope.shareLink = function () {
	        shareService.shareLink($routeParams.eventId, $routeParams.hostId);
	    }
	    
	    $scope.shareByEmail = function () {
	        shareService.shareByEmail($routeParams.eventId, $routeParams.hostId);
	    }
}]);

angular.module('itsonin').controller('AttendInvitationController',
	['$scope', '$routeParams', 'eventService', 'shareService',
	 function ($scope, $routeParams, eventService, shareService) {

		$scope.eventId = $routeParams.invitationId.split('.')[0];
		$scope.parentGuestId = $routeParams.invitationId.split('.')[1];
		
		$scope.loadEvent = function () {
			eventService.info($scope.eventId, function(response) {
				$scope.event = response.event;
			},
			function(error) {
			});
		}

		$scope.loadEvent();

        $scope.shareLink = function () {
            shareService.shareLink($routeParams.eventId, $routeParams.hostId);
        }
        
        $scope.shareByEmail = function () {
            shareService.shareByEmail($routeParams.eventId, $routeParams.hostId);
        }
}]);
angular.module('itsonin').controller('DeclineInvitationController',
	['$scope', '$routeParams', 'eventService', function ($scope, $routeParams, eventService) {

		$scope.eventId = $routeParams.invitationId.split('.')[0];
		$scope.parentGuestId = $routeParams.invitationId.split('.')[1];
		
		$scope.loadEvent = function () {
			eventService.info($scope.eventId, function(response) {
				$scope.event = response.event;
			},
			function(error) {
			});
		}

		$scope.loadEvent();

}]);
angular.module('itsonin').controller('EditEventController',
  ['$scope', '$rootScope', '$routeParams', '$modal', '$q', '$location', 'views', 'eventService', 'constants', 
   function ($scope, $rootScope, $routeParams, $modal, $q, $location, views, eventService, constants) {
	  
	  $scope.readyToShow = false;
	  $scope.eventTypes = constants.EVENT_TYPES;
	  
	  $scope.dateType = 'EMPTY';
	  $scope.locationType = 'EMPTY';
	  $scope.event = {
			  status: 'ACTIVE',
			  sharability: 'NORMAL',
			  visibility: 'PRIVATE',
			  flexibility: 'FIXED'
	  };
	  $scope.guest = {};
	  
	  $scope.$watch('event.startTime + event.endTime', function(newValue, oldValue) {
	      if($scope.event.startTime instanceof Date && $scope.event.endTime instanceof Date){
	          if($scope.event.startTime > $scope.event.endTime){
	              //var endTime = new Date($scope.event.startTime);
	              //endTime.setHours($scope.event.startTime.getHours() + 1);
	              $scope.event.endTime = moment($scope.event.startTime).clone().add('hours', 1).toDate();
    	      }
	      }
	  });
	  
	  $rootScope.$on("event:setLocation", function (event, place) {
			$scope.event['locationAddress'] = place.name;
			$scope.event['gpsLat'] = place.geometry.location.lat();
			$scope.event['gpsLong'] = place.geometry.location.lng();
	  });

	  $scope.loadEvent = function () {
		  if($routeParams.eventId){
			  eventService.info($routeParams.eventId, function(response) {
				  $scope.event = response.event;
				  $scope.sharabilityImg = $scope.getImgById(constants.EVENT_SHARABILITIES,
						  $scope.event.sharability);
				  $scope.visibilityImg = $scope.getImgById(constants.EVENT_VISIBILITIES,
						  $scope.event.visibility);
				  
				  $scope.dateType = 'CUSTOM';
				  $scope.locationType = 'MAP';
				  $scope.readyToShow = true;
			  },
			  function(error) {
	
			  });
		  } else {
			  $scope.readyToShow = true;
		  }
	  }
	  $scope.loadEvent();
	  
	  $scope.selectEventType = function(type) {
		  $scope.event.type = type;
	  }
	  
	  $scope.nextDateType = function() {
		  var dateType = $scope.cycle(constants.DATE_TYPES, $scope.dateType);
		  $scope.dateType = dateType.id;
		  if($scope.dateType == 'NOW'){
		      $scope.event.startTime = new Date();
		  } else if ($scope.dateType == 'CUSTOM') {
              var startTime = new Date();
              startTime.setHours(startTime.getHours() + 1);
              startTime.setMinutes(0);
              $scope.event.startTime = startTime;
		  }
	  }
	  
	  $scope.nextLocationType = function() {
		  var locationType = $scope.cycle(constants.LOCATION_TYPES, $scope.locationType);
		  $scope.locationType = locationType.id;
	  }
	  
	  $scope.nextSharability = function() {
		  var sharability = $scope.cycle(constants.EVENT_SHARABILITIES, $scope.event.sharability);
		  $scope.event.sharability = sharability.id;
		  $scope.sharabilityImg = sharability.img;
	  }
	  
	  $scope.nextVisibility = function() {
		  var visibility = $scope.cycle(constants.EVENT_VISIBILITIES, $scope.event.visibility);
		  $scope.event.visibility = visibility.id;
		  $scope.visibilityImg = visibility.img;
	  }
	  
	  $scope.showMap = function() {
		  var modalPromise = $modal({
			  template: views.map,
			  persist: false,
			  show: false,
			  keyboard: true,
			  data: {}
		  });

		  $q.when(modalPromise).then(function(modalEl) {
			  modalEl.modal('show');
		  });
	  }

	  $scope.cycle = function(array, currentValue) {
		  var currentIndex = 0;
		  for(var i=0; i < array.length; i++){
			  if(currentValue == array[i].id){
				  currentIndex = i;
				  break;
			  }
		  }
		  return array[(currentIndex+1) >= array.length ? 0 : (currentIndex+1)];
	  }
	  
	  $scope.getImgById = function(array, id) {
		  for(var i=0; i < array.length; i++){
			  if(id == array[i].id){
				  return array[i].img;
			  }
		  }
	  }
	  
	  $scope.shareEvent = function () {
		  eventService.create($scope.event, $scope.guest, function(resp) {
			  $location.path('/i/' + resp.event.eventId + '.' + resp.guest.guestId);
		  },
		  function(error) {
			  if(error.status == 'error') {
				  $scope.error = error.message;
			  }else{
				  $scope.error = 'Unknown server error. Please try again.';
			  }
		  });
	  }
	  
	  $scope.updateEvent = function () {
		  eventService.update($scope.event, function(response) {
			  $location.path('/' + $rootScope.location);
		  });
	  }
	  
	  $scope.sharabilityImg = $scope.getImgById(constants.EVENT_SHARABILITIES,
			  $scope.event.sharability);
	  $scope.visibilityImg = $scope.getImgById(constants.EVENT_VISIBILITIES,
			  $scope.event.visibility);
}]);
angular.module('itsonin').controller('InvitationController',
  ['$scope', '$routeParams', '$location', 'eventService', 'shareService',
   function ($scope, $routeParams, $location, eventService, shareService) {
	  
	$scope.hostId = $routeParams.hostId;
			  
	$scope.loadEvent = function () {
		eventService.info($routeParams.eventId, function(response) {
			$scope.event = response.event;
			$scope.guest = response.guest;
			$scope.readyToShow = true;
		},
		function(error) {
			console.log(error);
		});
	}
				
	$scope.loadEvent();
	
	$scope.attendEvent = function () {
		if(!$scope.guest.name){
			$scope.error = 'Guest name is required';
			return;
		}
		
		eventService.attend($routeParams.eventId, $scope.guest.name, function(response) {
			$location.path('/i/' + $routeParams.eventId + '.' + response.guestId);
		},
		function(error) {
						
		});
	}
	
	$scope.declineEvent = function () {
		eventService.decline($routeParams.eventId, function(response) {
			$scope.guest.status = 'DECLINED';
			$scope.success = response.message;
		},
		function(error) {
						
		});
	}
	
    $scope.shareLink = function () {
        shareService.shareLink($routeParams.eventId, $routeParams.hostId);
    }
    
    $scope.shareByEmail = function () {
        shareService.shareByEmail($routeParams.eventId, $routeParams.hostId);
    }
    
    $scope.getGooglePlusUrl = function() {
    	var url = 'https://plus.google.com/share?url=' + $location.host() + $location.path();
    	window.open(url, 'Share', 'width=400,height=400,personalbar=0,toolbar=0,scrollbars=1,resizable=1');
    }
    
    $scope.getFacebookUrl = function() {
    	var url = 'https://www.facebook.com/sharer/sharer.php?u=' + $location.host() + $location.path();
    	window.open(url, 'Share', 'personalbar=0,toolbar=0,scrollbars=1,resizable=1');
    }
		  
}]);
angular.module('itsonin').controller('ListController',
  ['$scope', '$rootScope', '$routeParams', 'eventService', 'constants', 
   function ($scope, $rootScope, $routeParams, eventService, constants) {
	  
	  $scope.dateFilterState = 0;
	  $scope.dateFilterText = ["All dates", "Custom", "Now", "Tomorrow"];
	  $scope.allEvents = true;
	  $scope.allPlaces = true;
	  $scope.allCategories = true;
	  $scope.eventTypes = constants.EVENT_TYPES;
	  $scope.locationExist = ($routeParams.location == $rootScope.location);
	  $scope.filter = {
		types: []
	  };
	  
	  $scope.isKnownLocation = function () {
	      var knownLocations = ['Düsseldorf', 'Duesseldorf', 'Dusseldorf'];
	      for(var i=0; i<knownLocations.length; i++){
	          if($routeParams.location == knownLocations[i]){
	              return true;
	          }
	      }
	      return false;
	  }

	  $scope.$watch('filter.startTime', function(newValue, oldValue) {
		  if(newValue && newValue instanceof Date && $scope.dateFilterState == 1){
			  $scope.loadEvents();
		  }
	  });
	  
	  $scope.loadEvents = function () {
		$scope.events = [];
	    var params = {
			allEvents: ($scope.allEvents == true)?true:false,
			types: $scope.filter.types
	    }
	    
	    console.log($scope.filter.startTime);
	    
	    if($scope.filter.startTime){
	    	params.startTime = moment($scope.filter.startTime).format('YYYY-MM-DD HH:mm');
	    }
	    
	    if($scope.filter.endTime){
	    	params.endTime = moment($scope.filter.endTime).format('YYYY-MM-DD HH:mm');
	    }
	    
		eventService.list(params, function(response) {
			  $scope.events = response;
		},
		function(error) {
			  
		});
	  }
	  
	  $scope.loadEvents();
	  
	  $scope.getEventImage = function(eventType) {
		  return $scope.eventImages[eventType];
	  }

	  $scope.getEventImages = function() {
		  var result = {};
		  angular.forEach(constants.EVENT_TYPES, function(eventType){
			  result[eventType.id] = eventType.img;
		  });
		  return result;
	  }
	  $scope.eventImages = $scope.getEventImages();
	  
	  $scope.toggleEventsFilter = function() {
          $scope.allEvents = !$scope.allEvents;
       	  $scope.loadEvents();
	  }
	  
	  $scope.toggleDateFilter = function(){
		  $scope.dateFilterState = ($scope.dateFilterState + 1) % $scope.dateFilterText.length;
		  
		  switch($scope.dateFilterState) {
			  case 0: {//all
				  delete $scope.filter.startTime;
				  delete $scope.filter.endTime;
				  $scope.loadEvents();
				  break;
			  } case 2: {//now
				  $scope.filter.startTime = new Date();
				  $scope.filter.endTime = new Date();
				  $scope.loadEvents();
				  break; 
			  } case 3: {//tomorrow
				  $scope.filter.startTime = moment().add('days', 1)
				  	.set('hour', 0).set('minute', 0).set('second', 0);
				  $scope.filter.endTime = moment().add('days', 1)
				  	.set('hour', 23).set('minute', 59).set('second', 59);
				  $scope.loadEvents();
			  }
		  }
	  }
	  
	  $scope.togglePlacesFilter = function() {
		  $scope.allPlaces = !$scope.allPlaces;
		  if($scope.allPlaces == true){
			  $scope.loadEvents();
		  }
	  }
	  
	  $scope.toggleCategoriesFilter = function() {
		  $scope.allCategories = !$scope.allCategories;
		  if($scope.allCategories == true){
			  $scope.filter.types = [];
			  $scope.loadEvents();
		  }
	  }
	  
	  $scope.toggleEventType = function(type) {
		  var exist = -1;
		  angular.forEach($scope.filter.types, function(item, index){
			  if(item == type) {
				  exist = index;
			  }
		  });
		  
		  if(exist == -1){
			  $scope.filter.types.push(type);
		  } else {
			  $scope.filter.types.splice(exist, 1);
		  }
		  
		  $scope.loadEvents();
	  }
	  
	  $scope.filterContainsType = function(type) {
		  var exist = -1;
		  angular.forEach($scope.filter.types, function(item, index){
			  if(item == type) {
				  exist = index;
			  }
		  });
		  return exist >= 0;
	  }
	  
}]);
angular.module('itsonin').controller('MeController',
  ['$scope', 'eventService', 'constants', function ($scope, eventService, constants) {
	  
	  $scope.loadEvents = function () {
		  eventService.list(function(response) {
			  $scope.events = response;
		  },
		  function(error) {

		  });
	  }

	  $scope.loadEvents();

	  $scope.getEventImage = function(eventType) {
		  return $scope.eventImages[eventType];
	  }

	  $scope.getEventImages = function() {
		  var result = {};
		  angular.forEach(constants.EVENT_TYPES, function(eventType){
			  result[eventType.id] = eventType.img;
		  });
		  return result;
	  }
	  $scope.eventImages = $scope.getEventImages();
	  
}]);
angular.module('itsonin').controller('MapController', ['$scope', '$rootScope', function ($scope, $rootScope) {
	$scope.place = {};
	  
	$scope.select = function(){
		$rootScope.$broadcast('event:setLocation', $scope.place);
		$scope.hide();
	}
	
}]);
angular.module('itsonin').controller('ShareByEmailController',
  ['$scope', function ($scope) {

      $scope.link = 'http://' + window.location.host + '/i/' + $scope.data.eventId + '.' + $scope.data.hostId;

}]);
angular.module('itsonin').controller('ShareLinkController',
  ['$scope', function ($scope) {
	  $scope.link = 'http://' + window.location.host + '/i/' + $scope.data.eventId + '.' + $scope.data.hostId;
	  //TODO: replace host
}]);
angular.module('itsonin').controller('ViewEventController',
  ['$scope', '$routeParams', '$location', 'eventService', 'guestService', 'commentService', 'shareService',
   function ($scope, $routeParams, $location, eventService, guestService, commentService, shareService) {

	$scope.readyToShow = false;
	$scope.event = {};

	$scope.loadEvent = function () {console.log($routeParams.eventId)
		eventService.info($routeParams.eventId, function(response) {
			$scope.event = response.event;
			$scope.guest = response.guest;
			$scope.guests = response.guests;
			$scope.comments = response.comments;
			$scope.viewonly = response.viewonly;
			$scope.readyToShow = true;
		},
		function(error) {
			console.log(error);	
		});
	}

	$scope.loadEvent();
    
	$scope.attendEvent = function () {
		if(!$scope.guest.name){
			$scope.error = 'Guest name is required';
			return;
		}
		
		eventService.attend($routeParams.eventId, $scope.guest.name, function(response) {
			$location.path('/i/' + $routeParams.eventId + '.' + response.guestId);
		},
		function(error) {
			if(error.status == 'error') {
				$scope.error = error.message;
			}else{
				$scope.error = 'Unknown server error. Please try again.';
			}
		});
	}
	
	$scope.declineEvent = function () {
		eventService.decline($routeParams.eventId, $scope.guest.name, function(response) {
			$scope.guest.status = 'DECLINED';
			$scope.success = response.message;
		},
		function(error) {
			console.log(error);			
		});
	}
	  
}]);
angular.module('itsonin').controller('WelcomeController',
  ['$scope', function ($scope) {
	  
}]);
angular.module('itsonin').directive('bDatepicker', function(){
	  return {
	    require: '?ngModel',
	    link: function(scope, element, attrs, controller) {
	      var updateModel = function(ev) {
	        scope.$apply(function() {
	            controller.$setViewValue(ev.date);
	            element.datepicker('hide');
	            element.blur();
	          });
	      };
	      if (controller != null) {
	        controller.$render = function() {
	          element.datepicker().data().datepicker.date = controller.$viewValue;
	          element.datepicker('setValue', controller.$viewValue);
	          element.datepicker('update');
	          return controller.$viewValue;
	        };
	      }
	      element.datepicker({autoclose: true}).on('changeDate', updateModel);
	    }
	  };
});angular.module('itsonin').directive('bDatetimepicker', function(){
	return {
		require: '?ngModel',
		link: function(scope, element, attrs, controller) {
			if(!controller) return;
			
			controller.$render = function() {
				element.datetimepicker({autoclose: true}).on('changeDate', function(ev){
					scope.$apply(function() {
						controller.$setViewValue(
								new Date(ev.date.getTime() + (ev.date.getTimezoneOffset() * 60000)));
					});
				});
		        if(controller.$viewValue){
		          element.datetimepicker('update', new Date(controller.$viewValue));
		        }
				return controller.$viewValue;
			}
		}
	};
});angular.module('itsonin').directive('googleMap', function(){
	return {
		restrict: 'E',
		scope: {place:'='},
		template: '<input id="pac-input" class="form-control" style="margin-bottom:5px" type="text">'+
		'<div id="map-canvas" style="height: 400px;"></div>',
		link: function($scope, element, attrs){
			var markers = [];
			
			var center = new google.maps.LatLng(50.1, 14.4);
			
			var map = new google.maps.Map(document.getElementById('map-canvas'), 
					{mapTypeId: google.maps.MapTypeId.ROADMAP, zoom: 8});
			
			var geocoder = new google.maps.Geocoder();

			var defaultBounds = new google.maps.LatLngBounds(
				      new google.maps.LatLng(51.230501, 6.762852),
				      new google.maps.LatLng(51.840701, 6.762852));
				  map.fitBounds(defaultBounds);
				  
			var input = document.getElementById('pac-input');
			//map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

			var searchBox = new google.maps.places.SearchBox(input);

			// Listen for the event fired when the user selects an item from the
			// pick list. Retrieve the matching places for that item.
			google.maps.event.addListener(searchBox, 'places_changed', function() {
				var places = searchBox.getPlaces();

				for (var i = 0, marker; marker = markers[i]; i++) {
					marker.setMap(null);
				}

				// For each place, get the icon, place name, and location.
				markers = [];
				var bounds = new google.maps.LatLngBounds();
				for (var i = 0, place; place = places[i]; i++) {
					
					$scope.$apply(function (){
						$scope.place = place;
					});

					// Create a marker for each place.
					var marker = new google.maps.Marker({
						map: map,
						title: place.name,
						position: place.geometry.location,
						draggable: true
					});
					
					google.maps.event.addListener(marker, 'click', function (mouseEvent) {
						//console.log(marker.getPosition().lat())
						//latitude: gMapMarker.getPosition().lat(),
						//	longitude: gMapMarker.getPosition().lng(),
						var pos = mouseEvent.latLng;

					});
					
					google.maps.event.addListener(marker, 'dragend', function() {
						geocoder.geocode({
						    latLng: marker.getPosition()
						  }, function(responses) {
						    if (responses && responses.length > 0) {
						      console.log(responses[0].formatted_address);
						    } else {
						      console.log('Cannot determine address at this location.');
						    }
						  });
					  });

					markers.push(marker);

					bounds.extend(place.geometry.location);
				}

				map.fitBounds(bounds);
			});
			
			google.maps.event.addListener(map, 'bounds_changed', function() {
				var bounds = map.getBounds();
				searchBox.setBounds(bounds);
			});
		}
	}
});angular.module('itsonin').directive('googlePlaces', function(){
	return {
		restrict:'E',
		replace:true,
		scope: {event:'='},
		template: '<input id="google_places_ac" name="google_places_ac" type="text" class="input-block-level" ng-model="event.locationAddress"/>',
		link: function($scope, elm, attrs){
			var autocomplete = new google.maps.places.Autocomplete($("#google_places_ac")[0], {});
			google.maps.event.addListener(autocomplete, 'place_changed', function() {
				var place = autocomplete.getPlace();
				$scope.event['locationAddress'] = place.name;
				$scope.event['gpsLat'] = place.geometry.location.lat();
				$scope.event['gpsLong'] = place.geometry.location.lng();
				$scope.$apply();
			});
		}
	}
});angular.module('itsonin').directive('GooglePlus', ['$parse', function($parse) {
    'use strict';

    var options = {
        counter: {
            url: '{proxy}?url={url}&type=google-plus&callback=JSON_CALLBACK',
            getNumber: function(data) {
                return data.count;
            }
        },
        popup: {
            url: 'https://plus.google.com/share?url={url}',
            width: 700,
            height: 500
        },
        track: {
            'name': 'Google+',
            'action': 'share'
        }
    };
    return {
        restrict: 'C',
        require: '^?ngSocialButtons',
        scope: true,
        replace: true,
        transclude: true,
        template: '<li> \
                    <a ng-href="{{ctrl.link(options)}}" target="_blank" ng-click="ctrl.clickShare($event, options)" class="ng-social-button"> \
                        <span class="ng-social-icon"></span> \
                        <span class="ng-social-text" ng-transclude></span> \
                    </a> \
                    <span ng-show="count" class="ng-social-counter">{{ count }}</span> \
                   </li>',
        link: function(scope, element, attrs, ctrl) {
            element.addClass('ng-social-google-plus');
            if (!ctrl) {
                return;
            }
            var proxyUrl = $parse(attrs.proxyUrl)(scope) || '/proxy.php';
            options.counter.url = options.counter.url.replace('{proxy}', proxyUrl);
            scope.options = options;
            scope.ctrl = ctrl;
            ctrl.init(scope, element, options);
        }
    };
}]);angular.module('itsonin').factory('commentService',
	['$http', '$q', '$rootScope', '$cacheFactory', function($http, $q, $rootScope, $cacheFactory) {

	return {
	    create: function(eventId, comment, success, error) {
	        $http.post('/api/event/' + eventId + '/comment/create', comment).success(success).error(error);
	    },
	    list: function(eventId, success, error) {
	        $http.post('/api/event/' + eventId + '/comment/list').success(success).error(error);
	    }
	};
}]);
angular.module('itsonin').factory('eventService',
	['$http', '$q', '$rootScope', '$cacheFactory', function($http, $q, $rootScope, $cacheFactory) {

	return {
		list: function(queryParams, success, error) {
			$http.get('/api/event/list', {params:queryParams}).success(success).error(error);
		},
		create: function(event, guest, success, error) {
			$http.post('/api/event/create', {event:event, guest:guest}).success(success).error(error);
		},
		update: function(event, success, error) {
			$http.put('/api/event/' + event.eventId + '/update', event).success(success).error(error);
		},
		info: function(eventId, success, error) {
			$http.get('/api/event/' + eventId + '/info').success(success).error(error);
		},
		attend: function(eventId, guestName, success, error) {
			$http.get('/api/event/' + eventId + '/attend/' + encodeURIComponent(guestName))
				.success(success).error(error);
		},
		decline: function(eventId, guestName, success, error) {
			$http.get('/api/event/' + eventId + '/decline/' + encodeURIComponent(guestName))
				.success(success).error(error);
		}
	};
}]);
angular.module('itsonin').factory('guestService',
	['$http', function($http) {

	return {
	    create: function(eventId, type, status, success, error) {
	        $http.post('/api/event/' + eventId + '/guest/create', {status: status, type: type})
	        .success(success).error(error);
	    },
	    list: function(eventId, success, error) {
	        $http.get('api/event/' + eventId + '/guest/list').success(success).error(error);
	    }
	};
}]);
angular.module('itsonin').factory('interceptor',
  ['$rootScope', '$q', '$location', 'loading', function($rootScope, $q, $location, loading) {

  return {
    request: function(config) {
      loading.show();
      return config || $q.when(config);
    },
    response: function(response) {
      loading.hide();
      return response || $q.when(response);
    },
    responseError: function(rejection) {
      loading.hide();
      return $q.reject(rejection);
    }
  };
}]);
angular.module('itsonin').factory('loading', function() {
  var service = {
      requestCount: 0,
      message: $('<div id="loading"/>'),
      show: function() {
        this.requestCount++;
        var width = $(window).width();

        if ($("#loading").length === 0) {
          this.message.appendTo($(document.body));
        }

        this.message
            .addClass('notification')
            .addClass('inf')
            .css('left', width / 2 - this.message.width() / 2)
            .text('Loading ...')
            .show();
      },
      hide: function() {
        this.requestCount--;
        if (this.requestCount === 0) {
          this.message.hide();
        }
      },
      isLoading: function() {
        return this.requestCount > 0;
      }
  };
  return service;
});
angular.module('itsonin').factory('$modal', 
	['$rootScope', '$compile', '$http', '$timeout', '$q', '$templateCache',
	function ($rootScope, $compile, $http, $timeout, $q, $templateCache) {
	var ModalFactory = function ModalFactory(config) {
	      function Modal(config) {
	        var options = angular.extend({ show: true }, config);
	        var scope = options.scope ? options.scope : $rootScope.$new();
	        var templateUrl = options.template;
	        return $q.when($templateCache.get(templateUrl) || $http.get(templateUrl, { cache: true }).then(function (res) {
	          return res.data;
	        })).then(function onSuccess(template) {
	          var id = templateUrl.replace('.html', '').replace(/[\/|\.|:]/g, '-') + '-' + scope.$id;
	          var $modal = $('<div class="modal" tabindex="-1"></div>').attr('role', 'dialog')
	          	.attr('aria-hidden', true).attr('id', id).html(template);
	          if (options.modalClass)
	            $modal.addClass(options.modalClass);
	          $('body').append($modal);
	          $timeout(function () {
	            $compile($modal)(scope);
	          });
	          scope.$modal = function (name) {
	            $modal.modal(name);
	          };
	          angular.forEach([
	            'show',
	            'hide'
	          ], function (name) {
	            scope[name] = function () {
	              $modal.modal(name);
	            };
	          });
	          scope.dismiss = scope.hide;
	          angular.forEach([
	            'show.bs.modal',
	            'shown.bs.modal',
	            'hide.bs.modal',
	            'hidden.bs.modal'
	          ], function (name) {
	            $modal.on(name, function (ev) {
	              scope.$emit(/*'modal-' + */name, ev);
	            });
	          });
	          if(options.data){
	        	  scope['data'] = angular.copy(options.data);
	          }
	          $modal.on('shown.bs.modal', function (ev) {
	            $('input[autofocus], textarea[autofocus]', $modal).first().trigger('focus');
	          });
	          $modal.on('hidden.bs.modal', function (ev) {
	            if (!options.persist)
	              scope.$destroy();
	          });
	          scope.$on('$destroy', function () {
	            $modal.remove();
	          });
	          $modal.modal(options);
	          return $modal;
	        });
	      }
	      return new Modal(config);
	    };
	    return ModalFactory;
}]);angular.module('itsonin').factory('shareService',
	['$q', '$modal', 'views', function($q, $modal, views) {

	return {
		shareLink: function (eventId, hostId) {
	        var modalPromise = $modal({
	            template: views.shareLink,
	            persist: false,
	            show: false,
	            keyboard: true,
	            data: {eventId: eventId, hostId: hostId}
	        });

	        $q.when(modalPromise).then(function(modalEl) {
	            modalEl.modal('show');
	        });
	    },
	    shareByEmail: function (eventId, hostId) {
	        var modalPromise = $modal({
	            template: views.shareByEmail,
	            persist: false,
	            show: false,
	            keyboard: true,
	            data: {eventId: eventId, hostId: hostId}
	        });

	        $q.when(modalPromise).then(function(modalEl) {
	            modalEl.modal('show');
	        });
	    }
	};
}]);
angular.module('itsonin').constant('views', {
    list: 'views/list.html?v=1',
    viewEvent: 'views/viewEvent.html?v=1',
    editEvent: 'views/editEvent.html?v=1',
    attendEvent: 'views/attendEvent.html?v=1',
    invitation: 'views/invitation.html?v=1',
    attendInvitation: 'views/attendInvitation.html?v=1',
    declineInvitation: 'views/declineInvitation.html?v=1',
    about: 'views/about.html?v=1',
    welcome: 'views/welcome.html?v=1',    
    me: 'views/me.html?v=1',
    shareLink: 'views/modals/shareLink.html?v=1',
    shareByEmail: 'views/modals/shareByEmail.html?v=1',
    map: 'views/modals/map.html?v=1'
});