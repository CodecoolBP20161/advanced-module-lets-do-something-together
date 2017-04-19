'use strict';

angular.module('actimate')

    .service('loggedUser', function ($http) {
        var $scope, loggedInUser;
        this.getUser = function () {
            $http.get('http://localhost:8080/login_form')
                .success(function (response) {
                    $scope.loggedInUser = response;
                    loggedInUser = $scope.loggedInUser;
                    return loggedInUser;
                }).error(function (response) {
                console.log(response);
            });
        };

    });