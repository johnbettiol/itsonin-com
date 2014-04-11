angular.module('itsonin').controller('ShareByEmailController',
  ['$scope', function ($scope) {

      $scope.link = 'http://' + window.location.host + '/i/' + $scope.data.eventId + '.' + $scope.data.hostId;

}]);
