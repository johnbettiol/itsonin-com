angular.module('itsonin', ['ngRoute', 'ngSanitize', 'ngCookies'])
.config(['$routeProvider', 'views', '$locationProvider', function($routeProvider, views, $locationProvider) {
  $routeProvider
  	  .when('/', {templateUrl: views.list, controller: 'ListController'})
      .when('/e/add', {templateUrl: views.editEvent, controller: 'EditEventController'})
      .when('/e/:eventId', {templateUrl: views.viewEvent, controller: 'ViewEventController'})
      .when('/e/:eventId/edit', {templateUrl: views.editEvent, controller: 'EditEventController'})
      .when('/e/:eventId/attend', {templateUrl: views.attendEvent, controller: 'AttendEventController'})
      .when('/i/:invitationId', {templateUrl: views.invitation, controller: 'InvitationController'})
      .when('/i/:invitationId/attend', {templateUrl: views.attendInvitation, controller: 'AttendInvitationController'})
      .when('/i/:invitationId/decline', {templateUrl: views.declineInvitation, controller: 'DeclineInvitationController'})
      .when('/about', {templateUrl: views.about, controller: 'AboutController'})
      .when('/welcome', {templateUrl: views.welcome, controller: 'WelcomeController'})
      .when('/me', {templateUrl: views.me, controller: 'MeController'})
      .when('/:location', {templateUrl: views.list, controller: 'ListController'})
      .otherwise({redirectTo: '/:location'});
  
  	  $locationProvider.html5Mode(true);
}])

.config(['$httpProvider', function($httpProvider) {
	$httpProvider.interceptors.push('interceptor'); 
}])

.run(['$rootScope', '$location', '$cookies',
    function($rootScope, $location, $cookies) {

	$rootScope.location = 'Düsseldorf'; //TODO: get location

	$rootScope.$on("$routeChangeStart", function (event, next, current) {
		if($location.path() == '/' || $location.path() == '' || $location.path() == '/Duesseldorf'
			|| $location.path() == '/Dusseldorf'){
			$location.path('/' + $rootScope.location);
		}
	});

	if (!$cookies.token){
		$location.path('/welcome');
	} else if($location.path() == '/' || $location.path() == '' || $location.path() == '/Duesseldorf'
		|| $location.path() == '/Dusseldorf'){
		$location.path('/' + $rootScope.location);
	}
		
}]);