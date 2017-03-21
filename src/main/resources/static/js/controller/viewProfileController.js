'use strict';

var actimate = angular.module('actimate', ['ngResource']);

actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);

actimate.controller('loadUserCtrl', function ($scope, $http) {

    $scope.user = null;
    $scope.listofInterests = null;

    $http.get("/u/profile_data")
        .then(function (response) {
            $scope.user = response.data.profile;
        })
});