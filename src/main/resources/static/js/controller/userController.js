'use strict';


actimate.controller("userCtrl", ['$scope', '$http', '$location', function ($scope, $http, $location) {


    $scope.user = {};
    $scope.errorConfirm = false;

    $scope.addUser = function (valid) {
        if (valid) {
            $http({
                method: 'POST',
                url: '/registration',
                withCredentials: true,
                headers: {'Content-Type': 'application/json; charset=UTF-8', 'Accept': 'text/plain'},
                data: angular.toJson($scope.user)
            })
                .then(function successCallback (response) {
                    if (response.data == "fail") {
                        $(".email-error-message").text("Email address is already in use").addClass("alert alert-danger alert-dismissable").fadeIn();
                        addError($("#emailDiv"));
                    } else {
                        $location.path('/');
                        console.log('saved')
                    }
                    console.log(response);
                }, function errorCallback (response) {
                    alert("failure message: " + angular.toJson($scope.user));
                });
        }
        // Making the fields empty
        $scope.user = null;
        };
    }]);