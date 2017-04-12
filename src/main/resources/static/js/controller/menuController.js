'use strict';

actimate.controller('menuCtrl', ['$rootScope', '$location', '$scope', function($rootScope, $location, $scope) {
    $scope.currentPath = $location.path();
    // console.log("Path: " + $location.path())

}]);
