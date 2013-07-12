'use strict';

function SiteTypeController($scope, SiteType) {
    $scope.siteTypes = SiteType.query();
    $scope.createForm = {};

    // creates a siteType using createForm and refreshes list
    $scope.create = function () {
        var siteType = new SiteType({name: $scope.createForm.name });
        siteType.$save(function () {
            $scope.createForm = {};
            $scope.siteTypes = SiteType.query();
        })
    }

    // removes a siteType and refreshes list
    $scope.remove = function (siteType) {
        siteType.$remove(function () {
            $scope.siteTypes = SiteType.query();
        })
    }

    // updates a siteType and refreshes list
    $scope.update = function (siteType) {
        siteType.$update(function () {
            $scope.siteTypes = SiteType.query();
        })
    }

}