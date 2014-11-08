"use strict";

angular.module("itsonin", ["ngRoute", "ngSanitize", "ui.bootstrap"])

.config(["$routeProvider", "views", function($routeProvider, views) {
    $routeProvider
        .when("/events", {
            templateUrl: views.events,
            controller: "EventsController"
        	})
        .when("/seeding", {
            templateUrl: views.seeding,
            controller: "SeedingController"
        	})
        .when("/users", {
            templateUrl: views.users,
            controller: "UsersController"
        	})
        .otherwise({
            redirectTo: "/events"
        });
}])

.config(["$httpProvider", function($httpProvider) {
    $httpProvider.interceptors.push("interceptor");
}]);