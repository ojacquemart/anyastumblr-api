'use strict';

/**
 * Tweets Controller.
 */
function TweetsController($scope, $http) {
    $scope.query = "java";
    $scope.tweets = null;

    $scope.stream = null;
    $scope.nbNewTweets = 0;

    $scope.espaceQuery = function() {
        return encodeURIComponent($scope.query);
    }

    $scope.loadTweets = function () {
        $http.get( "api/tweets/" + $scope.espaceQuery()).success(function (data) {
            $scope.tweets = data;
            $scope.openStream();
        });
    }

    $scope.closeIfStreamActive = function() {
        if ($scope.stream != null) {
            $scope.stream.close();
        }
    }

    $scope.openStream = function() {
        $scope.closeIfStreamActive();

        $scope.nbNewTweets = 0;
        $scope.stream = new EventSource("api/tweets/stream/" + $scope.espaceQuery());
        $($scope.stream).on('message', function(e) {
            var json = JSON.parse(e.originalEvent.data);

            $scope.$apply(function () {
                $scope.nbNewTweets = parseInt(json.recents);
                if ($scope.nbNewTweets == 0) {
                    // Null when no new tweets to use ng-show.
                    $scope.nbNewTweets = null;
                }
                if ($scope.nbNewTweets == 100) {
                    // Close current stream when reaching 100 new tweets.
                    e.target.close();
                }
            });
        })
    }

    $scope.loadTweets();

    /**
     * On destroy, close active stream.
     */
    $scope.$on('$destroy', function(){
        $scope.closeIfStreamActive();
    });

}
