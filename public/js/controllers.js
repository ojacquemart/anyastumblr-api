/**
 * AngularJS - Gifs controller.
 */
function siteGifsController($scope, $http) {

    $scope.page = null;

    $scope.storeImages = function (data) {
        $("html, body").animate({ scrollTop: 0 }, "slow");
        $scope.page = data;
    }

    $scope.loadImages = function () {
        $http.get("sites/" + $scope.siteId + "/gifs")
            .success(function (data) {
                $("#topics-select").blur();
                $scope.storeImages(data);
            });

    };

    $scope.refreshPage = function() {
        $scope.loadImages();
        $("#error").hide();
    };

    $scope.loadPage = function(pageNumber) {
        $http.get("sites/" + $scope.siteId + "/page/" + pageNumber)
            .success(function (data) {
                $scope.storeImages(data);
            });
    };

    $scope.loadNextPage = function () {
        $scope.loadPage($scope.page.pageNumber + 1);
    }
    $scope.loadPreviousPage = function () {
        var pageNumber = $scope.page.pageNumber - 1;
        if (pageNumber !== 0) {
            $scope.loadPage(pageNumber);
        }
    };

    $scope.handleKeypress = function(key) {
        // left = 37, right = 39
        switch (key) {
            case 37: // Previous page = left
                $scope.loadPreviousPage();
                break;
            case 39: // Next page = right
                $scope.loadNextPage();
                break;
        }
    };

    //-------------------
    // on controller load.
    //-------------------
    $http.get('sites').success(function (data) {
        $scope.sites = data;
        $scope.siteId = data[0].id; // to initialize default topic in select.

        $scope.loadImages();
    });

}



