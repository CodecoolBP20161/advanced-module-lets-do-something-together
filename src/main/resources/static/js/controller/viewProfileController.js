'use strict';

angular.module('actimate', []);

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