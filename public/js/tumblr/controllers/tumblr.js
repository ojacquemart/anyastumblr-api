'use strict';

/**
 * Tumblr Controller.
 */
function TumblrController($scope, $routeParams, $location, Tumblr, sitesNavigator, pagesNavigator, Animations) {

    $scope.sitesByType = null;

    $scope.page = null;
    $scope.site = null;

    $scope.loadImages = function() {
        $scope.page = Tumblr.get({ "id": sitesNavigator.getCurrentSiteId() });
    };

    $scope.loadImagesByPageNumber = function(pageNumber) {
        if (pageNumber !== -1) {
            var params = {
                "id":        sitesNavigator.getCurrentSiteId(),
                "pageParam": pageNumber
            }
            $scope.page = Tumblr.getPageByNumber(params, function () {
                Animations.toTop();
            });
        }
    };

    $scope.goToPreviousPage = function () {
        $scope.loadImagesByPageNumber(pagesNavigator.getPreviousPageNumber($scope.page));
    };

    $scope.goToNextPage = function () {
        $scope.loadImagesByPageNumber(pagesNavigator.getNextPageNumber($scope.page));
    };

    /**
     * Change site by index, used by the keyboard navigation.
     */
    $scope.loadSiteByIndex = function(index) {
        var nextSiteId = sitesNavigator.getSiteByIndex(index);
        if (nextSiteId != null) {
            $scope.$apply(function() {
                $location.path("/sites/" + nextSiteId);
            });
        }
    };

    $scope.refreshPage = function() {
        $scope.loadImages();
    };

    $scope.canDisplayLastPageInfos = function() {
        return pagesNavigator.hasLastPageInfos($scope.page);
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
                $scope.goToPreviousPage();
                break;
            // Next page = right | d
            case 39:
            case 68:
                $scope.goToNextPage();
                break;
            // Previous site = up | z
            case 90:
                $scope.loadSiteByIndex(-1);
                break;
            // Next site = down | s
            case 83:
                $scope.loadSiteByIndex(1);
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

    Tumblr.query(function (data) {
        $scope.sitesByType = data.sitesByType;
        $scope.site = sitesNavigator.init(data.sites, $routeParams.siteId);
        $scope.loadImages();
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
