/**
 * AngularJS - Gifs controller.
 */
function hfrGifsController($scope, $http) {

    $scope.initialPageNumber = -1;
    $scope.page = null;

    $scope.storeImages = function (data) {
        $("html, body").animate({ scrollTop: 0 }, "slow");
        $scope.page = data;
    }

    $scope.loadImages = function () {
        $http.get("topics/" + $scope.topicId + "/gifs")
            .success(function (data) {
                $scope.storeImages(data);
                $scope.initialPageNumber = $scope.page.pageNumber;
            });
    };

    $scope.loadMore = function(pageNumber) {
        $http.get("topics/" + $scope.topicId + "/page/" + pageNumber)
            .success(function (data) {
                $scope.storeImages(data);
            });
    }
    $scope.loadPageUp = function () {
        var pageNumber = $scope.page.pageNumber + 1;
        if (pageNumber <= $scope.initialPageNumber) {
            $scope.loadMore(pageNumber);
        }
    }
    $scope.loadPageDown = function () {
        var pageNumber = $scope.page.pageNumber - 1;
        if (pageNumber !== 1) {
            $scope.loadMore(pageNumber);
        }
    }

    //-------------------
    // on controller load.
    //-------------------
    $http.get('topics').success(function (data) {
        $scope.topics = data;
        $scope.topicId = data[0].id; // to initialize default topic in select.

        $scope.loadImages();
    });

}



