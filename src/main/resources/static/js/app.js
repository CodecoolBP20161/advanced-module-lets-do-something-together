'use strict';

var actimate = angular.module('actimate', [
    'ngRoute',
    'ngResource',
    'gm',
    'jcs-autoValidate'
])

    .config(['$httpProvider', '$routeProvider', '$locationProvider',
        function ($httpProvider, $routeProvider, $locationProvider) {
            $httpProvider.defaults.withCredentials = true;
            $httpProvider.defaults.headers['Content-Type'] = 'application/json; charset=UTF-8';
            $httpProvider.defaults.useXDomain = true;
            delete $httpProvider.defaults.headers.common['X-Requested-With'];
            $httpProvider.defaults.headers.post["Content-Type"] = "application/json";

            $locationProvider
                .html5Mode(true)
                .hashPrefix('!');

            $routeProvider
                .when('/', {
                    templateUrl: 'views/main.html',
                    controller: 'mainCtrl'
                })
                .when('/registration', {
                    templateUrl: 'views/registration.html',
                    controller: 'userCtrl'
                })
                .when('/login', {
                    templateUrl: 'views/login_form.html'
                    //controller: 'loginCtrl'
                })
                .when('/u/profile', {
                    templateUrl: 'views/profile.html',
                    controller: 'profileCtrl'
                })
                .when('/u/dashboard', {
                 template: 'views/user_main.html',
                 controller: 'profileCtrl'
                 })
                .when('/u/edit-profile', {
                    templateUrl: 'views/profile_form.html',
                    controller: 'profileCtrl'
                })
                .when('/u/create_event', {
                    templateUrl: 'views/create_event.html',
                    controller: 'saveEventCtrl'
                })
                .otherwise({
                    redirectTo: '/'
                })


        }])

    .controller('navCtrl', ['$scope', '$location', function($scope, $location) {
        $scope.currentPath = $location.path();
        console.log($location.path())
    }]);

/*
actimate.controller("mainCtrl", function ($scope, $http, $state, $stateParams) {
    // Expose $state and $stateParams to the <body> tag
    $scope.$state = $state;
    $scope.$stateParams = $stateParams;
});

actimate.service('mainService', function () {

});
*/


