'use strict';

var actimate = angular.module("actimate", ['ngResource']);

actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);


//http://www.concretepage.com/angular-js/angularjs-http-get-example
actimate.controller('cardCtrl', function ($scope, $http) {

    $scope.events = null;

    $http.get("/u/events")
        .success(function (data, status) {
            $scope.events = data;
            console.log("status:" + status);
        }).error(function (data, status) {
            console.error('Error occured: ', data, status);
    }).finally(function () {
        console.log('Finished');
    });
});