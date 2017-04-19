'use strict';

describe('App.js Test', function () {

    var $httpBackend, $controller, $rootScope, authRequestHandler, urlEncodedParams;

    beforeEach(module('actimate'));

    beforeEach(inject(function ($injector) {
        // Set up the mock http service responses
        $httpBackend = $injector.get('$httpBackend');
        authRequestHandler = $httpBackend.when('GET', '/')
            .respond({user: 'userX'}, {'A-Token': 'xxx'});

        $rootScope = $injector.get('$rootScope');
        $controller = $injector.get('$controller');

    }));

    beforeEach(inject(function ($injector) {
        // Set up the mock http service responses
        $httpBackend = $injector.get('$httpBackend');
        authRequestHandler = $httpBackend.when('GET', '/')
            .respond({user: 'userX'}, {'A-Token': 'xxx'});

        $rootScope = $injector.get('$rootScope');
        $controller = $injector.get('$controller');

    }));

    afterEach(inject(function ($httpBackend) {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    }));

    it('httpBackend true', function () {
        $httpBackend = true;
    });

    it('should have true useXDomain and withCredentiels', inject(function () {
        $httpBackend.withCredentials = true;
        $httpBackend.useXDomain = true;
    }));

    it('should have correct Content-Type header on GET request', function() {
        $httpBackend.when('GET', 'oauth/token', urlEncodedParams, function(headers) {
            return headers['Content-Type'] === 'application/json; charset=UTF-8';
        }).respond(200, {});
    });

    it('loged in test', function () {

        var loggedUser = {email: 'lorszil@gmail.com', password: 'Lor1234'};

        $httpBackend.when('GET', '/login', loggedUser, function (headers) {
            expect(headers['Content-Type']).toBe('application/json');
            // MUST return boolean
            return headers['Content-Type'] === 'application/json';
        }).respond();

    })
});