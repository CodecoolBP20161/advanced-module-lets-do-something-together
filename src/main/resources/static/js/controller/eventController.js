'use strict';

var actimate = angular.module('actimate', ['ngResource']);
actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);

actimate.controller('saveEventCtrl', function ($scope, $http) {
   $scope.event = {};
    
    $scope.saveEvent = function () {

        $http({
            method: 'POST',
            url: '/createevent',
            headers: {'Content-Type': 'application/json; charset=UTF-8'},
            data: JSON.stringify($scope.event)
        })
            .then(function (response) {
                console.log($scope.event);
            })
    }
    
});
