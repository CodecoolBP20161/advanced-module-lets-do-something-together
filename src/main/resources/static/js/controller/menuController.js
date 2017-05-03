'use strict';

actimate.controller('menuCtrl', ['$rootScope', '$location', '$scope', '$http', function($rootScope, $location, $scope, $http) {

    $rootScope.$on("$locationChangeStart", function(event, next, current) {
        $scope.currentPath = $location.path();
    });

    $scope.logout = function() {

        $http.get("/logout")
            .then(function (response) {
                window.location.href = "/";
                console.log("kiskutyafasza logout");
            });
    }
}]);
