angular.module('itsonin').controller('ViewEventController',
  ['$scope', '$routeParams', '$location', 'eventService', 'guestService', 'commentService', 'shareService',
   function ($scope, $routeParams, $location, eventService, guestService, commentService, shareService) {

	$scope.readyToShow = false;
	$scope.event = {};

	$scope.loadEvent = function () {
		eventService.info($routeParams.eventId, null, function(response) {
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
