'use strict';

actimate.controller('mainCtrl', ['$rootScope', '$location', '$scope', function($rootScope, $location, $scope) {
        $scope.currentPath = $location.path();
        console.log($location.path())

    }]);