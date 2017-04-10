describe('Unit: UserCtrl', function () {


    var $scope ={};
    $scope.user = {
        email: 'lorszil@gmail.com',
        password: 'Lor1234',
        passwordAgain: 'Lor1234'
    };
    var userAdminList = [
        {
        email: 'lorszil@gmail.com',
        password: 'Lor1234',
        passwordAgain: 'Lor1234'
        },
        {
        email: 'admin@admin.com',
        password: '1234',
        passwordAgain: '1234'
        }
    ];
    var user1 = [
        {
            email: 'lorszil@gmail.com',
            password: 'Lor1234',
            passwordAgain: 'Lor1234'
        }
    ];

    var $httpBackend, $http, $controller, userResource, $httpProvider, authRequestHandler;
    var location, route, rootScope, UserCtrl, myServiceMock, $rootScope, $scopeProvider;

    beforeEach(module('actimate'));

    beforeEach(angular.mock.module('actimate'));

    beforeEach(inject(function ($injector) {
        // Set up the mock http service responses
        $httpBackend = $injector.get('$httpBackend');
        authRequestHandler = $httpBackend.when('GET', '/')
            .respond({user: 'userX'}, {'A-Token': 'xxx'});
        $rootScope = $injector.get('$rootScope');
        $controller = $injector.get('$controller');
        //$scopeProvider = $injector.get('$scopeProvider');

    }));

    beforeEach(inject(function($scope) {

        $controller = function() {
            return $controller('UserCtrl', {'$scope': $scope});
        };

        $scope.config = {
            serverUrl: "https://localhost:8080/registration/",
            serverVersion: "test",
            title: "Test Server Reference"
        };

    }));

    beforeEach(inject(function ($controller) {
        UserCtrl = $controller('UserCtrl');
    }));

    afterEach(function () {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });

    it('should exist', function ($scope) {
        expect($scope.user).toBeDefined();
    });

    it('should exist', function($scope) {
        expect($scope.user.addUser()).toBeDefined();
    });

    it('should demonstrate using when (200 status)', inject(function($httpBackend) {

        /* Code Under Test */
        $http.get('http://localhost/foo')
            .success(function(data, status, headers, config) {
                $scope.valid = true;
                $scope.response = data;
            })
            .error(function(data, status, headers, config) {
                $scope.valid = false;
            });
        /* End */

        $httpBackend
            .when('GET', 'http://localhost/registration')
            .respond(200, { foo: 'bar' });

        $httpBackend.flush();

        expect($scope.valid).toBe(true);
        expect($scope.response).toEqual({ foo: 'bar' });
    }));

    it('should have correct Content-Type header on GET request', function() {
        $httpBackend.expectGET('/registration', function(headers) {
            return headers['Content-Type'] === 'application/x-www-form-urlencoded; charset=UTF-8';
        }).respond(200, {});
        $httpBackend.get('/registration');
        $httpBackend.flush();
    });

    it('should call getUser with username', inject(function () {
        $httpBackend.expectGET('/registration')
            .respond(['$scope.user']);

        var result = mockUserResource.addUser('test@test.hu', 'As1234');

        $httpBackend.flush();

        expect(result[0].email).toEqual('test@test.hu');
        expect(result[1].password).toEqual('As1234');
    }));

    it('should post', inject(function () {
        $httpBackend.expectPOST('/registration/user')
            .respond([{
                email: 'test@test.hu',
                password: 'As12345'
            }]);

        var result = mockUserResource.addUser('test@test.hu', 'As12345');

        $httpBackend.flush();

        expect(result[0].email).toEqual('test@test.hu');
        expect(result[1].password).toEqual('As1234');

    }));

    it('should exist', function () {
       expect($scope.user).toBeDefined();
    });

    it('send User to the backend', inject(function (_user_) {
        var controller = $controller('UserCtrl', { $scope: $scope });
        $httpBackend.post('http://localhost/registration')
            .success(function(){
                $scope.valid = true;
                $scope.response = _user_;
            })
            .error(function(){
                $scope.valid = false;
            });
        $httpBackend
            .except('POST', 'http://localhost/registration')
            .respond(200, { "email":"alma", "password":"körte" });
        console.log('ok');
        expect($scope.valid).toBe(true);
    }));

    describe('Users factory', function() {
        var user;

        // Before each test set our injected Users factory (_Users_) to our local Users variable
        beforeEach(inject(function(_user_) {
            $scope.user = _user_;
        }));

        // A simple test to verify the Users factory exists
        it('should exist', function() {
            expect($scope.user).toBeDefined();
        });
    });


        it('Working?', function () {
            expect(UserCtrl.mode).toBe(true)
        });
        it('it should added', function () {

            UserCtrl.addUser(validateEmailFormat('lorszil@gmail.com'));
            expect(UserCtrl.email).toBe('valid');
            UserCtrl.addUser(($scope.user.email).$valid);
            expect(UserCtrl.email)

        });


    describe('Unit: UserCtrl', function () {

        var $httpBackend, jsonData, $rootScope;
        var url = '/registration';
        var data = {
            email: 'lorszil@gmail.com',
            password: 'Lor1234',
            passwordAgain: 'Lor1234'
        };

        it('should pleaseeee', function () {

            var userTest = data;
            $httpBackend.when('POST', url, function (user) {

                    jsonData = JSON.parse(user);
                    expect(jsonData.email).toBe(userTest.email);
                    expect(jsonData.password).toBe(userTest.password);
                    expect(jsonData.passwordAgain).toBe(userTest.passwordAgain);
                    return true;
                }
            ).respond(200, userTest);

            Feed.addUser(userTest).then(function(d) {
                expect(d).toBeTruthy();
            });

            $httpBackend.flush();
        });
    });
});
