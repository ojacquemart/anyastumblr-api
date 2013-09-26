package tumblr.services

import org.specs2.mutable.Specification

import play.api.test.Helpers.defaultAwaitTimeout
import play.api.test.Helpers.await

import tumblr.model._
import tumblr.dao._

object SiteStatsServiceSpec extends Specification {

  // Hfr images étonnantes
  val site1: Site = LocalSiteDao.LocalSites(0)
  // Joiesducode
  val site2: Site = LocalSiteDao.LocalSites(2)
  // Joiesdusysadmin
  val site3: Site = LocalSiteDao.LocalSites(3)

  val sites = List(site1, site2, site3)
  val pages: List[Page] = List(
    getPage(site1.slug, 1, 2),
    getPage(site1.slug, 2, 10),
    getPage(site1.slug, 3, 20),
    getPage(site2.slug, 1, 4),
    getPage(site2.slug, 2, 44)
  )

  def saveSites(sites: List[Site]) {
    import tumblr.model.AdminSiteJSON.writes
    sites.foreach(SiteDao.save(_))
  }

  def savePages(pages: List[Page]) {
    pages.foreach(PageDao.save(_))
  }

  def getPage(slug: String, pageNumber: Int, nbViews: Int) = {
    new Page(slug, pageNumber, nbViews, List(), List(), Link.get("???", "foo", 1))
  }

  def assertSiteStat(siteStat: SiteStat, expectedName: String, tupleNbDocsAndViews: (Int, Int)) = {
    siteStat.name must be equalTo(expectedName)
    siteStat.nbDocuments must be equalTo(tupleNbDocsAndViews._1)
    siteStat.nbViews must be equalTo(tupleNbDocsAndViews._2)
  }

  "Site Stats" should {

    "generate stats" in new FakeDaoApp {
      saveSites(sites)
      savePages(pages)

      val stats: Stats = play.api.test.Helpers.await(SiteStatsService.generate())
      stats.sitesStats.size must be equalTo(sites.size)
      stats.nbDocuments must be equalTo(pages.size)
      stats.nbViews must be equalTo(pages.map(_.nbViews).sum)

      // Map by site name with nb docs and nb views
      val pagesStatsBySiteName = pages.groupBy(page => {
        sites.find(site => site.slug == page.siteId).get.name
      }).mapValues(pages => (pages.size, pages.map(_.nbViews).sum))

      assertSiteStat(stats.sitesStats(0), site1.name, pagesStatsBySiteName.get(site1.name).get)
      assertSiteStat(stats.sitesStats(1), site2.name, pagesStatsBySiteName.get(site2.name).get)
      assertSiteStat(stats.sitesStats(2), site3.name, (0, 0))
    }
  }

}
