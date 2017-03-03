'use strict';

angular.module('actimate')
    .controller('viewProfileController', function ($scope) {
        $scope.currentTab = 'bio';

        $scope.changeTab = function (tab){
            $scope.currentTab = tab;
        };
        alert('Hello')
    });