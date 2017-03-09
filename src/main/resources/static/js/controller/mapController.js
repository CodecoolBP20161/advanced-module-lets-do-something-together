'use strict';

var actimate = angular.module('actimate', ['gm']);

actimate.controller('mapCtrl', function($scope) {
    $scope.lat = undefined;
    $scope.lng = undefined;

    $scope.$on('gmPlacesAutocomplete::placeChanged', function(){
        var location = $scope.autocomplete.getPlace().geometry.location;
        $scope.lat = location.lat();
        $scope.lng = location.lng();
        $scope.$apply();
    });
});
