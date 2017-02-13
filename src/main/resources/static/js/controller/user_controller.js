
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

    };

    var parameter = JSON.stringify({email:$scope.email, password:$scope.password});
    var url = 'registration';
    console.log (parameter);
    $http.post(url, parameter).
    success(function(data, status, headers, config) {
        // this callback will be called asynchronously
        // when the response is available
        console.log(data);
    }).
    error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
    });

});