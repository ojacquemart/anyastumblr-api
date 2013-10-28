"use strict"

angular.module('admin.sites', [ 'admin.siteTypes' ])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/admin/sites', {
                templateUrl: 'scripts/admin/sites/sites.tpl.html',
                controller: 'SiteCtrl',
                access: "admin"
            })
            .when('/admin/sites/new', {
                templateUrl: "scripts/admin/sites/sites-form.tpl.html",
                controller: "NewSiteCtrl",
                access: "admin"
            })
            .when('/admin/sites/edit/:id', {
                controller: "EditSiteCtrl",
                resolve: {
                    site: function(SiteLoader) {
                        return SiteLoader();
                    }
                },
                templateUrl: "scripts/admin/sites/sites-form.tpl.html",
                access: "admin"
            })
        ;
    }])
    .factory("Site", function (secureResource) {
        return secureResource("/api/tumblr/admin/sites/:id", { id: "@id" });
    })
    .factory("SiteLoader", ["Site", "$route", "$q",
        function (Site, $route, $q) {
            return function () {
                var delay = $q.defer();
                var siteId = $route.current.params.id;
                Site.get({id: siteId}, function (recipe) {
                    delay.resolve(recipe);
                }, function () {
                    delay.reject("Unable to fetch site " + siteId);
                });
                return delay.promise;
            };
    }])
    .controller('SiteCtrl', function ($scope, Site) {
        $scope.sites = Site.query();

        $scope.delete = function(site) {
            // TODO: use angular-strap and popover: http://mgcrea.github.io/angular-strap/#/popover
            var deleteSite = confirm("Are you sure to delete the site '" + site.name + "'?");

            if (deleteSite) {
                site.$remove(function() {
                    $scope.sites = Site.query();
                });
            }
        }
    })
    .controller('RootFormSiteCtrl', function ($scope, SiteType) {
        $scope.checkImageRule = function() {
            if (typeof $scope.site.configuration.imageRule === "undefined") {
                $scope.site.configuration.imageRule = { "exclude": "", "startsWith": [] };
            }
            if (typeof $scope.site.configuration.imageRule.startsWith === "undefined") {
                $scope.site.configuration.imageRule.startsWith = [];
            }
        }

        $scope.addImageRuleException = function() {
            $scope.checkImageRule();
            $scope.site.configuration.imageRule.startsWith.push( { value: "???" });
        }

        $scope.removeImageRuleException = function(index) {
            $scope.site.configuration.imageRule.startsWith.splice(index, 1);
        }
    })
    .controller('NewSiteCtrl', function ($scope,Site, SiteType) {
        $scope.site = new Site();
        $scope.siteTypes = SiteType.query();

        $scope.save = function() {
            $scope.site.$save(function (site) {
                $location.path("/admin/sites");
            });
        }
    })
    .controller('EditSiteCtrl', function ($scope, $location, site, SiteType) {
        $scope.site = site;
        $scope.siteTypes = SiteType.query(function() {
            $scope.site.siteType = $scope.updateSiteType();
        });

        $scope.updateSiteType = function() {
            function getSiteType() {
                var siteTypes = $scope.siteTypes;
                for (var i = 0; i < siteTypes.length; i++) {
                    if ($scope.site.siteType.name == siteTypes[i].name) {
                        return siteTypes[i];
                    }
                }

                return siteTypes[0];
            }

            return getSiteType();
        }

        $scope.save = function() {
            $scope.site.$update(function (site) {
                $location.path("/admin/sites");
            });
        }
    })
;

