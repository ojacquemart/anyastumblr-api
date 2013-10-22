'use strict';

angular.module('admin.auth', [])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/admin/login', {
                templateUrl: 'scripts/admin/auth/login.tpl.html',
                controller: 'LoginCtrl',
                access: "admin",
                login: true
            })
    }])
    .service('Auth', function ($cookieStore, $http) {
        var token = $cookieStore.get("user") || "";

        var storeToken = function (_token) {
            token = _token;
            $cookieStore.put("user", _token);
        };

        var removeToken = function () {
            $cookieStore.remove("user");
            token = "";
        }

        return {
            getToken: function () {
                return token;
            },
            isLoggedIn: function () {
                return token.length > 0;
            },
            login: function (user, success, error) {
                $http.post('/tumblr/administration/login', user).success(function (data) {
                    storeToken(data);
                    success(data);
                }).error(error);
            },
            logout: function (success) {
                removeToken();
                success();
            }
        };

    })
    .controller('LoginCtrl', function ($scope, $location, Auth) {
        $scope.user = { name: "", password: "IDj89nL6I69431yD4L3l" };
        $scope.error = "";

        $scope.login = function () {

            Auth.login($scope.user, function (data) {
                    $location.path("/admin/index");
                },
                function (error) {
                    $scope.error = "Invalid username or password.";
                })

        }
    })
;
