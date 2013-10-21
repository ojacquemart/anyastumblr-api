'use strict';

function AdminSitesCtrl($scope, Site) {
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
}

function AdminRootSiteCtrl($scope, SiteType) {
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
}

function AdminNewSiteCtrl($scope, $location, Site, SiteType) {
    $scope.site = new Site();
    $scope.siteTypes = SiteType.query();

    $scope.save = function() {
        $scope.site.$save(function (site) {
            $location.path("/admin/sites");
        });
    }
}

function AdminEditSiteCtrl($scope, $location, site, SiteType) {
    $scope.site = site;
    $scope.siteTypes = SiteType.query(function(siteTypes) {
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
}
