"use strict";

angular.module("admin.navbar", [])
    .config(['$routeProvider', function($routeProvider) {

    }])
    .directive('adminNavbar', function () {
        return {
            restrict: "E",
            link: function (scope) {
                scope.templateUrl = "scripts/admin/navbar/navbar.tpl.html";
            },
            template: '<div ng-include="templateUrl"></div>'
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