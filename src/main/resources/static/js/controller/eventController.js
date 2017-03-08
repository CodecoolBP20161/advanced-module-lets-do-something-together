'use strict';

var actimate = angular.module('actimate', ['ngResource']);
actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);

actimate.controller('saveEventCtrl', function ($scope, $http) {
   $scope.name = null;
    $scope.location = null;
    $scope.date = null;
    $scope.participants = null;
    $scope.interest = null;
    $scope.description = null;
    
    $scope.postdata = function (name, location, date, participants, interest, description) {
        var data = {
            name: name,
            location: location,
            date: date,
            participants: participants,
            interest: interest,
            description: description
        };

        $http.post('/u/save-event', JSON.stringify(data))
            .then(function (response) {
                if(response.data)
                    $scope.message = 'Post is successful!';
        }, function (response) {
                $scope.message = 'Service not exists';
                $scope.statusVal = response.status;
                $scope.statusText = response.statusText;
                $scope.headers = response.headers();
            });
    };
});

/*
actimate.controller('saveEventCtrl', function ($scope, $http) {
    $scope.event = {};
    $scope.saveEvent = function() {
        $http({
            method: 'POST',
            url: '',
            headers: {'Content-Type': 'application/json; charset=UTF-8'},
            data: JSON.stringify($scope.event)
        })
            .then(function (response) {
                console.log($scope.event);
            })
    }
    
});*/
