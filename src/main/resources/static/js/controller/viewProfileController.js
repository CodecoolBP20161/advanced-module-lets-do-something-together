'use strict';

var actimate = angular.module("actimate", ['ngResource']);
actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
}]);

actimate.controller('loadUserCtrl', function ($scope, $http) {

    $scope.user = null;

    $scope.listofInterests = null;

    $http.get("/u/profile_data")
        .then(function (response) {
            $scope.user = response.data;
        })
});