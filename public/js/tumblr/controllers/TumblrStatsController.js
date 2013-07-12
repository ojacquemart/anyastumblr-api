'use strict';

/**
 * Tumblr Stats Controller.
 */
function TumblrStatsController($scope, $http) {

    $scope.nbDocuments = 0;

    $http.get("api/tumblr/sites/stats").success(function(data) {
        $scope.nbDocuments = data.count;
    });
}