'use strict';

/**
 * Tumblr Stats Controller.
 */
function TumblrStatsController($scope, $http) {

    $scope.nbDocuments = 0;

    $http.get("api/tumblr/stats").success(function(data) {
        $scope.nbDocuments = data.count;
    });
}