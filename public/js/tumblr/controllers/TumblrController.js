'use strict';

/**
 * Tumblr Controller.
 */
function TumblrController($scope, Tumblr) {

    $scope.currentSiteIndex = 0;
    $scope.page = null;
    $scope.pageTotal = null;

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
    };

    $scope.getTotalPage = function() {
        Tumblr.getTotalPage({ id: $scope.siteId }, function (data) {
            // FIXME: pageTotal must return a true null.
            if (data == "null") {
                $scope.pageTotal = null;
            } else {
                $scope.pageTotal = data;
            }
        });
    };

    $scope.setImages = function (data) {
        $("html, body").animate({ scrollTop: 0 }, "slow");
        $scope.page = data;
    };

    $scope.getImagesFromSite = function () {
        Tumblr.get({ "id": $scope.siteId }, function (data) {
            $("#topics-select").blur();
            $scope.setImages(data);

            // Reset last page infos to reinit binding.
            $scope.pageTotal = null;
            $scope.getTotalPage();

            $scope.setCurrentSiteIndex();
        });
    };

    $scope.getPageByNumber = function(pageNumber) {
        Tumblr.getPageByNumber({ "id" : $scope.siteId, "pageParam" : pageNumber }, function (data) {
            $scope.setImages(data);
        });
    };

    $scope.getNextPage = function () {
        var nextPageNumber = $scope.page.pageNumber + 1;
        var lastPageLink = $scope.pageTotal;

        function canGoToNextPage() {
            if (lastPageLink == null) {
                return true;
            }

            return nextPageNumber <= lastPageLink.pageNumber;
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
            $scope.currentSiteIndex = nextIndex;

            // to bind topic in select.
            $scope.siteId = $scope.sites[nextIndex].id;
            $scope.getImagesFromSite();
        }
    };

    $scope.refreshPage = function() {
        $scope.getImagesFromSite();
        $("#error").hide();
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

    $scope.showModalShortcuts = function() {
        $("#tumblr-sites-modal-shortcuts").modal();
    };

    /**
     * @OnLoad...
     */

    Tumblr.query(function (data) {
        $scope.sites = data;
        $scope.checkSiteIndexAndGetImages(0);
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



