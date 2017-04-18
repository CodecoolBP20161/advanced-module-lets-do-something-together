'use strict';

var actimate = angular.module('actimate', ['ngResource', 'gm', 'ngRoute']);
actimate.config(['$httpProvider', '$qProvider', function ($httpProvider, $qProvider) {
    $httpProvider.defaults.useXDomain = true;
    $qProvider.errorOnUnhandledRejections(false);
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);


actimate.controller('saveEventCtrl', function ($scope, $http) {
    $scope.event = {};

    $('#datetimepicker1').on('dp.change', function (data) {
        $scope.event.date = moment(data.date).format("DD/MM/YYYY HH:mm a");
        $scope.$apply();
        });


    $scope.saveEvent = function () {

        $http({
            method: 'POST',
            url: '/u/create_event',
            headers: {'Content-Type': 'application/json; charset=UTF-8'},
            data: JSON.stringify($scope.event)
        })
            .then(function (response) {
            })
    };

    $scope.$on('gmPlacesAutocomplete::placeChanged', function () {
        var location = $scope.autocomplete.getPlace().geometry.location;
        $scope.event.lat = location.lat();
        $scope.event.lng = location.lng();
        $scope.event.location = $scope.autocomplete.getPlace().formatted_address;
        $scope.$apply();
    });


});
