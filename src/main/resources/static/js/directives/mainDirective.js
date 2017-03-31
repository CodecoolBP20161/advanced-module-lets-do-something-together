'use strict';


actimate.run(function(defaultErrorMessageResolver){
        defaultErrorMessageResolver.getErrorMessages().then(function (errorMessages) {
            errorMessages['wrongInputType'] = 'Please enter letters only!';
        });
    }
);


actimate.directive('mainCtrl', function () {
    return {
        controller: function () {
            return {
                templateUrl: 'templates/main.html',
                replace : true,
                controller: 'mainCtrl',
                restrict: 'E'
            }
        }
    };
});
