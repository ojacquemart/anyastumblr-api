/**
 * AngularJS - controllers.
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

    /**
     * On load...
     */
    $scope.loadTweets();

    /**
     * On destroy, close active stream.
     */
    $scope.$on('$destroy', function(){
        $scope.closeIfStreamActive();
    });
}

function TumblrStatsController($scope, $http) {
    // TODO...
}

function TumblrController($scope, $http) {

    $scope.currentSiteIndex = 0;
    $scope.page = null;
    $scope.lastPageInfos = null;

    $scope.updateCurrentSiteIndex = function() {

        function searchSiteIndex() {
            var sitesSize = $scope.sites.length;

            for (var i = 0; i < sitesSize; i++) {
                var site = $scope.sites[i];
                if (site.id == $scope.siteId) {
                    return i;
                }
            }

            return 0;
        }

        $scope.currentSiteIndex = searchSiteIndex();
    };

    $scope.storeImages = function (data) {
        $("html, body").animate({ scrollTop: 0 }, "slow");
        $scope.page = data;
    };

    $scope.loadLastPageInfos = function() {
        $http.get("api/tumblr/" + $scope.siteId + "/lastPageInfos").success(function (data) {
            // FIXME: lastPageInfos must return a true null.
            if (data == "null") {
                $scope.lastPageInfos = null;
            } else {
                $scope.lastPageInfos = data;
            }
        });
    };

    $scope.loadImages = function () {
        $http.get("api/tumblr/" + $scope.siteId).success(function (data) {
            $("#topics-select").blur();
            $scope.storeImages(data);

            // Reset last page infos to reinit binding.
            $scope.lastPageInfos = null;
            $scope.loadLastPageInfos();

            $scope.updateCurrentSiteIndex();
            //$scope.getTweets();
        });
    };

    $scope.refreshPage = function() {
        $scope.loadImages();
        $("#error").hide();
    };

    $scope.loadPage = function(pageNumber) {
        $http.get("api/tumblr/" + $scope.siteId + "/page/" + pageNumber).success(function (data) {
            $scope.storeImages(data);
        });
    };

    $scope.loadNextPage = function () {
        var nextPageNumber = $scope.page.pageNumber + 1;
        var lastPageInfos = $scope.lastPageInfos;

        function canGoToNextPage() {
            if (lastPageInfos == null) {
                return true;
            }

            return nextPageNumber <= lastPageInfos.pageNumber;
        }

        if (canGoToNextPage()) {
            $scope.loadPage(nextPageNumber);
        }
    }
    $scope.loadPreviousPage = function () {
        var pageNumber = $scope.page.pageNumber - 1;
        if (pageNumber !== 0) {
            $scope.loadPage(pageNumber);
        }
    };

    $scope.loadSiteImages = function(siteIndex) {
        var nextIndex = $scope.currentSiteIndex + siteIndex;
        if (nextIndex >= 0 && nextIndex < $scope.sites.length) {
            $scope.currentSiteIndex = nextIndex;

            // to bind topic in select.
            $scope.siteId = $scope.sites[nextIndex].id;
            $scope.loadImages();
        }
    };

    /**
     * Keyboard navigation, FPS like and left/right arrows.
     * @param key the keycode.
     */
    $scope.handleKeypress = function(key) {
        // left = 37, right = 39, q = 81, d = 68, z = 90, s = 83, r = 82
        switch (key) {
            // Previous page = left | q
            case 37:
            case 81:
                $scope.loadPreviousPage();
                break;
            // Next page = right | d
            case 39:
            case 68:
                $scope.loadNextPage();
                break;
            // Previous site = up | z
            case 90:
                $scope.loadSiteImages(-1);
                break;
            // Next site = down | s
            case 83:
                $scope.loadSiteImages(1);
                break;
            // Refresh = r
            case 82:
                $scope.refreshPage();
                break;
        }
    };

    $scope.showModalShortcuts = function() {
        $("#tumblr-sites-modal-shortcuts").modal();
    };

    /**
     * On controller load...
     */

    $http.get('api/tumblr/sites').success(function (data) {
        $scope.sites = data;
        $scope.loadSiteImages(0);
    });

    // FIXME: see to use angularjs directive
    // Dont see how to put a directive on the body and use this controller.
    $(document).bind("keydown", function(e){
        $scope.handleKeypress(e.keyCode);
    });

    // Unbind keydown event on controller destroy.
    $scope.$on('$destroy', function(){
        $(document).unbind("keydown");
    });
}



