angular.module('itsonin', ['ngRoute', 'ngSanitize', 'ngCookies', 'google-maps'])
.config(['$routeProvider', 'views', function($routeProvider, views) {
  $routeProvider
  	  .when('/', {templateUrl: views.list, controller: 'ListController'})
      .when('/e/add', {templateUrl: views.editEvent, controller: 'EditEventController'})
      .when('/e/:eventId', {templateUrl: views.viewEvent, controller: 'ViewEventController'})
      .when('/e/:eventId/edit', {templateUrl: views.editEvent, controller: 'EditEventController'})
      .when('/e/:eventId/attend', {templateUrl: views.attendEvent, controller: 'AttendEventController'})
      .when('/i/:invitationId', {templateUrl: views.invitation, controller: 'InvitationController'})
      .when('/i/:invitationId/attend', {templateUrl: views.attendInvitation, controller: 'AttendInvitationController'})
      .when('/i/:invitationId/decline', {templateUrl: views.declineInvitation, controller: 'DeclineInvitationController'})
      .when('/about', {templateUrl: views.about, controller: 'AboutController'})
      .when('/me', {templateUrl: views.me, controller: 'MeController'})
      .when('/:location', {templateUrl: views.list, controller: 'ListController'})
      .otherwise({redirectTo: '/:location'});
}])

.config(['$httpProvider', function($httpProvider) {
	$httpProvider.interceptors.push('interceptor'); 
}])

.run(['$rootScope', '$location', '$cookies', 
    function($rootScope, $location, $cookies) {
	
	$rootScope.location = 'dusseldorf'; //TODO: get location

	if(!$cookies.token){
		$location.path('/about');
	} else if($location.path() == '/' || $location.path() == ''){
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
        {id: 'EXCERCISE', img: 'book', background: ''}
    ],
    EVENT_SHARABILITIES: [
        {id: 'NOSHARE', img: 'picture'},
        {id: 'NORMAL', img: 'gift'},
        {id: 'PYRAMID', img: 'cutlery'}
    ],
    EVENT_VISIBILITIES: [
        {id: 'PUBLIC', img: 'gift'},
    	{id: 'PRIVATE', img: 'picture'},
    	{id: 'FRIENDSONLY', img: 'cutlery'}
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
	['$scope', '$routeParams', '$modal', 'eventService', 'views', '$q',
	 function ($scope, $routeParams, $modal, eventService, views, $q) {

		$scope.loadEvent = function () {
			eventService.info($routeParams.eventId, function(response) {
				$scope.event = response;
			},
			function(error) {
			});
		}

		$scope.loadEvent();

		$scope.shareLink = function () {
		    var modalPromise = $modal({
		        template: views.shareLink,
		        persist: false,
		        show: false,
		        keyboard: true,
		        data: {eventId: $routeParams.eventId, hostId: $routeParams.hostId}
		    });

		    $q.when(modalPromise).then(function(modalEl) {
		        modalEl.modal('show');
		    });
		}
}]);

angular.module('itsonin').controller('AttendInvitationController',
	['$scope', '$routeParams', '$modal', 'eventService', 'views', '$q',
	 function ($scope, $routeParams, $modal, eventService, views, $q) {

		$scope.eventId = $routeParams.invitationId.split('.')[0];
		$scope.parentGuestId = $routeParams.invitationId.split('.')[1];
		
		$scope.loadEvent = function () {
			eventService.info($scope.eventId, function(response) {
				$scope.event = response;
			},
			function(error) {
			});
		}

		$scope.loadEvent();

		$scope.shareLink = function () {
		    var modalPromise = $modal({
		        template: views.shareLink,
		        persist: false,
		        show: false,
		        keyboard: true,
		        data: {}
		    });

		    $q.when(modalPromise).then(function(modalEl) {
		        modalEl.modal('show');
		    });
		}
}]);
angular.module('itsonin').controller('DeclineInvitationController',
	['$scope', '$routeParams', 'eventService', function ($scope, $routeParams, eventService) {

		$scope.eventId = $routeParams.invitationId.split('.')[0];
		$scope.parentGuestId = $routeParams.invitationId.split('.')[1];
		
		$scope.loadEvent = function () {
			eventService.info($scope.eventId, function(response) {
				$scope.event = response;
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
			  visibility: 'PUBLIC',
			  flexibility: 'FIXED'
	  };

	  $scope.loadEvent = function () {
		  if($routeParams.eventId){
			  eventService.info($routeParams.eventId, function(response) {
				  $scope.event = response;
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
		  eventService.create($scope.event, {name: 'nikolai'}, function(resp) {//TODO: where get the name ?
			  $location.url('/e/' + resp.event.eventId + '/attend?hostId=' + resp.guest.guestId);
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
  ['$scope', '$routeParams', '$location', 'eventService', 
   function ($scope, $routeParams, $location, eventService) {
	  
	$scope.eventId = $routeParams.invitationId.split('.')[0];
	$scope.parentGuestId = $routeParams.invitationId.split('.')[1];
			  
	$scope.loadEvent = function () {
		eventService.info($scope.eventId, function(response) {
			$scope.event = response;
		},
		function(error) {
						
		});
	}
				
	$scope.loadEvent();
	
	$scope.attendEvent = function () {
		eventService.attend($scope.eventId, function(response) {
			$location.path('/i/' + $routeParams.invitationId + '/attend');
		},
		function(error) {
						
		});
	}
	
	$scope.declineEvent = function () {
		eventService.decline($scope.eventId, function(response) {
			$location.path('/i/' + $routeParams.invitationId + '/decline');
		},
		function(error) {
						
		});
	}
		  
}]);
angular.module('itsonin').controller('ListController',
  ['$scope', '$rootScope', '$routeParams', 'eventService', 'constants', 
   function ($scope, $rootScope, $routeParams, eventService, constants) {
	  
	  $scope.isKnownLocation = ($rootScope.location == $routeParams.location);
	  $scope.allEvents = true;
	  $scope.allDates = true;
	  $scope.allPlaces = true;
	  $scope.allCategories = true;
	  $scope.eventTypes = constants.EVENT_TYPES;
	  $scope.locationExist = ($routeParams.location == $rootScope.location);
	  $scope.filter = {
		types: []
	  };

	  $scope.$watch('filter.startTime', function(newValue, oldValue) {
		  if(newValue && (newValue+'').length == 16 ){
			  $scope.loadEvents();
		  }
	  });
	  
	  $scope.loadEvents = function () {
		eventService.list($scope.filter, function(response) {
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
		  $scope.resetFilter();
		  $scope.loadEvents();
		  $scope.allEvents = !$scope.allEvents;
		  $scope.allDates = true;
		  $scope.allPlaces = true;
		  $scope.allCategories = true;
	  }
	  
	  $scope.toggleDatesFilter = function() {
		  $scope.resetFilter();
		  $scope.loadEvents();
		  $scope.allDates = !$scope.allDates;
		  $scope.allEvents = true;
		  $scope.allPlaces = true;
		  $scope.allCategories = true;
	  }
	  
	  $scope.togglePlacesFilter = function() {
		  $scope.resetFilter();
		  $scope.loadEvents();
		  $scope.allPlaces = !$scope.allPlaces;
		  $scope.allDates = true;
		  $scope.allEvents = true;
		  $scope.allCategories = true;
	  }
	  
	  $scope.toggleCategoriesFilter = function() {
		  $scope.resetFilter();
		  $scope.loadEvents();
		  $scope.allCategories = !$scope.allCategories;
		  $scope.allDates = true;
		  $scope.allPlaces = true;
		  $scope.allEvents = true;
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
	  
	  $scope.resetFilter = function() {
		  $scope.filter = {
			types: []
		  };
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
angular.module('itsonin').controller('MapController',
  ['$scope', function ($scope) {
	  $scope.map = {
			  center: {
				  latitude: 51.230501,
				  longitude: 6.762852
			  },
			  zoom: 8
	  };
}]);
angular.module('itsonin').controller('ShareLinkController',
  ['$scope', function ($scope) {
	  $scope.link = 'http://' + window.location.host + '/#/i/' + $scope.data.eventId + '.' + $scope.data.hostId;
	  //TODO: replace host
}]);
angular.module('itsonin').controller('ViewEventController',
  ['$scope', '$routeParams', 'eventService', 'guestService', 
   function ($scope, $routeParams, eventService, guestService) {

	$scope.readyToShow = false;

	$scope.loadEvent = function () {
		eventService.info($routeParams.eventId, function(response) {
			$scope.event = response;
			$scope.readyToShow = true;
		},
		function(error) {

		});
	}

	$scope.loadEvent();
	
	$scope.createGuest = function () {
		guestService.create($routeParams.eventId, 'GUEST', 'VIEWED', function(response) {

		},
		function(error) {

		});
	}

	$scope.createGuest();
	  
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
	      var updateModel = function(ev) {
	        scope.$apply(function() {
	            controller.$setViewValue(new Date(ev.date.valueOf()));
	            element.datetimepicker('hide');
	            element.blur();
	          });
	      };
	      if (controller != null) {
	        controller.$render = function() {
	          element.datetimepicker().initialDate = controller.$viewValue;
	          element.datetimepicker({autoclose: true}).on('changeDate', updateModel);
	          if(controller.$viewValue){
	        	  element.datetimepicker('update', new Date(controller.$viewValue));
	          }
	          return controller.$viewValue;
	        };
	      }/*else {
	    	  element.datetimepicker({autoclose: true}).on('changeDate', updateModel);
	      }*/
	    }
	  };
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
});angular.module('itsonin').factory('commentService',
	['$http', '$q', '$rootScope', '$cacheFactory', function($http, $q, $rootScope, $cacheFactory) {

	return {
		create: function(eventId, comment, success, error) {
			$http.post('/api/event/' + eventId + '/comment/create', comment).success(success).error(error);
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
			$http.get('/api/event/' + eventId + '/info', event).success(success).error(error);
		},
		attend: function(eventId, success, error) {
			$http.get('/api/event/' + eventId + '/attend', event).success(success).error(error);
		},
		decline: function(eventId, success, error) {
			$http.get('/api/event/' + eventId + '/decline', event).success(success).error(error);
		}
	};
}]);
angular.module('itsonin').factory('guestService',
	['$http', function($http) {

	return {
		create: function(eventId, type, status, success, error) {
			$http.post('/api/event/' + eventId + '/guest/create', {status: status, type: type})
			.success(success).error(error);
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
}]);angular.module('itsonin').constant('views', {
    list: 'views/list.html?v=1',
    viewEvent: 'views/viewEvent.html?v=1',
    editEvent: 'views/editEvent.html?v=1',
    attendEvent: 'views/attendEvent.html?v=1',
    invitation: 'views/invitation.html?v=1',
    attendInvitation: 'views/attendInvitation.html?v=1',
    declineInvitation: 'views/declineInvitation.html?v=1',
    about: 'views/about.html?v=1',
    me: 'views/me.html?v=1',
    shareLink: 'views/modals/shareLink.html?v=1',
    map: 'views/modals/map.html?v=1'
});