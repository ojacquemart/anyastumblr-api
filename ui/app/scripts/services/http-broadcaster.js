angular.module('httpInterceptor', [])
    .factory('httpBroadcaster', function ($q, $rootScope, $log) {

        var nbLoadings = 0;

        function success(response) {
            if ((--nbLoadings) === 0) {
                $rootScope.$broadcast("loader_hide");
            }
            return response;
        }

        function error(response) {
            if ((--nbLoadings) === 0) {
                $rootScope.$broadcast("loader_hide");
            }

            return $q.reject(response);
        }

        return function (promise) {
            nbLoadings++;
            $rootScope.$broadcast("loader_show");

            return promise.then(success, error)
        }
    });

/**
 When will use angularjs > 1.1.X
 .factory('httpBroadcaster', function ($q, $rootScope, $log) {

 var nbLoadings = 0;

 return {
            request: function (config) {
                nbLoadings++;
                $rootScope.$broadcast("loader_show");
                return config || $q.when(config)

            },
            response: function (response) {
                if ((--nbLoadings) === 0) {
                    $rootScope.$broadcast("loader_hide");
                }

                return response || $q.when(response);

            },
            responseError: function (response) {
                if (!(--nbLoadings)) {
                    $rootScope.$broadcast("loader_hide");
                }

                return $q.reject(response);
            }
        };
 })
 .config(function ($httpProvider) {
 $httpProvider.interceptors.push('httpBroadcaster');
 });
 */