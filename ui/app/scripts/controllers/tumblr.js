'use strict';

angular.module('uiApp')
    .controller('TumblrCtrl', function ($scope, $cookieStore, $routeParams, Auth, $location, tumblrApi, sitesNavigator, pageNavigator, animations) {

        $scope.sitesByType = null;

        $scope.page = null;
        $scope.site = null;

        $scope.init = function (data) {
            sitesNavigator.init(data);
        };

        $scope.initPage = function () {
            $scope.sitesByType = sitesNavigator.getSitesByType();
            $scope.site = sitesNavigator.storeSite($routeParams.siteId);

            var pageNumber = $routeParams.pageNumber;
            if (pageNumber == null) {
                $scope.loadImages();
            } else {
                $scope.loadImagesByPageNumber(pageNumber);
            }
        };

        $scope.loadImages = function () {
            $scope.page = tumblrApi.get({ "id": sitesNavigator.getCurrentSiteId() });
        };

        $scope.loadImagesByPageNumber = function (pageNumber) {
            if (pageNumber !== -1) {
                var params = {
                    "id": sitesNavigator.getCurrentSiteId(),
                    "pageParam": pageNumber
                }
                $scope.page = tumblrApi.getPageByNumber(params, function () {
                    animations.toTop();
                });
            }
        };

        $scope.goToPage = function (pageNumber) {
            if (pageNumber != -1 && !isNaN(pageNumber)) {
                return $location.path("/sites/" + sitesNavigator.getCurrentSiteId() + "/page/" + pageNumber);
            }

            return null;
        };

        $scope.goToPreviousPage = function () {
            return $scope.goToPage(pageNavigator.getPreviousPageNumber($scope.page));
        };

        $scope.goToNextPage = function () {
            return $scope.goToPage(pageNavigator.getNextPageNumber($scope.page));
        };

        /**
         * Change site by index, used by the keyboard navigation.
         */
        $scope.loadSiteByIndex = function (index) {
            var nextSiteId = sitesNavigator.getSiteByIndex(index);
            if (nextSiteId != null) {
                return $location.path("/sites/" + nextSiteId);
            }

            return null;
        };

        $scope.refreshPage = function () {
            $scope.loadImages();
        };

        $scope.canDisplayLastPageInfos = function () {
            return pageNavigator.hasLastPageInfos($scope.page);
        };

        /**
         * Keyboard navigation, FPS like and left/right arrows.
         * $scope.$apply is used because we need to change the $location.path outside an angular expression.
         *
         * @param key the keycode.
         */
        $scope.handleKeypress = function (key) {
            // left = 37, right = 39, q = 81, d = 68, z = 90, s = 83, r = 82
            switch (key) {
                // Previous page = left | q
                case 37:
                case 81:
                    $scope.$apply($scope.goToPreviousPage());
                    break;
                // Next page = right | d
                case 39:
                case 68:
                    $scope.$apply($scope.goToNextPage());
                    break;
                // Previous site = up | z
                case 90:
                    $scope.$apply($scope.loadSiteByIndex(-1));
                    break;
                // Next site = down | s
                case 83:
                    $scope.$apply($scope.loadSiteByIndex(1));
                    break;
                // Refresh = r
                case 82:
                    $scope.refreshPage();
                    break;
            }
        };

        /**
         * @OnLoad...
         */

        if (!sitesNavigator.isInitialized()) {
            tumblrApi.query(function (data) {
                $scope.init(data);
                $scope.initPage();
            });
        } else {
            $scope.initPage();
        }

        // FIXME: see to use angularjs directive
        // Dont see how to put a directive on the body and use this controller.
        $(document).bind("keydown", function (e) {
            $scope.handleKeypress(e.keyCode);
        });

        // Unbind keydown event on controller destroy.
        $scope.$on('$destroy', function () {
            $(document).unbind("keydown");
        });
    });