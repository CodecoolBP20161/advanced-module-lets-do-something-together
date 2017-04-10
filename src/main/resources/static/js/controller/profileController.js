'use strict';



actimate.controller("profileCtrl", function ($scope, $http) {
        $scope.user = {};
        $scope.saveProfile = function () {
            $http({
                method: 'POST',
                url: '/u/edit-profile',
                headers: {'Content-Type': 'application/json; charset=UTF-8'},
                data: JSON.stringify($scope.user)
            })
                .then($scope.loadUserDetails = function ($scope, $http) {
                    $scope.user = null;
                    $http.get("/u/profile_data")
                        .then(function (response) {
                            $scope.user = response.data;
                        })
                }, function (error) {
                    console.log('Error: ', error)
                });
        };
    });