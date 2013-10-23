"use strict";

angular.module("admin.navbar", [])
    .config(['$routeProvider', function($routeProvider) {

    }])
    .directive('adminNavbar', function () {
        return {
            restrict: "E",
            templateUrl: 'scripts/admin/navbar/navbar.tpl.html'
        }
    })
    .controller('NavbarCtrl', function ($scope, $location, Auth) {
        $scope.logout = function() {

            Auth.logout(function() {
                $location.path("/admin/login");
            });
        }
    });
;