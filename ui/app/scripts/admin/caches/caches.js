"use stricts";

angular.module('admin.caches', [])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/admin/caches', {
                controller: "CachesCtrl",
                templateUrl: "scripts/admin/caches/caches.tpl.html",
                access: "admin"
            })
    }])
    .factory("Caches", function (secureResource) {
        return secureResource("/api/tumblr/admin/caches/:key", { key: "@key"}, {
            'removeAll': { method: 'DELETE' },
            'remove': { method: 'DELETE' }
        });
    })
    .controller('CachesCtrl', function ($scope, Caches) {
        $scope.removeAll = function () {
            Caches.removeAll();
        };

        $scope.remove = function ($http, cacheKey) {
            Caches.remove({ "key": cacheKey });
        };

        // @OnLoad...
        $scope.cacheKeys = Caches.query();

    })
;