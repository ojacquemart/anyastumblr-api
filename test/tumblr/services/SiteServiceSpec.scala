package tumblr.services

import org.specs2.mutable.Specification

import play.api.test.Helpers.defaultAwaitTimeout

import tumblr.model._
import tumblr.dao._

object SiteServiceSpec extends Specification {

  "The SiteService" should {

    "find sites grouped by site type" in new FakeDaoApp {
      import tumblr.model.AdminSiteJSON.writes
      LocalSiteDao.LocalSites.foreach(site => SiteDao.save(site))

      val sitesInfo = play.api.test.Helpers.await(SiteService.findAllGroupedBySiteType())
      sitesInfo.sitesByType.size should be equalTo(3)
      sitesInfo.sitesByType.foreach(_.sites.size must be >=(0))
      sitesInfo.sites.size must be equalTo(LocalSiteDao.LocalSites.size)
    }

  }

 }
