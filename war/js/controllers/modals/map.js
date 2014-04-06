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
