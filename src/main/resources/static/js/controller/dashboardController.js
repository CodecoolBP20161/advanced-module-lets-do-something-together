'use strict';

actimate.controller("dashboardCtrl", function ($scope, $http) {

    $scope.getEvents= function () {

        $scope.events = null;

        $http.get("/u/events")
            .then(function (response) {
                $scope.events = response.data.events;
                console.log($scope.events);
            })
    };

    $scope.getEvents();
});