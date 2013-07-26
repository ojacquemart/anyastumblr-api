"use strict";

angular.module('httpInterceptor', [])
    .config(function ($httpProvider) {
        $httpProvider.responseInterceptors.push('loadingHttpInterceptor');
        var spinnerFunction = function (data, headersGetter) {
            $('#loading').show();

            return data;
        };
        $httpProvider.defaults.transformRequest.push(spinnerFunction);
    })
    // register the interceptor as a service, intercepts ALL angular ajax http calls
    .factory('loadingHttpInterceptor', function ($q, $window) {
        return function (promise) {
            return promise.then(function (response) {
                $('#loading').hide();
                $('#error').hide();

                return response;

            }, function (response) {
                $('#error').show();
                $('#loading').hide();

                return $q.reject(response);
            });
        };
    });
