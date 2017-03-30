'use strict';

var actimate = angular.module('actimate', ['ngResource', 'gm']);
actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);


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
        $scope.event.location = $scope.autocomplete.getPlace().formatted_address;
        $scope.$apply();
    });

    var selectedDate = $scope.event.date;
    var now = new Date();
    if (selectedDate < now) {
        console.log('error in date');
    }


});
