'use strict';


actimate.run(function(defaultErrorMessageResolver){
        defaultErrorMessageResolver.getErrorMessages().then(function (errorMessages) {
            errorMessages['wrongInputType'] = 'Please enter letters only!';
        });
    }
);



actimate.directive('loadEventCtrl', function () {
    return {
        controller: function ($scope, $http) {
            $scope.events = null;

            $http.get("/u/events")
                .then(function (response) {
                    $scope.events = response.data.events;
                })
        }
    };
});