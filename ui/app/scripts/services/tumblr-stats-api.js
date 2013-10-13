'use strict';

angular.module('uiApp')
  .factory('tumblrStatsApi', function ($resource) {
        return $resource("/api/tumblr/stats", {}, { });
    });