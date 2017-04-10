'use strict';

var actimate = angular.module("actimate", ['ngResource']);

actimate.controller('contactCtrl', function ($scope, $http) {
    $scope.contact = {};

    $scope.sendContact = function () {
        $http({
            method: 'POST',
            url: '/contact',
            headers: {'Content-Type': 'application/json; charset=UTF-8'},
            data: JSON.stringify($scope.contact)
        })
            .then(function () {
                window.location.href = "/";
                $scope.contact = null;
            })
    };

});