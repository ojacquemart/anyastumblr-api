'use strict';

angular.module('uiApp')
    .controller('AdminCachesCtrl', function ($scope, Caches) {
        $scope.removeAll = function () {
            Caches.removeAll();
        };

        $scope.remove = function ($http, cacheKey) {
            Caches.remove({ "key": cacheKey });
        };

        // @OnLoad...
        $scope.cacheKeys = Caches.query();

    });
