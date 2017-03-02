'use strict';

var actimate = angular.module('actimate',['ngResource']);

actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
}]);

actimate.controller("profileController", function ($scope, $http, $location) {
    $scope.user = {};

    $scope.saveProfile = function () {
            $http({
                method: 'POST',
                url: '/edit-profile',
                headers: {'Content-Type': 'application/json; charset=UTF-8'},
                data: JSON.stringify($scope.user)
            })
                .then(function (response) {
                    console.log('Success: ', $scope.user);

                }, function (error) {
                    console.log('Error: ' , error)
                })




    };

});