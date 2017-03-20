'use strict';

var actimate = angular.module('actimate', ['ngResource', 'gm', 'jcs-autoValidate']);
actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);
actimate.run(function (defaultErrorMessageResolver) {
        defaultErrorMessageResolver.getErrorMessages().then(function (errorMessages) {
            errorMessages['wrongNumber'] = 'Please invite at least 1 person!';
            errorMessages['wrongInputType'] = 'Please enter letters only!';
        });
    }
);

actimate.controller('saveEventCtrl', function ($scope, $http) {
    $scope.event = {};

    $scope.saveEvent = function () {

        $http({
            method: 'POST',
            url: '/u/create_event',
            headers: {'Content-Type': 'application/json; charset=UTF-8'},
            data: JSON.stringify($scope.event)
        })
            .then(function (response) {
                console.log($scope.event);
            })
    };

    $scope.$on('gmPlacesAutocomplete::placeChanged', function () {
        var location = $scope.autocomplete.getPlace().geometry.location;
        $scope.event.lat = location.lat();
        $scope.event.lng = location.lng();
        $scope.$apply();
    });

});
