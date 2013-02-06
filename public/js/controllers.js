/**
 * AngularJS - Gifs controller.
 */
function hfrGifsController($scope, $http) {

    var clip = new ZeroClipboard($("#button-paste-image-src"));

    $scope.imageSrcFocus = "";
    $scope.currentPageNumber = 0;
    $scope.pages = [];

    $scope.concatPages = function (data) {
        var page = data[0];
        $scope.currentPageNumber = page.pageNumber;
        $scope.pages = $scope.pages.concat(data);
    }

    $scope.loadImages = function () {
//        var data = [{"page" : "Page 123", "images" : ["http://localhost:9000/assets/img/angularjs-logo.png","http://localhost:9000/assets/img/angularjs-logo.png","http://localhost:9000/assets/img/angularjs-logo.png"]}];
//        var data2 = [{"page" : "Page 456", "images" : ["http://localhost:9000/assets/img/play-logo.png","http://localhost:9000/assets/img/play-logo.png","http://localhost:9000/assets/img/play-logo.png"]}];
//        var data3 = [{"page" : "Page 789", "images" : ["http://localhost:9000/assets/img/scala-logo.png"]}];

//        $scope.pages = $scope.pages.concat(data);
//        $scope.pages = $scope.pages.concat(data2);
//        $scope.pages = $scope.pages.concat(data3);
//        console.log($scope.pages);
        $http.get("topics/" + $scope.topicId + "/gifs")
            .success(function (data) {
                $scope.pages = [];
                $scope.concatPages(data);
            });
    };

    // on controller load.
    $http.get('topics').success(function (data) {
        $scope.topics = data;
        $scope.topicId = data[0].id; // to initialize default topic in select.

        $scope.loadImages();
    });

    $scope.loadPreviousPage = function () {
        var pageNumber = $scope.currentPageNumber - 1;
        if (pageNumber !== 1) {
            $http.get("topics/" + $scope.topicId + "/page/" + pageNumber)
                .success(function (data) {
                    $scope.concatPages(data);
                });
        }
    }

    $scope.updateClipboardInput = function (image) {
        $scope.imageSrcFocus = image;
    }

}



