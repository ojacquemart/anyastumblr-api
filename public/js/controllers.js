/**
 * AngularJS - Gifs controller.
 */
function hfrGifsController($scope, $http) {

    $scope.initialPageNumber = -1;
    $scope.page = null;
    $scope.pages = []; // To concat each page and its images.

    $scope.storeImages = function (data) {
        $("html, body").animate({ scrollTop: 0 }, "slow");
        $scope.page = data[0];
        $scope.pages = data;
    }

    $scope.loadImages = function () {
        $http.get("topics/" + $scope.topicId + "/gifs")
            .success(function (data) {
                $scope.storeImages(data);
                $scope.initialPageNumber = $scope.page.pageNumber;
            });
    };
//        var data = [{"page" : "Page 123", "images" : ["http://localhost:9000/assets/img/angularjs-logo.png","http://localhost:9000/assets/img/angularjs-logo.png","http://localhost:9000/assets/img/angularjs-logo.png"]}];
//        var data2 = [{"page" : "Page 456", "images" : ["http://localhost:9000/assets/img/play-logo.png","http://localhost:9000/assets/img/play-logo.png","http://localhost:9000/assets/img/play-logo.png"]}];
//        var data3 = [{"page" : "Page 789", "images" : ["http://localhost:9000/assets/img/scala-logo.png"]}];
//        $scope.pages = $scope.pages.concat(data);
//        $scope.pages = $scope.pages.concat(data2);
//        $scope.pages = $scope.pages.concat(data3);
//        console.log($scope.pages);

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



