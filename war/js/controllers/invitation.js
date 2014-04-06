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
