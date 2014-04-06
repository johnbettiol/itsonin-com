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
