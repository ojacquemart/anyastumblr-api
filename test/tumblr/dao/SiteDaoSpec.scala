package tumblr.dao

import org.specs2.mutable._

import play.api.Logger
import play.api.test.Helpers._


import tumblr.model._

class SiteDaoSpec extends Specification {

  "The SiteDao" should {

    "find sites grouped by site type" in new FakeDaoApp {
      import tumblr.model.AdminSiteJSON.writes
      LocalSiteDao.LocalSites.foreach(site => SiteDao.save(site))

      val sitesBySiteType = await(SiteDao.findAllGroupedBySiteType())
      sitesBySiteType.size should be equalTo(3)
      sitesBySiteType.foreach(_.sites.size must be >=(0))
    }

  }

}