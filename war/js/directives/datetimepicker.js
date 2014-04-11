angular.module('itsonin').directive('bDatetimepicker', function(){
	return {
		require: '?ngModel',
		link: function(scope, element, attrs, controller) {
			if(!controller) return;
			
			controller.$render = function() {
				element.datetimepicker({autoclose: true}).on('changeDate', function(ev){
					scope.$apply(function() {
						controller.$setViewValue(
								new Date(ev.date.getTime() + (ev.date.getTimezoneOffset() * 60000)));
					});
				});
		        if(controller.$viewValue){
		          element.datetimepicker('update', new Date(controller.$viewValue));
		        }
				return controller.$viewValue;
			}
		}
	};
});