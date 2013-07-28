'use strict';

/**
 * Tumblr Stats Controller.
 */
function TumblrStatsController($scope, TumblrStats) {

    $scope.stats = TumblrStats.get();
}