'use strict';

function SiteController($scope, Site) {
    $scope.sites = Site.query();
}