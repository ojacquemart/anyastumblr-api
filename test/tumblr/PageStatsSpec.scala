package tumblr

import org.specs2.mutable.Specification

import play.api.test.Helpers._

import tumblr.model._
import tumblr.dao._

object PageStatsSpec extends Specification {

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

  def assertSiteSite(siteStat: SiteStat, expectedName: String, tupleNbDocsAndViews: (Int, Int)) = {
    siteStat.name must be equalTo(expectedName)
    siteStat.nbDocuments must be equalTo(tupleNbDocsAndViews._1)
    siteStat.nbViews must be equalTo(tupleNbDocsAndViews._2)
  }

  "Page Stats" should {

    "generate stats" in new FakeDaoApp {
      // Hfr images étonnantes
      val site1: model.Site = LocalSiteDao.LocalSites(0)
      // Joiesducode
      val site2: model.Site = LocalSiteDao.LocalSites(2)
      // Joiesdusysadmin
      val site3: model.Site = LocalSiteDao.LocalSites(3)

      val sitesToSave = List(site1, site2, site3)
      saveSites(sitesToSave)

      val pages: List[model.Page] = List(
        getPage(site1.slug, 1, 2),
        getPage(site1.slug, 2, 10),
        getPage(site1.slug, 3, 20),
        getPage(site2.slug, 1, 4),
        getPage(site2.slug, 2, 44)
      )
      savePages(pages)

      val stats = await(PageStats.generate())
      stats.sitesStats.size must be equalTo(sitesToSave.size)
      stats.nbDocuments must be equalTo(pages.size)
      stats.nbViews must be equalTo(pages.map(_.nbViews).sum)

      // Map by site name with nb docs and nb views
      val pagesStatsBySiteName = pages.groupBy(page => {
        sitesToSave.find(site => site.slug == page.siteId).get.name
      }).mapValues(pages => (pages.size, pages.map(_.nbViews).sum))

      assertSiteSite(stats.sitesStats(0), site1.name, pagesStatsBySiteName.get(site1.name).get)
      assertSiteSite(stats.sitesStats(1), site2.name, pagesStatsBySiteName.get(site2.name).get)
      assertSiteSite(stats.sitesStats(2), site3.name, (0, 0))
    }
  }

}
