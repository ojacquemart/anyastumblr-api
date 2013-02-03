function TopicListCtrl($scope, $http) {

    $http.get('topics/list').success(function(data) {
        $scope.topics = data;
    });
    $http.get("topics/1/gifs").success(function(data) {
       $scope.gifs = data;
    });

    $scope.consoleTest = function() {
        console.log("???");
    };

    $scope.changePage = function(number) {
        console.log(number);
        // TODO
    }
}
