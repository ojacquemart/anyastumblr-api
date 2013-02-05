/**
 * AngularJS - Gifs controller.
 */
function hfrGifsController($scope, $http) {

    var clip = new ZeroClipboard($("#button-paste-image-src"));

    $scope.imageSrcFocus = "";
    $scope.images = [];

    $scope.concatImages = function (data) {
        $scope.images = $scope.images.concat(data.images)
        $scope.gifs = data;
        $scope.gifs.images = $scope.images;
    }

    $scope.loadImages = function () {
        $scope.images = [];
//        var data =  {"rootUrl":"http://forum.hardware.fr",
//                        "currentPage":7703,"previousPage":7702,"nextPage":-1,
//            "images":["http://localhost:9000/assets/img/angularjs-logo.png"]};
//        $scope.concatImages(data);
        $http.get("topics/" + $scope.topicId + "/gifs")
            .success(function (data) {
                $scope.images = [];
                $scope.concatImages(data);
            });
    };

    // on controller load.
    $http.get('topics').success(function (data) {
        $scope.topics = data;
        $scope.topicId = data[0].id; // to initialize default topic in select.

        $scope.loadImages();
    });

    $scope.loadPreviousPage = function () {
        var pageNumber = $scope.gifs.pageNumber - 1;
        if (pageNumber !== 1) {
            $http.get("topics/" + $scope.topicId + "/page/" + pageNumber)
                .success(function (data) {
                    $scope.concatImages(data);
                });
        }
    }

    $scope.updateClipboardInput = function (image) {
        $scope.imageSrcFocus = image;
    }

}



