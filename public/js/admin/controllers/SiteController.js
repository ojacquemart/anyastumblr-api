'use strict';

function SiteController($scope, Site) {
    $scope.sites = Site.query();

    $scope.delete = function(site) {
        site.$remove(function() {
           $scope.sites = Site.query();
        });
    }
}

function RootSiteFormController($scope) {

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

function NewSiteController($scope, $location, Site) {
    $scope.site = new Site();

    $scope.save = function($scope) {
        $scope.site.$save(function (site) {
            $location.path("/sites");
        });
    }
}

function EditSiteController($scope, $location, site) {
    $scope.site = site;

    $scope.save = function() {
        $scope.site.$update(function (site) {
            $location.path("/sites");
        });
    }
}
