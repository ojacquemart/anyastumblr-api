/**
 * AngularJS - Gifs controller.
 */

function hfrGifsController($scope, $http) {

    $scope.images = [];

    $scope.concatImages = function (data) {
        $scope.images = $scope.images.concat(data.images)
        $scope.gifs = data;
        $scope.gifs.images = $scope.images;
    }

    $scope.loadGifs = function () {
        $http.get("topics/" + $scope.topicId + "/gifs")
            .success(function (data) {
                $scope.images = [];
                $scope.concatImages(data);
            });

    };
    $http.get('topics').success(function (data) {
        $scope.topics = data;
        $scope.topicId = data[0].id;

        $scope.loadGifs();
    });


    $scope.loadNextGifs = function () {
        $http.get("topics/" + $scope.topicId + "/page/" + $scope.gifs.previousPage)
            .success(function (data) {
                $scope.concatImages(data);
            });
    }

}



