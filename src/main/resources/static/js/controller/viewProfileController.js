'use strict';

var actimate = angular.module('actimate',['ngResource']);

actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);

actimate.controller('loadUserCtrl', function($scope, $http){

    $scope.user = null;

    $http.get('')
        .success(function (data) {
        $scope.user = data.user;
    })
});