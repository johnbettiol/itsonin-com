"use strict";

angular.module('itsonin').controller('EventsController', ['$scope', 'eventService', '$modal', 'views',
	function ($scope, eventService, $modal, views) {

	$scope.config = Config;
	$scope.filters = [];
	$scope.selectedEvents = [];
	$scope.editableEvents = {};
	$scope.sort = {column: 'eventId', asc: true};
	$scope.visibilities = ['PUBLIC', 'PRIVATE', 'FRIENDSONLY'];
	$scope.sharabilities = ['NOSHARE', 'NORMAL', 'PYRAMID'];
	$scope.statuses = ['ACTIVE', 'EXPIRED', 'CANCELLED'];
	$scope.subcategories = [
	                    	'PARTY',
	                    	'DJNIGHT',
	                    	'OTHERNGT',
	                    	'MEETUP',
	                    	'SINGLES',
	                    	'OTHERSOC',
	                    	'FAMILY_KIDS',
	                    	'FOOTBALL',
	                    	'ICEHOCKEY',
	                    	'MOTORSPORT',
	                    	'OTHERSPR',
	                    	'CONCERT',
	                    	'ART',
	                    	'ACADEMIC',
	                    	'CONVENTION',
	                    	'MUSEUM',
	                    	'TOWN',
	                    	'FREE_TIME',
	                    	'OTHERCUL'];

	$scope.loadEvents = function() {
		eventService.list($scope.config.city, function(response) {
			$scope.events = response;
			$scope.filteredEvents = response;
		},
		function(error) {
			console.log(error);
		});
	};

	$scope.loadEvents();

	$scope.deleteEvents = function() {
		if($scope.selectedEvents.length > 0){
			eventService.remove($scope.selectedEvents,
				function(response) {
					$scope.selectedEvents = [];
					$scope.loadEvents();
				},
				function(error) {
					console.log(error);
			});
		}
	};

	$scope.saveEvent = function(event) {
		eventService.save(event,
			function(response) {
				event.editing = false;
			},
			function(error) {
				console.log(error);
		});
	};

	$scope.editEvent = function(event) {
		$scope.editableEvents[event.eventId] = angular.copy(event);
		event.editing = true;
	};

	$scope.cancelEdit = function(event) {
		var e = $scope.editableEvents[event.eventId];
		for(var prop in e){
			if(e.hasOwnProperty(prop)){
				event[prop] = e[prop];
			}
		}
		event.editing = false;
	};

	$scope.changeSorting = function(column) {
        if ($scope.sort.column == column) {
        	$scope.sort.asc = !$scope.sort.asc;
        } else {
        	$scope.sort.column = column;
        	$scope.sort.asc = true;
        }
    };

	$scope.getSortableClass = function(column) {
        if ($scope.sort.column == column) {
        	if($scope.sort.asc == true) {
        		return 'fa fa-sort-asc';
        	} else {
        		return 'fa fa-sort-desc';
        	}
        } else {
        	return '';
        }
    };

	$scope.toggleCheckBox = function(eventId) {
		var i = $.inArray(eventId, $scope.selectedEvents);
		if(i >= 0) {
			$scope.selectedEvents.splice(i,1);
		} else {
			$scope.selectedEvents.push(eventId);
		}
	}

	$scope.getSelectedEventsCount = function() {
		return $scope.selectedEvents.length;
	}

	$scope.showFilters = function() {
		var modalInstance = $modal.open({
			templateUrl: views.filter,
			controller: 'FilterModalController',
			resolve: {
				filters: function () {
					return $scope.filters;
				}
			}
		});

		modalInstance.result.then(function (filters) {
			$scope.filters = filters;
			$scope.filteredEvents = $.grep($scope.events, function (event) {
				var match = $scope.filters.length == 0 ? true : false;
				angular.forEach($scope.filters, function(filter) {
					switch (filter.condition) {
					  case 'contains':
						if(event[filter.field].indexOf(filter.value) > -1) {
							match = true;
						}
					    break;
					  case 'does not contain':
						if(event[filter.field].indexOf(filter.value) < 0) {
							match = true;
						}
					    break;
					  case 'is equal to':
						if(event[filter.field] == filter.value) {
							match = true;
						}
					    break;
					  case 'is not equal to':
						if(event[filter.field] != filter.value) {
							match = true;
						}
						break;

					  case 'is less than':
						if(event[filter.field] < filter.value) {
							match = true;
						}
						break;
					  case 'is greater than':
						if(event[filter.field] > filter.value) {
							match = true;
						}
						break;
					  case 'begin with':
						if(event[filter.field].indexOf(filter.value) == 0) {
							match = true;
						}
						break;
					  case 'end with':
						if(event[filter.field].indexOf(filter.value) == event[filter.field].length - filter.value.length) {
							match = true;
						}
						break;
					  case 'is empty':
						if(!event[filter.field] || event[filter.field] == '') {
							match = true;
						}
						break;
					  case 'is not empty':
						if(event[filter.field] && event[filter.field] != '') {
							match = true;
						}
						break;
					  default:
					    break;
					}
				});
				return match; 
			});
		}, function () {
			console.log('Modal dismissed at: ' + new Date());
		});
	}

}]);
