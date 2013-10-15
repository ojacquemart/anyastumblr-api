'use strict';

angular.module('uiApp')
  .service('sitesNavigator', function sitesNavigator() {
        var currentSiteIndex = -1;
        var sites = null;
        var sitesByType = null;

        return {
            isInitialized: function() {
                return sites != null;
            },
            init: function (data) {
                sitesByType = data.sitesByType;
                sites = data.sites;
            },
            getSitesByType: function() {
                return sitesByType;
            },
            storeSite: function(siteId) {
                if (siteId == null) {
                    siteId = sites[0].id;
                }
                this.setCurrentSiteIndex(siteId);

                return this.getCurrentSite();
            },
            setCurrentSiteIndex: function (siteId) {
                function searchForSiteIdIndex() {
                    var sitesSize = sites.length;
                    for (var i = 0; i < sitesSize; i++) {
                        if (sites[i].id == siteId) {
                            return i;
                        }
                    }

                    return 0;
                }

                currentSiteIndex = searchForSiteIdIndex();
            },
            getCurrentSite: function() {
                return sites[currentSiteIndex];
            },
            getCurrentSiteId: function() {
                return this.getCurrentSite().id;
            },
            getSiteByIndex: function(index) {
                if (this.canNavigateToIndex(index)) {
                    var nextIndex = this.getNextIndex(index);
                    var site = sites[ nextIndex];

                    return site.id;
                }

                return null;
            },
            canNavigateToIndex: function (index) {
                var nextIndex = this.getNextIndex(index);

                return nextIndex >= 0 && nextIndex < sites.length;
            },
            getNextIndex: function(index) {
                return currentSiteIndex + index;
            }
        };
  });
