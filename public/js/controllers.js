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
                $("#topics-select").blur();
                $scope.storeImages(data);
                $scope.initialPageNumber = $scope.page.offset;
        });

    };

    $scope.refreshPage = function() {
        $scope.loadImages();
        $("#error").hide();
    };

    $scope.loadPage = function(pageNumber) {
        $http.get("topics/" + $scope.topicId + "/page/" + pageNumber)
            .success(function (data) {
                $scope.storeImages(data);
            });
    };

    $scope.loadNextPage = function () {
        $scope.loadPage($scope.page.offset + 1);
    }
    $scope.loadPreviousPage = function () {
        var pageNumber = $scope.page.offset - 1;
        if (pageNumber !== 1) {
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
    $http.get('topics').success(function (data) {
        $scope.topics = data;
        $scope.topicId = data[0].id; // to initialize default topic in select.

        $scope.loadImages();
    });

}



