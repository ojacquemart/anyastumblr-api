function HfrGifsCtrl($scope, $http) {

    $http.get('topics').success(function (data) {
        $scope.topics = data;
        $scope.topicId = data[0].id;
    });

    /**
     * Load gifs from topicId.
     */
    $scope.loadGifs = function () {
        $http.get("topics/" + $scope.topicId + "/gifs")
            .success(function (data) {
                $scope.gifs = data;
            });
    };

    $scope.loadGifs();

    $scope.changePage = function(pageNumber) {
        if (pageNumber != -1) {
            $http.get("topics/" + $scope.topicId + "/page/" + pageNumber)
                .success(function (data) {
                    $scope.gifs = data;
                });
        }
    }

    $scope.previousPage = function () {
        $scope.changePage($scope.gifs.previousPage)
    }

    $scope.nextPage = function () {
        $scope.changePage($scope.gifs.nextPage)
    }
}

