angular.module('itsonin').controller('ShareLinkController',
  ['$scope', function ($scope) {
	  $scope.link = 'http://' + window.location.host + '/#/i/' + $scope.data.eventId + '.' + $scope.data.hostId;
	  //TODO: replace host
}]);
