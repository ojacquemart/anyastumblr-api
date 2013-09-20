'use strict';

/**
 * Tumblr Controller.
 */
function TumblrController($scope, $routeParams, $location, Tumblr, Animations) {

    $scope.currentSiteIndex = 0;
    $scope.page = null;
    $scope.currentSite = null;

    $scope.setCurrentSiteIndex = function() {

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
        $scope.currentSite = $scope.sites[$scope.currentSiteIndex];
    };

    $scope.hasNotEmptyTotalPage = function() {
        return $scope.page != null
            && $scope.page.linkLastPage != null
            && $scope.page.linkLastPage["url"] != null;
    }

    $scope.getImagesFromSite = function() {
        $scope.page = Tumblr.get({ "id": $scope.siteId }, function () {
            $("#sites-select").blur();

            $scope.setCurrentSiteIndex();
        });

    };

    $scope.getPageByNumber = function(pageNumber) {
        Tumblr.getPageByNumber({ "id" : $scope.siteId, "pageParam" : pageNumber }, function (data) {
            Animations.toTop();

            $scope.page = data;
        });
    };

    $scope.getNextPage = function () {
        var nextPageNumber = $scope.page.pageNumber + 1;

        function canGoToNextPage() {
            if (!$scope.hasNotEmptyTotalPage()) {
                return true;
            }

            return nextPageNumber <= $scope.page.linkLastPage.pageNumber;
        }

        if (canGoToNextPage()) {
            $scope.getPageByNumber(nextPageNumber);
        }
    }

    $scope.getPreviousPage = function () {
        var pageNumber = $scope.page.pageNumber - 1;
        if (pageNumber !== 0) {
            $scope.getPageByNumber(pageNumber);
        }
    };

    $scope.checkSiteIndexAndGetImages = function(siteIndex) {
        var nextIndex = $scope.currentSiteIndex + siteIndex;
        if (nextIndex >= 0 && nextIndex < $scope.sites.length) {
            var nextSiteId = $scope.sites[nextIndex].id;

            $scope.$apply(function() {
                $location.path("/sites/" + nextSiteId);
            });
        }
    };

    $scope.refreshPage = function() {
        $scope.getImagesFromSite();
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
                $scope.getPreviousPage();
                break;
            // Next page = right | d
            case 39:
            case 68:
                $scope.getNextPage();
                break;
            // Previous site = up | z
            case 90:
                $scope.checkSiteIndexAndGetImages(-1);
                break;
            // Next site = down | s
            case 83:
                $scope.checkSiteIndexAndGetImages(1);
                break;
            // Refresh = r
            case 82:
                $scope.refreshPage();
                break;
        }
    };

    /**
     * @OnLoad...
     */

    $scope.initDefaultImages = function() {
        $scope.siteId = $routeParams.siteId;
        if ($scope.siteId == null) {
            $scope.siteId = $scope.sites[0].id;
        }

        $scope.setCurrentSiteIndex();
        $scope.getImagesFromSite();
    }

    Tumblr.query(function (data) {
        $scope.sitesByType = data.sitesByType;
        $scope.sites = data.sites;

        $scope.initDefaultImages();
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
