package tumblr.dao

import org.specs2.mutable._

import play.api.test.Helpers.defaultAwaitTimeout

import tumblr.model._

class SiteDaoSpec extends Specification {

  "The SiteDao" should {

    "findBySlug" in new FakeDaoApp {
      import tumblr.model.AdminSiteJSON.writes
      LocalSiteDao.LocalSites.foreach(site => SiteDao.save(site))

      LocalSiteDao.LocalSites.foreach(site => {
        val siteFound = play.api.test.Helpers.await(SiteDao.findBySlug(site.slug))
        siteFound must not be None
      })
    }

  }

}