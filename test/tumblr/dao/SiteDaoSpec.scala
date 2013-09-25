package tumblr.dao

import org.specs2.mutable._

import play.api.test.Helpers.defaultAwaitTimeout

import tumblr.model._

class SiteDaoSpec extends Specification {

  "The SiteDao" should {

    "find sites grouped by site type" in new FakeDaoApp {
      import tumblr.model.AdminSiteJSON.writes
      LocalSiteDao.LocalSites.foreach(site => SiteDao.save(site))

      val sitesInfo = play.api.test.Helpers.await(SiteDao.findAllGroupedBySiteType())
      sitesInfo.sitesByType.size should be equalTo(3)
      sitesInfo.sitesByType.foreach(_.sites.size must be >=(0))
      sitesInfo.sites.size must be equalTo(LocalSiteDao.LocalSites.size)
    }

  }

}