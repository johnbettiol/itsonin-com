"use strict";

angular.module('itsonin').controller('SeedingController', ['$scope', 'seedingService',
	function ($scope, seedingService) {

	$scope.seedingEngines = ['DusPrinz', 'DusEventim', 'DusTourismo', 'DusTonight'];

	$scope.startSeeding = function() {
		seedingService.seed($scope.seedingEngine,
			function(response) {
			},
			function(error) {
				console.log(error);
			});
	};

    $scope.openFromPicker = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.openedFrom = true;
    };

    $scope.openToPicker = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.openedTo = true;
    };

}]);
