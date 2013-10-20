'use strict';

angular.module('uiApp')
  .controller('AdminSiteTypesCtrl', function ($scope, $location, Auth) {
       $scope.logout = function() {
            Auth.logout(function() {
                $location.path("/admin/login");
            });

       }
  });
