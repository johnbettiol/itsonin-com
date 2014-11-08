"use strict";

angular.module('itsonin').factory('eventService', ['$http', function($http){
    return {
        save: function(event, success, error) {
        	$http.put('/api/admin/event', event).success(function(response) {
	        	if(angular.isFunction(success)){
	        		success(response);
	        	}
	        }).error(error);
        },
        list: function(city, success, error) {
        	$http.get('/api/admin/event/' + city).success(function(response) {
	        	if(angular.isFunction(success)){
	        		success(response);
	        	}
	        }).error(error);
        },
        remove: function(ids, success, error) {
        	$http.post('/api/admin/event/remove', ids).success(function(response) {
	        	if(angular.isFunction(success)){
	        		success(response);
	        	}
	        }).error(error);
        }
    };
}]);