'use strict';

function SiteController($scope, Site) {
    $scope.sites = Site.query();

    $scope.delete = function(site) {
        site.$remove(function() {
           $scope.sites = Site.query();
        });
    }
}

function RootSiteFormController($scope, SiteType) {
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

function NewSiteController($scope, $location, Site, SiteType) {
    $scope.site = new Site();
    $scope.siteTypes = SiteType.query();

    $scope.save = function() {
        $scope.site.$save(function (site) {
            $location.path("/sites");
        });
    }
}

function EditSiteController($scope, $location, site, SiteType) {
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
           $location.path("/sites");
        });
    }
}
