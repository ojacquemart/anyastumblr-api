'use strict';

var module = angular.module('uiApp')
module.controller('TumblrStatsCtrl', function ($scope, tumblrStatsApi) {
    $scope.stats = tumblrStatsApi.get();
});
