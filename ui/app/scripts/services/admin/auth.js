'use strict';

angular.module('uiApp')
    .service('Auth', function AdminAuth($cookieStore, $http) {
        var token = $cookieStore.get("user") || "";

        var storeToken = function(_token) {
            token = _token;
            $cookieStore.put("user", _token);
        };

        var removeToken = function() {
            $cookieStore.remove("user");
            token = "";
        }

        return {
            getToken: function() {
                return token;
            },
            isLoggedIn: function() {
                return token.length > 0;
            },
            login: function (user, success, error) {
                $http.post('/tumblr/administration/login', user).success(function (data) {
                    storeToken(data);
                    success(data);
                }).error(error);
            },
            logout: function(success) {
                removeToken();
                success();
            }
        };

    });
