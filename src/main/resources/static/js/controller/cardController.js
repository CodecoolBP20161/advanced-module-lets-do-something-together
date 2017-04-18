'use strict';

var actimate = angular.module("actimate", ['ngResource']);

actimate.config(['$httpProvider', '$qProvider', function ($httpProvider, $qProvider) {
    $httpProvider.defaults.useXDomain = true;
    $qProvider.errorOnUnhandledRejections(false);
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);


//http://www.concretepage.com/angular-js/angularjs-http-get-example
actimate.controller('cardCtrl', function ($scope, $http) {

    $scope.events = null;

    $http.get("/u/events")
        .success(function (response) {
            $scope.events = response;
            console.log("status:" + status);
        })
});