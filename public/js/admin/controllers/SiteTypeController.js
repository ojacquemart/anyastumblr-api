'use strict';

function SiteTypeController($scope, SiteType) {

    $scope.emptySiteType = function() {
        var newTypeSite = new SiteType();
        newTypeSite.ordinal = -1;
        newTypeSite.enabled = true;

        return newTypeSite;
    };

    // creates a siteType using createForm and refreshes list
    $scope.create = function () {
        $scope.newSiteType.$save(function () {
            $scope.newSiteType = $scope.emptySiteType();
            $scope.siteTypes = SiteType.query();
        })
    }

    // removes a siteType and refreshes list
    $scope.remove = function (siteType) {
        // TODO: use angular-strap and popover: http://mgcrea.github.io/angular-strap/#/popover
        var deleteUser = confirm("Are you sure to delete the site type '" + siteType.slug + "'?");

        if (deleteUser) {
            siteType.$remove(function () {
                $scope.siteTypes = SiteType.query();
            })
        }
    }

    // updates a siteType and refreshes list
    $scope.update = function (siteType) {
        siteType.$update(function () {
            $scope.siteTypes = SiteType.query();
        })
    }

    // @OnLoad...
    $scope.siteTypes = SiteType.query();
    $scope.newSiteType = $scope.emptySiteType();

}