angular.module('itsonin').controller('InvitationController',
  ['$scope', '$routeParams', '$location', 'eventService', 
   function ($scope, $routeParams, $location, eventService) {
			  
	$scope.loadEvent = function () {
		eventService.info($routeParams.invitationId, function(response) {//TODO: send eventId
			$scope.event = response;
		},
		function(error) {
						
		});
	}
				
	$scope.loadEvent();
	
	$scope.attendEvent = function () {
		eventService.attend($routeParams.invitationId, function(response) {//TODO:
			$location.path('/i/' + $routeParams.invitationId + '/attend');
		},
		function(error) {
						
		});
	}
	
	$scope.declineEvent = function () {
		eventService.decline($routeParams.invitationId, function(response) {//TODO:
			$location.path('/i/' + $routeParams.invitationId + '/decline');
		},
		function(error) {
						
		});
	}
		  
}]);
