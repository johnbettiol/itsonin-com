angular.module('itsonin').filter('calendar', function() {
    return function(date) {
        return moment(date).calendar();
    };
});