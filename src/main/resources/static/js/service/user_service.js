
var helloApp = angular.module("helloApp", ['ngResource']);

helloApp.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
}]);

$scope.addRow = function($scope){
    $scope.users.push({ 'email':$scope.email, 'password': $scope.password, 'password_again':$scope.password_again });
    // Writing it to the server
    //
    var data = 'email=' + $scope.email + '&password=' + $scope.password;
    $http.post('/registration', data )
        .success(function(data, status, headers, config) {
            $scope.message = data;
        })
        .error(function(data, status, headers, config) {
            alert( "failure message: " + JSON.stringify({data: data}));
        });
    // Making the fields empty
    //
    $scope.email='';
    $scope.password='';
    $scope.password_again='';
};