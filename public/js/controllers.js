/**
 * AngularJS - Gifs controller.
 */
function siteGifsController($scope, $http) {

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
    }

    $scope.storeImages = function (data) {
        $("html, body").animate({ scrollTop: 0 }, "slow");
        $scope.page = data;
    }

    $scope.loadLastPageInfos = function() {
        $http.get("sites/" + $scope.siteId + "/lastPageInfos")
            .success(function (data) {
                $scope.lastPageInfos = data;
            });
    }

    $scope.loadImages = function () {
        $http.get("sites/" + $scope.siteId + "/gifs")
            .success(function (data) {
                $("#topics-select").blur();
                $scope.storeImages(data);

                // Reset last page infos to reinit binding.
                $scope.lastPageInfos = null;
                $scope.loadLastPageInfos();

                $scope.updateCurrentSiteIndex();
            });

    };

    $scope.refreshPage = function() {
        $scope.loadImages();
        $("#error").hide();
    };

    $scope.loadPage = function(pageNumber) {
        $http.get("sites/" + $scope.siteId + "/gifs/" + pageNumber)
            .success(function (data) {
                $scope.storeImages(data);
            });
    };

    $scope.loadNextPage = function () {
        var nextPageNumber = $scope.page.pageNumber + 1;
        var lastPageInfos = $scope.lastPageInfos;
        if (lastPageInfos != null && nextPageNumber <= lastPageInfos.pageNumber) {
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
    }

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
                $scope.siteId = $scope.sites[$scope.currentSiteIndex].id;
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
        $("#modal-shortcuts").modal();
    }

    //-------------------
    // on controller load.
    //-------------------
    $http.get('sites').success(function (data) {
        $scope.sites = data;
        $scope.loadSiteImages(0);
    });

}



