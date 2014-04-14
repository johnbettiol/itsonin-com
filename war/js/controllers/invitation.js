angular.module('itsonin').controller('InvitationController',
  ['$scope', '$routeParams', '$location', 'eventService', 'shareService',
   function ($scope, $routeParams, $location, eventService, shareService) {
	  
	$scope.hostId = $routeParams.hostId;
			  
	$scope.loadEvent = function () {
		eventService.info($routeParams.eventId, {forInvitation: true}, function(response) {
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
    	window.open(url, 'Share', ',personalbar=0,toolbar=0,scrollbars=1,resizable=1');
    }
    
    $scope.getFacebookUrl = function() {
    	var url = 'https://www.facebook.com/sharer/sharer.php?u=' + $location.host() + $location.path();
    	window.open(url, 'Share', 'personalbar=0,toolbar=0,scrollbars=1,resizable=1');
    	//TODO:https://developers.facebook.com/docs/sharing/reference/feed-dialog
    	//https://github.com/esvit/angular-social/blob/master/src/scripts/03-twitter.js
    }
		  
}]);
