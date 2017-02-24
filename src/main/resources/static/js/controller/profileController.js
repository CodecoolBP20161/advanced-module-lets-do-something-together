'use strict';

var actimate = angular.module('actimate',['ngResource']);

actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
}]);

actimate.controller("profileController", function ($scope, $http) {
    $scope.user = {};

    $scope.saveProfile = function () {
        $http({
            method : 'POST',
            url: '/profile',
            headers: {'Content-Type': 'application/json; charset=UTF-8'},
            data: angular.toJson($scope.user)
        })
            .success(function (response) {
                console.log($scope.user);
            })
            .error(function (response) {
                alert("failure message: " + angular.toJson($scope.user));
            });
    };

});