'use strict';



actimate.controller("loadUserCtrl", function ($scope, $http) {

        $scope.user = null;

        $scope.listofInterests = null;

        $http.get("/u/profile_data")
            .then(function (response) {
                $scope.user = response.data.profile;
                updateInterests(response.data.profile.interestList);
            })
    });