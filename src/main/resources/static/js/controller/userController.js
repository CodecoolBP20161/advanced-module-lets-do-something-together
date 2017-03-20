'use strict';

var actimate = angular.module("actimate", ['ngResource']);

actimate.controller("UserCtrl", function ($scope, $http) {
    $scope.user = {};
    $scope.errorConfirm = false;

    $scope.addUser = function (valid) {
        if (valid) {
            $http({
                method: 'POST',
                url: '/registration',
                withCredentials: true,
                headers: {'Content-Type': 'application/json; charset=UTF-8'},
                data: angular.toJson($scope.user)
            })
                .success(function (response) {
                    if (response == "fail") {
                        $(".email-error-message").text("Email address is already in use").addClass("alert alert-danger alert-dismissable").fadeIn();
                        addError($("#emailDiv"));
                    } else {
                        onSuccess();
                    }
                    console.log(response);
                })
                .error(function (response) {
                    alert("failure message: " + angular.toJson($scope.user));
                });
        }
        // Making the fields empty
        $scope.user = null;
    };
});