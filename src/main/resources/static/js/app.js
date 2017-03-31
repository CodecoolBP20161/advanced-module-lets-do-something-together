'use strict';

window.actimate = angular.module('actimate', [
    'ngRoute',
    'ngResource',
    'gm',
    'jcs-autoValidate'
]);


actimate.config(['$httpProvider',
    function ($httpProvider) {
        $httpProvider.defaults.withCredentials = true;
        $httpProvider.defaults.headers['Content-Type'] = 'application/json; charset=UTF-8';
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];

    }]);


/*actimate.controller("MainCtrl", function ($scope, $http, $state, $stateParams) {
    // Expose $state and $stateParams to the <body> tag
    $scope.$state = $state;
    $scope.$stateParams = $stateParams;
});

actimate.service('MainService', function () {

});*/


