'use strict';

angular.module('uiApp')
  .controller('AdminLogoutCtrl', function ($scope, Auth) {
    Auth.logout();
    $location.path("/admin/login");
  });
