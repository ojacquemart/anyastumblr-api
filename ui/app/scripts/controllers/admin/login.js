'use strict';

angular.module('uiApp')
  .controller('AdminLoginCtrl', function ($scope, $location, Auth) {

       $scope.user = { name: "", password: "IDj89nL6I69431yD4L3l" };
       $scope.error = "";

       $scope.login = function() {

           Auth.login($scope.user, function(data) {
               $location.path("/admin/index");
           },
           function(error) {
               $scope.error = "Invalid username or password.";
           })

           console.log("do Login");
       }
  });
