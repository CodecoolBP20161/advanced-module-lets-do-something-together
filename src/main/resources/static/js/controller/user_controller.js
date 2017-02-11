
var helloApp = angular.module("helloApp", ['ngResource']);
helloApp.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
}]);
helloApp.controller("UserCtrl", function($scope, $http) {
    $scope.users = [

    ];
    $scope.addRow = function(){
        $scope.users.push({ 'email':$scope.email, 'password': $scope.password, 'password_again':$scope.password_again });
        $scope.email='';
        $scope.password='';
        $scope.password_again='';

        var data = 'email=' + $scope.email + '&password=' + $scope.password;
        $http.post('/registration', data )
            .success(function(data, status, headers, config) {
                $scope.message = data;
            })
            .error(function(data, status, headers, config) {
                alert( "failure message: " + JSON.stringify(data));
            });
        // Making the fields empty
        //
        $scope.email='';
        $scope.password='';
        $scope.password_again='';
    };
    });