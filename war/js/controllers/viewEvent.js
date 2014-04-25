angular.module('itsonin').controller('ViewEventController',
  ['$scope', '$routeParams', '$location', 'eventService', 'commentService', 'shareService',
   function ($scope, $routeParams, $location, eventService, commentService, shareService) {

	$scope.readyToShow = false;
	$scope.event = {};
	$scope.pageType = ($location.path().indexOf('/i') === 0)? 'SHARE' : 'INFO';

	$scope.loadEvent = function () {
		var queryParams = ($scope.pageType == 'SHARE')? {forInvitation: true} : null;
		eventService.info($routeParams.eventId, queryParams, function(response) {
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
			$scope.guest.status = 'ATTENDING';
			$location.path('/i/' + $routeParams.eventId + '.' + response.guestId);
		},function(error) {
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
			$scope.success = 'Event declined successfully';
			$location.path('/i/' + $routeParams.eventId + '.' + response.guestId);
		},
		function(error) {
			console.log(error);			
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
