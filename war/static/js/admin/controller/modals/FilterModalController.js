"use strict";

angular.module('itsonin').controller('FilterModalController', ['$scope', '$modalInstance', 'filters',
	function ($scope, $modalInstance, filters) {

	$scope.filters = filters;
	$scope.filter = {};
	$scope.fields = ['title', 'offer', 'description', 'subCategory', 'status', 'visibility', 'startTime', 'endTime'];
	$scope.conditions = ['contains', 'does not contain', 'is equal to', 'is not equal to', 'is less than',
	                     'is greater than', 'begin with', 'end with', 'is empty', 'is not empty'];

	$scope.addFilter = function () {
		$scope.filters.push($scope.filter);
		$scope.filter = {};
	};

	$scope.removeFilter = function (index) {
		$scope.filters.splice(index, 1);
	};
	
	$scope.apply = function () {
		$modalInstance.close($scope.filters);
	};

	$scope.close = function () {
		$modalInstance.dismiss('cancel');
	};

}]);
