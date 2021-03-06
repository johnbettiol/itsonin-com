angular.module('itsonin').controller('EditEventController',
  ['$scope', '$rootScope', '$routeParams', '$modal', '$q', '$timeout', '$location', 'views', 'eventService', 'constants', 
   function ($scope, $rootScope, $routeParams, $modal, $q, $timeout, $location, views, eventService, constants) {
	  
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
			$scope.event['locationAddress'] = place.formatted_address;
			$scope.event['gpsLat'] = place.geometry.location.lat();
			$scope.event['gpsLong'] = place.geometry.location.lng();
	  });
	  
	  $scope.getObjById = function(array, id) {
		  for(var i=0; i < array.length; i++){
			  if(id == array[i].id){
				  return array[i];
			  }
		  }
	  }
	  
	  $scope.initUI = function(){
		  var sharability = $scope.getObjById(constants.EVENT_SHARABILITIES,
				  $scope.event.sharability);
		  var visibility = $scope.getObjById(constants.EVENT_VISIBILITIES,
				  $scope.event.visibility);
		  $scope.sharabilityImg = sharability.img;
		  $scope.sharabilityText = sharability.text;
		  $scope.visibilityImg = visibility.img;
		  $scope.visibilityText = visibility.text;
		  $scope.readyToShow = true;
	  }

	  $scope.loadEvent = function () {
		  if($routeParams.eventId){
			  eventService.info($routeParams.eventId, null, function(response) {
				  $scope.event = response.event;
				  $scope.guest = response.guest;
			  
				  $scope.dateType = 'CUSTOM';
				  $scope.locationType = 'MAP';
				  $scope.initUI();
			  },
			  function(error) {
	
			  });
		  } else {
			  $scope.initUI();
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
		  $scope.sharabilityText = sharability.text;
	  }
	  
	  $scope.nextVisibility = function() {
		  var visibility = $scope.cycle(constants.EVENT_VISIBILITIES, $scope.event.visibility);
		  $scope.event.visibility = visibility.id;
		  $scope.visibilityImg = visibility.img;
		  $scope.visibilityText = visibility.text;
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
	  
	  $scope.shareEvent = function () {
		  delete $scope.error;
		  eventService.create($scope.event, $scope.guest, function(resp) {
			  $scope.success = 'New event was successfully created.';
			  $timeout(function() {
				  $location.path('/i/' + resp.event.eventId + '.' + resp.guest.guestId);
			  }, 800);
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
		  eventService.update($scope.event, $scope.guest, function(response) {
			  $location.path('/' + $rootScope.location);
		  });
	  }

}]);
