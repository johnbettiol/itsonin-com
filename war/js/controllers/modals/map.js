angular.module('itsonin').controller('MapController', ['$scope', '$rootScope', function ($scope, $rootScope) {
	$scope.place = {};
	  
	$scope.select = function(){
		$rootScope.$broadcast('event:setLocation', $scope.place);
		$scope.hide();
	}
	
}]);
