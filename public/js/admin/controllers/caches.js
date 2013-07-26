'use strict';

function CachesController($scope, Caches) {

    $scope.removeAll = function () {
        Caches.removeAll();
    }

    $scope.remove = function (cacheKey) {
        Caches.remove({ "key": cacheKey });
    }

    // @OnLoad...
    $scope.cacheKeys = Caches.query();

}