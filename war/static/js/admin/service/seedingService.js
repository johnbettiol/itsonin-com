"use strict";

angular.module('itsonin').factory('seedingService', ['$http', function($http){
    return {
        seed: function(engine, success, error) {
        	$http.get('/api/admin/seed/' + engine).success(function(response) {
	        	if(angular.isFunction(success)){
	        		success(response);
	        	}
	        }).error(error);
        }
    };
}]);